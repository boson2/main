package org.tok.cust.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.tool.model.ZipCodeVo;
import org.tok.cust.user.model.UserVo;
import org.tok.view.Talk;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.StandardDaoSupport;


public abstract class AbstractSiteUserManagerDAO extends StandardDaoSupport implements SiteUserManagerDAO
{
	private static final Log log = LogFactory.getLog( AbstractSiteUserManagerDAO.class );
	
	public AbstractSiteUserManagerDAO()
	{
		 
	}
	
	public void authenticate(ConnectionContext connCtx, String userId, String passwd) throws UserException
	{
		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		StringBuffer pageQuery = new StringBuffer();
        
    	try {
    		pageQuery.append( "SELECT a.PRINCIPAL_ID, a.COLUMN_VALUE, a.UPDATE_REQUIRED, a.AUTH_FAILURES, b.IS_ENABLED " );
    		pageQuery.append( "FROM SECURITY_CREDENTIAL a, SECURITY_PRINCIPAL b " );
    		pageQuery.append( "WHERE a.PRINCIPAL_ID=b.PRINCIPAL_ID AND b.PRINCIPAL_TYPE='U' AND b.SHORT_PATH=? " );
    		
    	    conn = connCtx.getConnection();
    	    
    	    selectStmt = conn.prepareStatement( pageQuery.toString() );
    	    selectStmt.setString(1, userId);
    	    rs = selectStmt.executeQuery();
            if( rs.next() ) {
            	int principal_id = rs.getInt(1);
            	String pw = rs.getString(2);
            	boolean isUpdateRequired = rs.getBoolean(3);
            	int loginFailLimit = rs.getInt(4);
            	boolean isEnabled = rs.getBoolean(5);
            	
            	if( isEnabled == false ) {
            		throw new UserException( "pt.ev.login.label.ErrorCode.3" ); // 비활성화 계정
            	}

            	int portalLoginFailLimit = Talk.getConfiguration().getInt("portal.login.failLimit", 3);
            	if( loginFailLimit >= portalLoginFailLimit ) {
            		throw new UserException( "pt.ev.login.label.ErrorCode.8" ); // 비밀번호 오류횟수 초과
            	}

            	if( pw.equals(passwd) == false ) {
            		pageQuery.delete(0, pageQuery.length());
            		pageQuery.append( "UPDATE SECURITY_CREDENTIAL SET AUTH_FAILURES=? WHERE PRINCIPAL_ID=? " );
            		updateStmt = conn.prepareStatement( pageQuery.toString() );
            		updateStmt.setInt(1, loginFailLimit+1);
            		updateStmt.setInt(2, principal_id);
            		
            		updateStmt.executeUpdate();
            		
            		conn.commit();
            		
            		throw new UserException( "pt.ev.login.label.ErrorCode.2" ); // 비밀번호 오류
            	}
            	
                pageQuery.delete(0, pageQuery.length());
        		pageQuery.append( "UPDATE SECURITY_CREDENTIAL SET AUTH_FAILURES=? WHERE PRINCIPAL_ID=? " );
        		updateStmt = conn.prepareStatement( pageQuery.toString() );
        		updateStmt.setInt(1, 0);
        		updateStmt.setInt(2, principal_id);
        		
        		updateStmt.executeUpdate();
            }
            else {
            	throw new UserException( "pt.ev.login.label.ErrorCode.1" ); // 사용자 없음
            }
            
    	}
    	catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
    		e.printStackTrace();
            throw new UserException(e);
        } 
        finally
        {
            try
            {
                if (null != rs) rs.close();
            	if (null != selectStmt) selectStmt.close();
            	if (null != updateStmt) updateStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public Map getUserInfoMap(ConnectionContext connCtx, String userId) throws UserException 
    {
    	Map userInfoMap = new HashMap();
    	Connection conn = null;
    	PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
    	try {
    		pageQuery.append( "SELECT a.reg_no, a.org_cd, a.emp_no, a.kind_cd, a.type_cd, a.nm_kor, a.nm_eng, a.nm_nic, a.mobile_tel, a.email_addr, b.theme, a.user_info04," +
    				" a.lang_knd, b.principal_id, b.principal_name,b.auth_method, b.default_page, c.UPDATE_REQUIRED, a.reg_datim, a.home_Zip, a.home_Addr1, a.home_Addr2, " +
    				" a.last_logon, a.user_info10, a.sex_flag, a.open_flag, TO_CHAR(a.birth_ymd, 'yyyy-mm-dd'), a.home_addr1, a.home_addr2, a.intro, a.grade_cd, d.code_name " +
    				" FROM USERPASS a INNER JOIN SECURITY_PRINCIPAL b ON a.user_id=b.short_path" +
    				"						   INNER JOIN SECURITY_CREDENTIAL c ON b.PRINCIPAL_ID=c.PRINCIPAL_ID" +
    				"						   LEFT OUTER JOIN CODEBASE d ON a.grade_cd=d.code AND d.system_code='PT' AND d.code_id='118' AND d.lang_knd='ko'" +
    				" WHERE b.principal_type='U' and a.user_id=? " );
    		//log.debug("*** pageQuery=" + pageQuery.toString() );
    		
    		conn = connCtx.getConnection();
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setString(1, userId);
            rs = selectStmt.executeQuery();
            if( rs.next() ) {
            	
            	userInfoMap.put("user_id", userId);
            	userInfoMap.put("reg_no", rs.getString(1));
            	userInfoMap.put("org_cd", rs.getString(2));
            	userInfoMap.put("emp_no", rs.getString(3));
            	userInfoMap.put("kind_cd", rs.getString(4));
            	userInfoMap.put("type_cd", rs.getString(5));
            	userInfoMap.put("nm_kor", rs.getString(6));
            	userInfoMap.put("nm_eng", rs.getString(7));
            	userInfoMap.put("nm_nic", rs.getString(8));
            	userInfoMap.put("mobile_tel", rs.getString(9));
            	userInfoMap.put("email_addr", rs.getString(10));
            	userInfoMap.put("theme", rs.getString(11));
            	userInfoMap.put("org_name", rs.getString(12));
            	userInfoMap.put("lang_knd", rs.getString(13));
            	userInfoMap.put("principal_id", rs.getString(14));
            	userInfoMap.put("principal_name", rs.getString(15));
            	userInfoMap.put("auth_method", rs.getString(16));
            	if( rs.getString(17) != null && rs.getString(17).length()>0 ) {
            		userInfoMap.put("default_page", rs.getString(17));
            	}
            	userInfoMap.put("update_required", rs.getString(18));
            	userInfoMap.put("reg_datim", rs.getDate(19));
            	userInfoMap.put("homeZip", rs.getString(20));
            	userInfoMap.put("homeAddr1", rs.getString(21));
            	userInfoMap.put("homeAddr2", rs.getString(22));
            	
            	userInfoMap.put("last_login_time", rs.getDate(23));
            	userInfoMap.put("imei", rs.getString(24));
            	
            	userInfoMap.put("sex_flag", rs.getString(25));
            	userInfoMap.put("open_flag", rs.getString(26));
            	
            	userInfoMap.put("birth_ymd", rs.getString(27));
            	/*
            	Timestamp birth_ymd = rs.getTimestamp(26);
            	if( birth_ymd != null ) {
            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            		String birthYmd = formatter.format(birth_ymd);
            		userInfoMap.put("birth_ymd", birthYmd);
            	}
            	*/
            	userInfoMap.put("home_addr1", rs.getString(28));
            	userInfoMap.put("home_addr2", rs.getString(29));
            	userInfoMap.put("intro", rs.getString(30));
            	userInfoMap.put("grade_cd", rs.getString(31));
            	userInfoMap.put("grade_cd_nm", rs.getString(32));
            	//System.out.println("######### userInfoMap=" + userInfoMap);
            }
            else {
            	return null;
            }
            
            if (null != rs) rs.close();
            if (null != selectStmt) selectStmt.close();
            
            // 주민번호가 동일한 모든 ID 리스트 저장
            pageQuery.delete(0, pageQuery.length());
            pageQuery.append( "SELECT a.principal_id, a.short_path " );
            pageQuery.append( "FROM security_principal a, userpass b " );
            pageQuery.append( "WHERE principal_type='U' and a.short_path=b.user_id and reg_no=? " );
            selectStmt = conn.prepareStatement( pageQuery.toString() );
            selectStmt.setString(1, (String)userInfoMap.get("reg_no"));
    		rs = selectStmt.executeQuery();
    		List principalids = new ArrayList();
            while( rs.next() ) {
            	Map principalidMap = new HashMap();
            	principalidMap.put("principal_id", rs.getString(1) );
            	principalidMap.put("short_path", rs.getString(2) );
            	principalids.add( principalidMap );
            }
            userInfoMap.put("userids", principalids);
            
            getDetailUserInfomation(conn, userInfoMap); 
    	}
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
                if (null != rs) rs.close();
                if (null != selectStmt) selectStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
    	
    	return userInfoMap;
    }
	
	protected void getDetailUserInfomation(Connection conn, Map userInfoMap) throws Exception 
	{
    	PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
        	/**************************************************************************************/
        	/*  UserInfoMap안에 있는 역할과 그룹관련 정보
        	 * 
        	 *  groups : 그룹 shortPath (그룹 ID) 목록
        	 *  groupIds : 그룹 principalId (","로 구분) 
        	 *  roles : 역할 shortPath (역할 ID) 목록
        	 *  roleIds : 역할 principalId (","로 구분)
        	 *  principalMap : 그룹 정보맵과 역할 정보맵을 담고 있다.
        	 *    group/principalId : HashMap (principal_id, short_path, theme, default_page, site_name, principal_name)
        	 *    role/principalId : HashMap (principal_id, short_path, theme, default_page, site_name, principal_name)
        	 */
        	/**************************************************************************************/
        	// 그룹 및 역할의 상세 정보를 Map에 보관한다.
        	Map principalMap = new HashMap();
			List groups = new ArrayList();
			List roles = new ArrayList();
			List groupIdList = new ArrayList();
			List groupNameList = new ArrayList();
			
	        userInfoMap.put("groups", groups);
	        userInfoMap.put("roles", roles);
	        userInfoMap.put("principalMap", principalMap);

	        String principal_id = (String)userInfoMap.get("principal_id");
        	
			// 사용자에게 할당된 그룹정보 저장
	        pageQuery.delete(0, pageQuery.length());
	        pageQuery.append( "SELECT a.principal_id, a.short_path, a.theme, a.default_page, a.site_name, a.principal_name " );
	        pageQuery.append( "FROM security_principal a, security_user_group b " );
	        pageQuery.append( "WHERE a.principal_id=b.group_id AND b.user_id=? " );
	        pageQuery.append( "ORDER BY SORT_ORDER ASC " );
	        //pageQuery.append( "WHERE a.principal_id=b.group_id AND b.user_id=(SELECT principal_id FROM SECURITY_PRINCIPAL WHERE principal_type='U' and short_path=?)" );
			//log.debug("*** query=" + pageQuery.toString() );
	        selectStmt = conn.prepareStatement( pageQuery.toString() );
	        selectStmt.setInt(1, Integer.parseInt(principal_id));
			//selectStmt.setString(1, userId);
			rs = selectStmt.executeQuery();

	        while( rs.next() ) {
	        	Map groupMap = new HashMap();
	        	groupMap.put("principal_id", rs.getString(1));
	        	groupMap.put("short_path", rs.getString(2));
	        	groupMap.put("theme", rs.getString(3));
	        	groupMap.put("default_page", rs.getString(4));
	        	groupMap.put("site_name", rs.getString(5));
	        	groupMap.put("principal_name", rs.getString(6));
	        	principalMap.put("group/" + rs.getString(2), groupMap);
	        	
	        	groupIdList.add( rs.getString(1) );
	        	groups.add( rs.getString(2) );
	        	groupNameList.add( rs.getString(6) );
	        	
	        }
	        
	        userInfoMap.put("groupNameList", groupNameList);
        	userInfoMap.put("groupIdList", groupIdList);
        	
	        //if( groups.size() == 0 ) return;
			String themeName = (String)userInfoMap.get("theme");
			String defaultPage = (String)userInfoMap.get("default_page");
			if( defaultPage == null || defaultPage.length()==0 ) {
				userInfoMap.put("hasUserPage", "true");
			}
	        
	        // 테마가 지정 안되어 있는 경우 상위 그룹의 테마를 지정하도록 한다.
			// 최종적으로 없는 경우에는 "enview"테마가 지정된다.
	        if( themeName == null || themeName.length()==0 ) {
	        	if( groups.size() > 0 ) {
	        		Map groupMap = (Map)principalMap.get( "group/" + (String)groups.get(0) );
	        		if( groupMap != null ) {
	        			String groupTheme = (String)groupMap.get("theme");
	        			if( groupTheme != null ) {
	        				userInfoMap.put("theme", groupTheme);
	        			}
	        			else {
	        				//System.out.println("### 1");
	        				groupTheme = getParentGroupTheme(conn, (String)groupMap.get("principal_id"));
	        				//System.out.println("### groupTheme=" + groupTheme);
	        				if( groupTheme != null ) {
	        					//groupMap.put("theme", groupTheme);
		        				userInfoMap.put("theme", groupTheme);
	        				}
	        				else {
	        					userInfoMap.put("theme", "enview");
	        					System.out.println("### Cannot find group theme. so use default theme[ enview ]");
	        				}
	        			}
	        		}
	        	}
        	}
	        
	        // 그룹의 rootSite를 결정한다.
	        // 없는 경우에는 상위 그룹의 rootSite를 지정하도록 한다.
			String siteName = (String)userInfoMap.get("site_name");
	        if( siteName == null || siteName.length()==0 ) {
	        	if( groups.size() > 0 ) {
	        		Map groupMap = (Map)principalMap.get( "group/" + (String)groups.get(0) );
	        		if( groupMap != null ) {
	        			siteName = (String)groupMap.get("site_name");
        				if( siteName != null ) {
	        				userInfoMap.put("site_name", siteName);
	        			}
	        			else {
	        				siteName = getParentGroupSite(conn, (String)groupMap.get("principal_id"));
	        				if( siteName != null ) {
	        					groupMap.put("site_name", siteName);
		        				userInfoMap.put("site_name", siteName);
	        				}
	        				else {
	        					userInfoMap.put("site_name", "/public");
	        					System.out.println("### Cannot find group page. so use default page[ /default_page.page ]");
	        				}
	        			}
        				
        				String groupPage = (String)groupMap.get("default_page");
        				if( defaultPage == null || defaultPage.length()==0 ) {
	        				if( groupPage != null ) {
		        				userInfoMap.put("default_page", groupPage);
		        			}
        				}
	        		}
	        	}
	        	else {
	        		userInfoMap.put("site_name", "/public");
	        	}
        	}
	        
	        // defaultPage의 유무는 체크 안한다.
	        // 없는 경우에는 해당 사용자 그룹의 rootSite의 defaultPage를 사용하게 된다.
        	/*
        	if( defaultPage == null || defaultPage.length()==0 ) {
	        	if( groups.size() > 0 ) {
	        		Map groupMap = (Map)principalMap.get( "group/" + (String)groups.get(0) );
	        		if( groupMap != null ) {
        				String groupPage = (String)groupMap.get("default_page");
        				if( groupPage != null ) {
	        				userInfoMap.put("default_page", groupPage);
	        			}
	        			else {
	        				groupPage = getParentGroupPage(conn, (String)groupMap.get("principal_id"));
	        				if( groupPage != null ) {
	        					groupMap.put("default_page", groupPage);
		        				userInfoMap.put("default_page", groupPage);
	        				}
	        				else {
	        					userInfoMap.put("default_page", "/default_page.page");
	        					System.out.println("### Cannot find group page. so use default page[ /default_page.page ]");
	        				}
	        			}
	        		}
	        	}
        	}
        	*/
	        
	        if (null != rs) rs.close();
	        if (null != selectStmt) selectStmt.close();
	        
	        String groupIds = "";
        	if( groupIdList.size() > 0 ) {
        		for(Iterator it=groupIdList.iterator(); it.hasNext(); ) {
        			groupIds += (String)it.next();
        			if( it.hasNext() ) {
        				groupIds += ",";
        			}
        		}
        	}
        	else {
        		groupIds = "100"; // 그룹이 지정 안된 경우 최상위 그룹을 지정한다.
        	}
	        
        	// 그룹페이지용으로 사용될 그룹 principalId를 저장한다. 
	        String[] groupPrincipalIdArray = groupIds.split(",");
	        userInfoMap.put("groupPagePrincipalId", groupPrincipalIdArray[0]);
        	
	        List roleIdList = getAllUserRole(conn, groupIds, principal_id);
	        
	        String roleIds = "";
        	if( roleIdList.size() > 0 ) {
        		for(Iterator it=roleIdList.iterator(); it.hasNext(); ) {
        			roleIds += (String)it.next();
        			if( it.hasNext() ) {
        				roleIds += ",";
        			}
        		}
        	}
	        
        	if( roleIds == null || roleIds.length()==0 ) {
        		roleIds = "10"; // 역할이 없는 경우 최상위 역할 (공통역할)을 지정한다.
        	}
        	
        	userInfoMap.put("groupIds", groupIds);
	        userInfoMap.put("roleIds", roleIds);

        	// 사용자에게 할당된 역할정보 저장
	        pageQuery.delete(0, pageQuery.length());
	        pageQuery.append( "SELECT a.principal_id, a.short_path, a.parent_id, a.principal_name " );
	        pageQuery.append( "FROM security_principal a " );
	        pageQuery.append( "WHERE a.principal_id in (" ).append( roleIds ).append( ") " );
			//log.debug("*** query=" + pageQuery.toString() );
	        selectStmt = conn.prepareStatement( pageQuery.toString() );
			rs = selectStmt.executeQuery();
	        while( rs.next() ) {
	        	Map roleMap = new HashMap();
	        	roleMap.put("principal_id", rs.getString(1));
	        	roleMap.put("short_path", rs.getString(2));
	        	roleMap.put("principal_name", rs.getString(4));
	        	principalMap.put("role/" + rs.getString(2), roleMap);
	        	
	        	roles.add( rs.getString(2) );
	        }
	        
	        if (null != rs) rs.close();
	        if (null != selectStmt) selectStmt.close();
	        
        }
    	catch (Exception e)
        {
    		e.printStackTrace();
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	protected List getAllUserRole(Connection conn, String groudIds, String principal_id) throws Exception 
	{
		List resultValue = new ArrayList();
		List roles = new ArrayList();
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
        	if( groudIds != null && groudIds.length()>0 ) {
	    		pageQuery.append( "SELECT role_id FROM security_group_role WHERE group_id in (" ).append( groudIds ).append( ") " );
	    		pageQuery.append( "UNION " );
        	}
    		pageQuery.append( "SELECT role_id FROM security_user_role WHERE user_id=?" );
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(principal_id));
    		rs = selectStmt.executeQuery();
    		while( rs.next() ) {
    			String roleId = rs.getString(1);
    			if( roles.contains(roleId) == false ) {
    				roles.add( roleId );
    			}
    		}
    		
    		if (null != rs) rs.close();
            if (null != selectStmt) selectStmt.close();
            
            List tmpList = new ArrayList();
            tmpList.addAll( roles );
            for(Iterator it=roles.iterator(); it.hasNext(); ) {
    			String roleId = (String)it.next();
    			
    			this.getParentRole(conn, tmpList, roleId);
            }
            
            resultValue = tmpList;
        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return resultValue;
	}
	
	protected String getParentGroupTheme(Connection conn, String groupPrincipalId) throws Exception 
	{
		String theme = null;
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
    		pageQuery.append( "SELECT parent_id, theme FROM security_principal WHERE principal_id=?" );
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(groupPrincipalId));
    		rs = selectStmt.executeQuery();
    		String parentId = null;
    		if( rs.next() ) {
    			parentId = rs.getString(1);
    			theme = rs.getString(2);
    		}
    		
    		if( theme != null && theme.length()>0 ) return theme;
    		
			if( parentId != null ) {
				if (null != rs) rs.close();
	            if (null != selectStmt) selectStmt.close();
	            
	            theme = this.getParentGroupTheme(conn, parentId);
			}
        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return theme;
	}
	
	protected String getParentGroupPage(Connection conn, String groupPrincipalId) throws Exception 
	{
		String groupPage = null;
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
    		pageQuery.append( "SELECT parent_id, default_page FROM security_principal WHERE principal_id=?" );
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(groupPrincipalId));
    		rs = selectStmt.executeQuery();
    		String parentId = null;
    		if( rs.next() ) {
    			parentId = rs.getString(1);
    			groupPage = rs.getString(2);
    		}
    		
    		if( groupPage != null && groupPage.length()>0 ) return groupPage;
    		
			if( parentId != null ) {
				if (null != rs) rs.close();
	            if (null != selectStmt) selectStmt.close();
	            
	            groupPage = this.getParentGroupPage(conn, parentId);
			}
        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return groupPage;
	}
	
	protected String getParentGroupSite(Connection conn, String groupPrincipalId) throws Exception 
	{
		String siteName = null;
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
    		pageQuery.append( "SELECT parent_id, site_name FROM security_principal WHERE principal_id=?" );
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(groupPrincipalId));
    		rs = selectStmt.executeQuery();
    		String parentId = null;
    		if( rs.next() ) {
    			parentId = rs.getString(1);
    			siteName = rs.getString(2);
    		}
    		
    		if( siteName != null && siteName.length()>0 ) return siteName;
    		
			if( parentId != null ) {
				if (null != rs) rs.close();
	            if (null != selectStmt) selectStmt.close();
	            
	            siteName = this.getParentGroupSite(conn, parentId);
			}
        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return siteName;
	}
	
	protected void getParentRole(Connection conn, List roles, String roleId) throws Exception 
	{
		List resultValue = new ArrayList();
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
    		pageQuery.append( "SELECT parent_id FROM security_principal WHERE principal_id=?" );
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(roleId));
    		rs = selectStmt.executeQuery();
    		String parentId = null;
    		if( rs.next() ) {
    			parentId = rs.getString(1);
    		}
    		
			if( parentId != null && roles.contains(parentId) == false ) {
				roles.add( parentId );
				
				if (null != rs) rs.close();
	            if (null != selectStmt) selectStmt.close();
	            
	            this.getParentRole(conn, roles, parentId);
			}

        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	protected Map getPrincipalInfo(Connection conn, String principalId) throws Exception 
	{
		Map principalMap = new HashMap();
		PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
        try {
    		pageQuery.append("select principal_id, parent_id, principal_type, principal_name, short_path, theme, site_name, default_page ");
    		pageQuery.append("from security_principal a, security_user_group b where a.principal_id=b.group_id and b.user_id=? ");
    		pageQuery.append("union ");
    		pageQuery.append("select principal_id, parent_id, principal_type, principal_name, short_path, theme, site_name, default_page ");
    		pageQuery.append("from security_principal a, security_user_role b where a.principal_id=b.role_id and b.user_id=? ");
    		pageQuery.append("union ");
    		pageQuery.append("select principal_id, parent_id, principal_type, principal_name, short_path, theme, site_name, default_page ");
    		pageQuery.append("from security_principal a, security_group_role b where a.principal_id=b.role_id and b.group_id ");
    		pageQuery.append("in (select group_id from security_user_group where user_id=?) ");
    		selectStmt = conn.prepareStatement( pageQuery.toString() );
    		selectStmt.setInt(1, Integer.parseInt(principalId));
    		selectStmt.setInt(2, Integer.parseInt(principalId));
    		selectStmt.setInt(3, Integer.parseInt(principalId));
    		rs = selectStmt.executeQuery();
    		
    		String principal_id = null;
    		String parentId = null;
    		String principal_type = null;
    		String principal_name = null;
    		String short_path = null;
    		String theme = null;
    		String site_name = null;
    		String default_page = null;
    		
    		if( rs.next() ) {
    			principal_id = rs.getString(1);
    			parentId = rs.getString(2);
    			principal_type = rs.getString(3);
    			principal_name = rs.getString(4);
    			short_path = rs.getString(5);
    			theme = rs.getString(6);
    			site_name = rs.getString(7);
    			default_page = rs.getString(8);
    			
    			Map infoMap = new HashMap();
    			infoMap.put("principal_id", principal_id);
    			infoMap.put("parentId", parentId);
    			infoMap.put("principal_name", principal_name);
    			infoMap.put("short_path", short_path);
    			
    			if( theme != null && theme.length() > 0 ) {
    				infoMap.put("theme", theme);
    			}
    			if( site_name != null && site_name.length() > 0 ) {
    				infoMap.put("site_name", site_name);
    			}
    			if( default_page != null && default_page.length() > 0 ) {
    				infoMap.put("default_page", default_page);
    			}
    			
    			if( "G".equals(principal_type) == true ) {
    				principalMap.put("group/" + principal_id, infoMap);
    			}
    			else {
    				principalMap.put("role/" + principal_id, infoMap);
    			}
    		}
    		
    		if (null != rs) rs.close();
            if (null != selectStmt) selectStmt.close();
    		
        }
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
                if (null != selectStmt) selectStmt.close();
                if (null != rs) rs.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return principalMap;
	}
	
	public void changePassword(ConnectionContext connCtx, String userId, String passwd, String passwdNew) throws UserException, Exception
	{
		Connection conn = null;
		PreparedStatement updateStmt = null;
		StringBuffer pageQuery = new StringBuffer();
        
    	try {
    	    authenticate(connCtx, userId, passwd);
    	    
    		pageQuery.append( "UPDATE SECURITY_CREDENTIAL SET COLUMN_VALUE=?, UPDATE_REQUIRED=0, MODIFIED_DATE=? " );
    		pageQuery.append( "WHERE PRINCIPAL_ID=( " );
    		pageQuery.append( "	SELECT a.PRINCIPAL_ID " );
    		pageQuery.append( "	FROM SECURITY_CREDENTIAL a, SECURITY_PRINCIPAL b " );
    		pageQuery.append( "	WHERE a.PRINCIPAL_ID=b.PRINCIPAL_ID AND b.PRINCIPAL_TYPE='U' AND b.SHORT_PATH=? " );
    		pageQuery.append( ") " );
    	    
    	    conn = connCtx.getConnection();
    	    updateStmt = conn.prepareStatement( pageQuery.toString() );
    		updateStmt.setString(1, passwdNew);
    		updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()) );
    		updateStmt.setString(3, userId);
    		
    		updateStmt.executeUpdate();
            
    	}
    	catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
            	if (null != updateStmt) updateStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public void log(ConnectionContext connCtx, Map userInfoMap, int status) throws Exception
    {
    	Connection conn = null;
		PreparedStatement updateStmt = null;
		PreparedStatement updateStmt2 = null;
    	String updateQuery = null;
    	String updateQuery2 = null;
        
    	try {
    		updateQuery = "INSERT INTO USER_STATISTICS (IPADDRESS,PRINCIPAL_ID,USER_ID,USER_NAME,ORG_CD,ORG_NAME,TIME_STAMP,STATUS,ELAPSED_TIME,ACCESS_BROWSER) VALUES(?,?,?,?,?,?,?,?,?,?)";
    		//updateQuery = "INSERT INTO USER_STATISTICS (IPADDRESS,PRINCIPAL_ID,USER_ID,USER_NAME,ORG_CD,ORG_NAME,TIME_STAMP,STATUS,ELAPSED_TIME,ACCESS_BROWSER,EXTRA_INFO01,EXTRA_INFO02,EXTRA_INFO03,EXTRA_INFO04,EXTRA_INFO05) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    		updateQuery2 = "UPDATE USERPASS SET LAST_IP=?, LAST_LOGON=?, LANG_KND=? WHERE USER_ID=?";
    	    conn = connCtx.getConnection();
    	    
    	    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String orgCd = null;
            String orgName = null;
            List groups = (List)userInfoMap.get("groups");
            if( groups != null && groups.size()>0 ) {
	            Map principalMap = (Map)userInfoMap.get("principalMap");
	            if( principalMap != null ) {
		            Map groupMap = (Map)principalMap.get("group/" + (String)groups.get(0));
		            orgCd = (String)groupMap.get("short_path");
		            orgName = (String)groupMap.get("principal_name");
	            }
	            else {
	            	orgCd = (String)userInfoMap.get("org_cd");
	            	orgName = (String)userInfoMap.get("org_name");
	            }
            }
            else {
            	orgCd = (String)userInfoMap.get("org_cd");
            	orgName = (String)userInfoMap.get("org_name");
            }
        	
    	    updateStmt = conn.prepareStatement( updateQuery );
	    	updateStmt.setString(1, (String)userInfoMap.get("remote_address"));
	    	updateStmt.setInt(2, Integer.parseInt((String)userInfoMap.get("principal_id")));
	    	updateStmt.setString(3, (String)userInfoMap.get("user_id"));
	    	updateStmt.setString(4, (String)userInfoMap.get("principal_name"));
	    	updateStmt.setString(5, orgCd);
	    	updateStmt.setString(6, orgName);
	    	updateStmt.setTimestamp(7, timestamp);
	    	updateStmt.setInt(8, status);
	    	updateStmt.setLong(9, 0);
	    	updateStmt.setString(10, (String)userInfoMap.get("user-agent"));
	    	/*
	    	updateStmt.setString(11, (String)userInfoMap.get("extraUserInfo01"));
	    	updateStmt.setString(12, (String)userInfoMap.get("extraUserInfo02"));
	    	updateStmt.setString(13, (String)userInfoMap.get("extraUserInfo03"));
	    	updateStmt.setString(14, (String)userInfoMap.get("extraUserInfo04"));
	    	updateStmt.setString(15, (String)userInfoMap.get("extraUserInfo05"));
	    	*/
	    	updateStmt.executeUpdate();
    	    
    	    updateStmt2 = conn.prepareStatement( updateQuery2 );
    		updateStmt2.setString(1, (String)userInfoMap.get("remote_address"));
    		updateStmt2.setTimestamp(2, timestamp);
    		updateStmt2.setString(3, (String)userInfoMap.get("lang_knd"));
    		updateStmt2.setString(4, (String)userInfoMap.get("user_id"));
    		updateStmt2.executeUpdate();
    	}
    	catch (Exception e)
        {
            throw e;
        } 
        finally
        {
            try
            {
            	if (null != updateStmt) updateStmt.close();
            	if (null != updateStmt2) updateStmt2.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * 회원 가입에 필요한 메소드(insert, isOverlap, getUserList)
	 * 작성자: 김성욱
	 * 최초작성일: 2010년 12월 1일
	 * 최종수정일: 2010년 12월 6일
	 */
	
	protected int getPrincipalId(ConnectionContext connCtx, String userId) throws UserException
	{
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		StringBuffer pageQuery = new StringBuffer();
        
    	try {
    		pageQuery.append( "SELECT PRINCIPAL_ID  " );
    		pageQuery.append( "FROM SECURITY_PRINCIPAL " );
    		pageQuery.append( "WHERE SHORT_PATH=? " );
    		
    	    conn = connCtx.getConnection();
    	    
    	    selectStmt = conn.prepareStatement( pageQuery.toString() );
    	    selectStmt.setString(1, userId);
    	    rs = selectStmt.executeQuery();
            if( rs.next() ) {
            	int principal_id = rs.getInt(1);
            	return principal_id;
            }
            else {
            	throw new UserException( "pt.ev.user.login.InvalidUserIdOrInfo" ); // 사용자 없음
            }
            
    	}
    	catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
                if (null != rs) rs.close();
            	if (null != selectStmt) selectStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
	}
	
	public void insert(ConnectionContext connCtx, UserVo user, String encPasswd) throws Exception {
		Connection conn = null;
		
		PreparedStatement principalUpdateStmt = null;
		StringBuffer principalUpdateSql = new StringBuffer();
		
		PreparedStatement credentialUpdateStmt = null;
		StringBuffer credentialUpdateSql = new StringBuffer();
		
		PreparedStatement userpassUpdateStmt = null;
		StringBuffer userpassUpdateSql = new StringBuffer();
		
		PreparedStatement groupUpdateStmt = null;
		StringBuffer groupUpdateSql = new StringBuffer();
		
		PreparedStatement roleUpdateStmt = null;
		StringBuffer roleUpdateSql = new StringBuffer();
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
    	try {
    		conn = connCtx.getConnection();    	    
    		
    		/* SECURITY_PRINCIPAL TABLE 삽입*/
    		principalUpdateSql.append( "INSERT INTO SECURITY_PRINCIPAL(PRINCIPAL_ID, PRINCIPAL_TYPE, CLASSNAME, IS_MAPPING_ONLY, IS_ENABLED, " );
    		principalUpdateSql.append( "SHORT_PATH, FULL_PATH, PRINCIPAL_NAME, CREATION_DATE, MODIFIED_DATE) " );
    		principalUpdateSql.append( "VALUES(" + user.getPrincipalKey() + ",'U', '" + this.getClass().getName() + "', 0, 1, ");
    		principalUpdateSql.append( "'" + user.getUser_id() + "','/user/" + user.getUser_id() +"', '" + user.getUser_name() + "','" + time + "', '" + time + "') ");
    		
    	    principalUpdateStmt = conn.prepareStatement( principalUpdateSql.toString() );
    		principalUpdateStmt.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(principalUpdateSql.toString());
        } finally {
            try {
            	if (null != principalUpdateStmt) principalUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	try {
    		credentialUpdateSql.append( "INSERT INTO SECURITY_CREDENTIAL(CREDENTIAL_ID, PRINCIPAL_ID, COLUMN_VALUE, TYPE, CLASSNAME, UPDATE_REQUIRED, ");
    		credentialUpdateSql.append( "IS_ENCODED, IS_ENABLED, AUTH_FAILURES, IS_EXPIRED, CREATION_DATE, MODIFIED_DATE) " );
    		credentialUpdateSql.append( "VALUES(" + user.getCredentialKey() + ", " + user.getPrincipalKey() + ", '" + encPasswd + "', 0,'" + this.getClass().getName() + "', 0, ");
    		credentialUpdateSql.append( "1, 1, 0, 0, '" + time + "', '" + time + "') ");
    		System.out.println(credentialUpdateSql.toString());
    		credentialUpdateStmt = conn.prepareStatement( credentialUpdateSql.toString() );
			credentialUpdateStmt.executeUpdate();
			
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(credentialUpdateSql.toString());
        } finally {
            try {
            	if (null != credentialUpdateStmt) credentialUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	
    	try{
			/* USERPASS TABLE 삽입*/
			userpassUpdateSql.append( "INSERT INTO USERPASS(USER_ID, REG_NO, NM_KOR, MOBILE_TEL, EMAIL_ADDR, ");
			userpassUpdateSql.append( "REG_DATIM, LANG_KND, HOME_ZIP, HOME_ADDR1, HOME_ADDR2) ");
    		userpassUpdateSql.append( "VALUES('" + user.getUser_id() + "','" + user.getRegNo() + "','" + user.getUser_name() + "','" + user.getUser_hp() + "','" + user.getUser_email() + "', ");
    		userpassUpdateSql.append( "'" + time + "','" + user.getLangKnd() + "','" + user.getHomeZip() + "','" + user.getHomeAddr1() + "'," + user.getHomeAddr2() + "') ");

    		userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
			userpassUpdateStmt.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(userpassUpdateSql.toString());
        } finally {
            try {
            	if (null != userpassUpdateStmt) userpassUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }	
		
    	try{
			int	group_id = getPrincipalId(connCtx, Talk.getConfiguration().getString("default.user.group"));
			
			groupUpdateSql.append( "INSERT INTO SECURITY_USER_GROUP(USER_ID, GROUP_ID, SORT_ORDER, MILEAGE_TAG)  ");
			groupUpdateSql.append( "VALUES(" + user.getPrincipalKey() + ", " + group_id + ", 0, 'N')");

    		groupUpdateStmt = conn.prepareStatement( groupUpdateSql.toString() );
    		groupUpdateStmt.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(groupUpdateSql.toString());
        } finally {
            try {
            	if (null != groupUpdateStmt) groupUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	try{
			/* SECURITY_USER_ROLE TABLE 삽입*/
    		int	role_id = getPrincipalId(connCtx, Talk.getConfiguration().getString("default.user.role"));
    		
    		roleUpdateSql.append( "INSERT INTO SECURITY_USER_ROLE(USER_ID, ROLE_ID, MILEAGE_TAG)  ");
    		roleUpdateSql.append( "VALUES(" + user.getPrincipalKey() + ", " + role_id + ", 'N')");

			roleUpdateStmt = conn.prepareStatement( roleUpdateSql.toString() );
			roleUpdateStmt.executeUpdate();
            
			System.out.println("Insert Success");
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(roleUpdateSql.toString());
        } finally {
            try {
            	if (null != roleUpdateStmt) roleUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 *  아이디 중복 체크 
	 **/
	public boolean isOverlap(ConnectionContext connCtx, String userId) throws Exception{
    	Connection conn = null;
    	PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
        
		pageQuery.append( "SELECT user_id " );
		pageQuery.append( "FROM userpass " );
		pageQuery.append( "WHERE user_id=?" );
		
		conn = connCtx.getConnection();
		selectStmt = conn.prepareStatement( pageQuery.toString() );
		selectStmt.setString(1, userId);
        rs = selectStmt.executeQuery();
        if( rs.next() ) {
        	return true;
        }
        if (null != rs) rs.close();
        if (null != selectStmt) selectStmt.close();
    	return false;
	}
	
	/**
	 * 중복 가입 여부를 판단하기 위한 함수
	 */
	public List getUserList(ConnectionContext connCtx,
			String name, String regNo) throws UserException{
		List userList = new ArrayList();
		boolean isInValid = false;
		Connection conn = null;
    	PreparedStatement selectStmt = null;
        ResultSet rs = null;
        StringBuffer pageQuery = new StringBuffer();
		
    	pageQuery.append( "SELECT user_id, nm_kor, reg_No, reg_datim " );
		pageQuery.append( "FROM userpass " );
		pageQuery.append( "WHERE reg_No=?" );
		try{
			conn = connCtx.getConnection();
			selectStmt = conn.prepareStatement( pageQuery.toString() );
			selectStmt.setString(1, regNo);
	        rs = selectStmt.executeQuery();
	        while( rs.next() ) {
	        	UserVo user = new UserVo();
	        	user.setUser_id(rs.getString("user_id"));
	        	user.setUser_name(rs.getString("nm_kor"));
	        	user.setRegNo(rs.getString("reg_no"));
	        	user.setRegDatim(rs.getTimestamp("reg_datim"));
	        	userList.add(user);
	        }
	        if (null != rs) rs.close();
	        if (null != selectStmt) selectStmt.close();
			
	        for(int i = 0 ; i < userList.size() ; i++){
				if(!((UserVo)userList.get(i)).getUser_name().equals(name)){
					isInValid = true;
					log.debug("*** Inputted Name: " +  name + " & Reg_no: " + regNo + " is not matched.");
					break;
				}
				log.debug("*** Searched Name: " +  ((UserVo)userList.get(i)).getUser_name() + " & Reg_no: " + ((UserVo)userList.get(i)).getRegNo() + " & ID: " + ((UserVo)userList.get(i)).getUser_id());
			}
	        if(isInValid){
	        	throw new UserException( "pt.ev.user.login.FailToConfirmForName" ); // 실명인증 실패
	        }
	        return userList;
		}
        catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
                if (null != rs) rs.close();
            	if (null != selectStmt) selectStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public void update(ConnectionContext connCtx, UserVo user)
			throws Exception {
		Connection conn = null;
		
		PreparedStatement principalUpdateStmt = null;
		StringBuffer principalUpdateQuery = new StringBuffer();
		
		PreparedStatement userpassUpdateStmt = null;
		StringBuffer userpassUpdateQuery = new StringBuffer();
		
    	try {
    		conn = connCtx.getConnection();    	    
    		Timestamp time = new Timestamp(System.currentTimeMillis());
    		
    		/* SECURITY_PRINCIPAL TABLE 수정*/
    		principalUpdateQuery.append( "UPDATE SECURITY_PRINCIPAL " );
    		principalUpdateQuery.append( "SET PRINCIPAL_NAME=?, MODIFIED_DATE=? " );
    		principalUpdateQuery.append( "WHERE SHORT_PATH=?");
    		
    	    principalUpdateStmt = conn.prepareStatement( principalUpdateQuery.toString() );
    		principalUpdateStmt.setString(1, user.getUser_name());
    		principalUpdateStmt.setTimestamp(2, time);
    		principalUpdateStmt.setString(3, user.getUser_id());
    		principalUpdateStmt.executeUpdate();
    		
			/* USERPASS TABLE 수정*/
			userpassUpdateQuery.append( "UPDATE USERPASS ");
			userpassUpdateQuery.append( "SET MOBILE_TEL=?, EMAIL_ADDR=?, HOME_ZIP=?, HOME_ADDR1=?, HOME_ADDR2=?, UPD_DATIM=? " );
    		userpassUpdateQuery.append( "WHERE USER_ID=?");

    		userpassUpdateStmt = conn.prepareStatement( userpassUpdateQuery.toString() );
			userpassUpdateStmt.setString(1, user.getUser_hp());
			userpassUpdateStmt.setString(2, user.getUser_email());
			userpassUpdateStmt.setString(3, user.getHomeZip());
			System.out.println("user.getHomeZip()=" + user.getHomeZip());
			userpassUpdateStmt.setString(4, user.getHomeAddr1());
			System.out.println("user.getHomeAddr1()=" + user.getHomeAddr1());
			userpassUpdateStmt.setString(5, user.getHomeAddr2());
			System.out.println("user.getHomeAddr2()=" + user.getHomeAddr2());
			userpassUpdateStmt.setTimestamp(6, time);
			userpassUpdateStmt.setString(7, user.getUser_id());
			userpassUpdateStmt.executeUpdate();
    	}
    	catch (UserException e)
        {
    		System.out.println("******** UserException!! *********  " + e);
            throw e;
        } 
    	catch (Exception e)
        {
    		System.out.println("******** Exception!! *********  " + e);
            throw new UserException(e);
        } 
        finally
        {
            try
            {
            	if (null != principalUpdateStmt) principalUpdateStmt.close();
            } catch (Exception e)
            {
            	System.out.println("******** Finally Exception!! *********  " + e);
                e.printStackTrace();
            }
        }
	}
	
	public String authenticate(ConnectionContext connCtx, String userId, String userName, String regNo) throws UserException
	{
		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		StringBuffer pageQuery = new StringBuffer();
		log.debug("*** Inputted Name: " +  userName + " & Reg_no: " + regNo + " ID: " + userId);
    	try {
    		pageQuery.append( "SELECT b.IS_ENABLED, c.EMAIL_ADDR " );
    		pageQuery.append( "FROM SECURITY_CREDENTIAL a, SECURITY_PRINCIPAL b, USERPASS c " );
    		pageQuery.append( "WHERE a.PRINCIPAL_ID=b.PRINCIPAL_ID AND b.PRINCIPAL_TYPE='U' AND b.SHORT_PATH=c.USER_ID AND c.USER_ID=? AND c.NM_KOR=? AND c.REG_NO=?  " );
    		
    	    conn = connCtx.getConnection();
    	    
    	    selectStmt = conn.prepareStatement( pageQuery.toString() );
    	    selectStmt.setString(1, userId);
    	    selectStmt.setString(2, userName);
    	    selectStmt.setString(3, regNo);
    	    rs = selectStmt.executeQuery();
            if( rs.next() ) {
            	boolean isEnabled = rs.getBoolean(1);
            	
            	if( isEnabled == false ) {
            		throw new UserException( "pt.ev.user.login.DisabledAccount" ); // 비활성화 계정
            	}
            	return rs.getString(2);
            }
            else {
            	throw new UserException( "pt.ev.user.login.InvalidUserIdOrInfo" ); // 사용자 없음
            }
            
    	}
    	catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
                if (null != rs) rs.close();
            	if (null != selectStmt) selectStmt.close();
            	if (null != updateStmt) updateStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public Map reissuePassword(ConnectionContext connCtx, String userId, String temp_pw, String userName, String regNo) throws UserException, Exception
	{
		Connection conn = null;
		PreparedStatement updateStmt = null;
		StringBuffer updateQuery = new StringBuffer();
    	try {
    		
    		authenticate(connCtx, userId, userName, regNo);
    		
    		updateQuery.append( "UPDATE SECURITY_CREDENTIAL SET COLUMN_VALUE=?, UPDATE_REQUIRED=1, MODIFIED_DATE=?, AUTH_FAILURES=0 " );
    		updateQuery.append( "WHERE PRINCIPAL_ID=( " );
    		updateQuery.append( "SELECT a.PRINCIPAL_ID " );
    		updateQuery.append( "FROM SECURITY_CREDENTIAL a, SECURITY_PRINCIPAL b, USERPASS c " );
    		updateQuery.append( "WHERE a.PRINCIPAL_ID=b.PRINCIPAL_ID AND b.PRINCIPAL_TYPE='U' AND b.SHORT_PATH=c.USER_ID AND c.USER_ID=? AND c.NM_KOR=? AND c.REG_NO=?" );
    		updateQuery.append( ") " );
    	    
    	    conn = connCtx.getConnection();
    	    updateStmt = conn.prepareStatement( updateQuery.toString() );
    		updateStmt.setString(1, temp_pw);
    		updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()) );
    		updateStmt.setString(3, userId);
    		updateStmt.setString(4, userName);
    		updateStmt.setString(5, regNo);
    		updateStmt.executeUpdate();
    		
    		return getUserInfoMap(connCtx, userId);
            
    	}
    	catch (UserException e)
        {
            throw e;
        } 
    	catch (Exception e)
        {
            throw new UserException(e);
        } 
        finally
        {
            try
            {
            	if (null != updateStmt) updateStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public Collection getZipCodes(ConnectionContext connCtx, String dong, String langKnd) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		StringBuffer selectQuery = new StringBuffer();
		Collection zipCodes = new ArrayList();
    	try {
    		selectQuery.append( "SELECT ZIP_CD, SEQ, SIDO, GUGUN, DONG, RI, BLDG, ST_BUNGI, ED_BUNGI " );
    		selectQuery.append( "FROM ZIPCODE " );
    		selectQuery.append( "WHERE DONG LIKE ? and LANG_KND=? ");
    		selectQuery.append( "ORDER BY seq ASC");
    	    
    	    conn = connCtx.getConnection();
    	    selectStmt = conn.prepareStatement( selectQuery.toString() );
    	    System.out.println("****    Search ZipCode!! -> dong=" + dong + ", langKnd=" + langKnd);
    	    selectStmt.setString(1, dong + "%");
    	    selectStmt.setString(2, langKnd);
    		selectStmt.executeUpdate();

    		rs = selectStmt.executeQuery();
    		while( rs.next() ) {
    			ZipCodeVo zipCode = new ZipCodeVo();
    			zipCode.setZipCode(rs.getString("ZIP_CD"));
    			zipCode.setSeq(rs.getString("SEQ"));
    			zipCode.setSido(rs.getString("SIDO"));
    			zipCode.setGugun(rs.getString("GUGUN"));
    			zipCode.setDong(rs.getString("DONG"));
    			zipCode.setRi(rs.getString("RI"));
    			zipCode.setBldg(rs.getString("BLDG"));
    			zipCode.setSt_bunji(rs.getString("ST_BUNGI"));
    			zipCode.setEd_bunji(rs.getString("ED_BUNGI"));
    			zipCodes.add(zipCode);
    		}
    		if (null != rs) rs.close();
    		if (null != selectStmt) selectStmt.close();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
        } 
        finally
        {
            try
            {
            	if (null != selectStmt) selectStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
		return zipCodes;
	}
	
	public Collection getJoinGroups(ConnectionContext connCtx, String user_id) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		StringBuffer selectQuery = new StringBuffer();
		Collection groupList = new ArrayList();
    	try {
    		log.debug("getGroupList joined user: " + user_id);
    		selectQuery.append( " SELECT A.GROUP_ID, B.PRINCIPAL_NAME " );
    		selectQuery.append( " FROM SECURITY_USER_GROUP A " );
    		selectQuery.append( " JOIN SECURITY_PRINCIPAL B ON A.GROUP_ID = B.PRINCIPAL_ID " );
    		selectQuery.append( " WHERE A.USER_ID=? ");
    		selectQuery.append( " ORDER BY B.PRINCIPAL_NAME ASC ");
    	    
    	    conn = connCtx.getConnection();
    	    selectStmt = conn.prepareStatement( selectQuery.toString() );
    	    selectStmt.setInt(1, getPrincipalId(connCtx, user_id));
    		selectStmt.executeUpdate();

    		rs = selectStmt.executeQuery();
    		while( rs.next() ) {
    			UserVo group = new UserVo();
    			group.setPrincipalId(rs.getString(1));
    			group.setUser_name(rs.getString(2));
    			groupList.add(group);
    		}
    		if (null != rs) rs.close();
    		if (null != selectStmt) selectStmt.close();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
        } 
        finally
        {
            try
            {
            	if (null != selectStmt) selectStmt.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
		return groupList;
	}
	
	public void addUserInfo(ConnectionContext connCtx, Map userInfoMap) throws Exception {
		Connection conn = null;
		
		String userId = (String)userInfoMap.get("user_id");
		String encPasswd = (String)userInfoMap.get("encPasswd");
		int principalId = Integer.parseInt( (String)userInfoMap.get("principal_id") );
		int credentialId = Integer.parseInt( (String)userInfoMap.get("credential_id") );
		String imei = (String)userInfoMap.get("imei");
		String name = (String)userInfoMap.get("name");
		
		PreparedStatement principalUpdateStmt = null;
		StringBuffer principalUpdateSql = new StringBuffer();
		
		PreparedStatement credentialUpdateStmt = null;
		StringBuffer credentialUpdateSql = new StringBuffer();
		
		PreparedStatement userpassUpdateStmt = null;
		StringBuffer userpassUpdateSql = new StringBuffer();
		
		PreparedStatement groupUpdateStmt = null;
		StringBuffer groupUpdateSql = new StringBuffer();
		
		PreparedStatement roleUpdateStmt = null;
		StringBuffer roleUpdateSql = new StringBuffer();
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
    	try {
    		conn = connCtx.getConnection();    	    
    		
    		/* SECURITY_PRINCIPAL TABLE 삽입*/
    		principalUpdateSql.append( "INSERT INTO SECURITY_PRINCIPAL (PRINCIPAL_ID,PRINCIPAL_TYPE,CLASSNAME,IS_MAPPING_ONLY,IS_ENABLED,SHORT_PATH,FULL_PATH,PRINCIPAL_NAME,CREATION_DATE,MODIFIED_DATE) " );
    		principalUpdateSql.append( "VALUES(?,'U','org.tok.view.security.InternalUserPrincipalImpl',?,?,?,?,'',?,?)" );
    	    principalUpdateStmt = conn.prepareStatement( principalUpdateSql.toString() );
    	    principalUpdateStmt.setInt(1, principalId);
    	    principalUpdateStmt.setInt(2, 0);
    	    principalUpdateStmt.setInt(3, 1);
    	    principalUpdateStmt.setString(4, userId);
    	    principalUpdateStmt.setString(5, "/user/" + userId);
    	    //principalUpdateStmt.setString(6, name);
    	    principalUpdateStmt.setTimestamp(6, time);
    	    principalUpdateStmt.setTimestamp(7, time);
    		principalUpdateStmt.executeUpdate();

    		/* SECURITY_CREDENTIAL TABLE 삽입*/
    		credentialUpdateSql.append( "INSERT INTO SECURITY_CREDENTIAL(CREDENTIAL_ID, PRINCIPAL_ID, COLUMN_VALUE, \"TYPE\", CLASSNAME, UPDATE_REQUIRED, ");
    		credentialUpdateSql.append( "IS_ENCODED, IS_ENABLED, AUTH_FAILURES, IS_EXPIRED, CREATION_DATE, MODIFIED_DATE) " );
    		credentialUpdateSql.append( "VALUES(?,?,?,?,'org.tok.view.security.spi.impl.DefaultPasswordCredentialImpl',?,?,?,?,?,?,?)" );
    		credentialUpdateStmt = conn.prepareStatement( credentialUpdateSql.toString() );
    		credentialUpdateStmt.setInt(1, credentialId);
    		credentialUpdateStmt.setInt(2, principalId);
    		credentialUpdateStmt.setString(3, (encPasswd != null) ? encPasswd : "TBD");
    		credentialUpdateStmt.setInt(4, 0);
    		credentialUpdateStmt.setInt(5, 0);
    		credentialUpdateStmt.setInt(6, 1);
    		credentialUpdateStmt.setInt(7, 1);
    		credentialUpdateStmt.setInt(8, 0);
    		credentialUpdateStmt.setInt(9, 0);
    		credentialUpdateStmt.setTimestamp(10, time);
    		credentialUpdateStmt.setTimestamp(11, time);
			credentialUpdateStmt.executeUpdate();
			
			/* USERPASS TABLE 삽입*/
    		userpassUpdateSql.append( "INSERT INTO USERPASS(USER_ID, MOBILE_TEL, NM_KOR, NM_NIC, REG_DATIM, LANG_KND, USER_INFO01, USER_INFO10) ");
    		userpassUpdateSql.append( "VALUES(?,?,'','',?,'ko',1,?)" );
    		userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
    		userpassUpdateStmt.setString(1, userId);
    		userpassUpdateStmt.setString(2, userId);
    		//userpassUpdateStmt.setString(3, name);
    		userpassUpdateStmt.setTimestamp(3, time);
    		userpassUpdateStmt.setString(4, imei);
			userpassUpdateStmt.executeUpdate();

			/* SECURITY_USER_GROUP TABLE 삽입*/
			String groupId = Talk.getConfiguration().getString("default.user.group");
			int	group_id = getPrincipalId(connCtx, groupId);
			groupUpdateSql.append( "INSERT INTO SECURITY_USER_GROUP(USER_ID, GROUP_ID, SORT_ORDER, MILEAGE_TAG)  ");
			groupUpdateSql.append( "VALUES(?,?,0,'N')" );
    		groupUpdateStmt = conn.prepareStatement( groupUpdateSql.toString() );
    		groupUpdateStmt.setInt(1, principalId);
    		groupUpdateStmt.setInt(2, group_id);
    		groupUpdateStmt.executeUpdate();
    	
			/* SECURITY_USER_ROLE TABLE 삽입*/
    		String roleId = Talk.getConfiguration().getString("default.user.role");
    		int	role_id = getPrincipalId(connCtx, roleId);
    		roleUpdateSql.append( "INSERT INTO SECURITY_USER_ROLE(USER_ID, ROLE_ID, MILEAGE_TAG)  ");
    		roleUpdateSql.append( "VALUES(?,?,'N')" );
    		roleUpdateStmt = conn.prepareStatement( roleUpdateSql.toString() );
    		roleUpdateStmt.setInt(1, principalId);
    		roleUpdateStmt.setInt(2, role_id);
			roleUpdateStmt.executeUpdate();
    	} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
    		throw e;
        } 
    	finally 
    	{
            try {
            	if (null != roleUpdateStmt) roleUpdateStmt.close();
            	if (null != groupUpdateStmt) groupUpdateStmt.close();
            	if (null != userpassUpdateStmt) userpassUpdateStmt.close();
            	if (null != credentialUpdateStmt) credentialUpdateStmt.close();
            	if (null != principalUpdateStmt) principalUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public void updateUserInfo(ConnectionContext connCtx, Map userInfoMap) throws Exception {
		Connection conn = null;
		
		String userId = (String)userInfoMap.get("user_id");
		String encPasswd = (String)userInfoMap.get("encPasswd");
		String nm_kor = (String)userInfoMap.get("nm_kor");
		String sex_flag = (String)userInfoMap.get("sex_flag");
		String birth_ymd = (String)userInfoMap.get("birth_ymd");
		String home_addr1 = (String)userInfoMap.get("home_addr1");
		String home_addr2 = (String)userInfoMap.get("home_addr2");
		String intro = (String)userInfoMap.get("intro");
		String open_flag = (String)userInfoMap.get("open_flag");
		
		String authrity = (String)userInfoMap.get("authrity");
		
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		
		PreparedStatement principalUpdateStmt = null;
		StringBuffer principalUpdateSql = new StringBuffer();
		
		PreparedStatement credentialUpdateStmt = null;
		StringBuffer credentialUpdateSql = new StringBuffer();
		
		PreparedStatement userpassUpdateStmt = null;
		StringBuffer userpassUpdateSql = new StringBuffer();
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
    	try {
    		conn = connCtx.getConnection();    	    
    		
    		/* SECURITY_CREDENTIAL TABLE 업데이트*/
    		if( encPasswd != null && encPasswd.length() > 0) {
	    		credentialUpdateSql.append( "UPDATE SECURITY_CREDENTIAL SET COLUMN_VALUE=?, MODIFIED_DATE=? WHERE PRINCIPAL_ID=" );
	    		credentialUpdateSql.append( " (SELECT principal_id FROM security_principal WHERE principal_type='U' AND short_path=?)" );
	    		credentialUpdateStmt = conn.prepareStatement( credentialUpdateSql.toString() );
	    		credentialUpdateStmt.setString(1, encPasswd);
	    		credentialUpdateStmt.setTimestamp(2, time);
	    		credentialUpdateStmt.setString(3, userId);
				credentialUpdateStmt.executeUpdate();
    		}
    		
    		if( nm_kor != null && nm_kor.length() > 0) {
    			principalUpdateStmt = conn.prepareStatement( "UPDATE security_principal SET principal_name=? WHERE short_path=?" );
    			principalUpdateStmt.setString(1, nm_kor);
    			principalUpdateStmt.setString(2, userId);
    			principalUpdateStmt.executeUpdate();
    			if (null != principalUpdateStmt) principalUpdateStmt.close();
    		}
    		
			/* USERPASS TABLE 업데이트*/
    		//System.out.println("############ birth_ymd.length()=" + birth_ymd.length());
    		if( birth_ymd != null && birth_ymd.length() == 10 ) {
        		userpassUpdateSql.append( "UPDATE USERPASS SET nm_kor=?, nm_nic=?, sex_flag=?, birth_ymd=to_date(?, 'yyyy-mm-dd'), home_addr1=?, home_addr2=?, intro=?, open_flag=? WHERE user_id=? ");
        		userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
        		userpassUpdateStmt.setString(1, nm_kor);
        		userpassUpdateStmt.setString(2, nm_kor);
        		userpassUpdateStmt.setString(3, sex_flag);
        		userpassUpdateStmt.setString(4, birth_ymd);
        		userpassUpdateStmt.setString(5, home_addr1);
        		userpassUpdateStmt.setString(6, home_addr2);
        		userpassUpdateStmt.setString(7, intro);
        		userpassUpdateStmt.setString(8, open_flag);
        		userpassUpdateStmt.setString(9, userId);
    		}
    		else {
    			userpassUpdateSql.append( "UPDATE USERPASS SET nm_kor=?, nm_nic=?, sex_flag=?, home_addr1=?, home_addr2=?, intro=?, open_flag=? WHERE user_id=? ");
    			userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
        		userpassUpdateStmt.setString(1, nm_kor);
        		userpassUpdateStmt.setString(2, nm_kor);
        		userpassUpdateStmt.setString(3, sex_flag);
        		userpassUpdateStmt.setString(4, home_addr1);
        		userpassUpdateStmt.setString(5, home_addr2);
        		userpassUpdateStmt.setString(6, intro);
        		userpassUpdateStmt.setString(7, open_flag);
        		userpassUpdateStmt.setString(8, userId);
    		}
    		
			userpassUpdateStmt.executeUpdate();
			
			if (null != userpassUpdateStmt) userpassUpdateStmt.close();

    		/* SECURITY_USER_ROLE TABLE 업데이트*/
    		if( authrity != null && authrity.length() > 0 ) {
    			principalUpdateSql.append("DELETE FROM SECURITY_USER_ROLE WHERE ");
    			principalUpdateSql.append(" user_id=(SELECT principal_id FROM security_principal WHERE principal_type='U' AND short_path=?) ");
    			//principalUpdateSql.append(" role_id=(SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=?) ");
    			//principalUpdateSql.append(" role_id=(SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=(SELECT grade_cd FROM userpass WHERE user_id=?)) ");
    			principalUpdateStmt = conn.prepareStatement( principalUpdateSql.toString() );
    			principalUpdateStmt.setString(1, userId);
    			//principalUpdateStmt.setString(2, userId);
    			principalUpdateStmt.executeUpdate();
    			if (null != principalUpdateStmt) principalUpdateStmt.close();
    			
    			userpassUpdateStmt = conn.prepareStatement("UPDATE USERPASS SET grade_cd=? WHERE user_id=?");
	    		userpassUpdateStmt.setString(1, authrity);
	    		userpassUpdateStmt.setString(2, userId);
	    		userpassUpdateStmt.executeUpdate();
	    		
	    		selectStmt = conn.prepareStatement("SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=?");
	    		selectStmt.setString(1, authrity);
	    		rs = selectStmt.executeQuery();
	    		if( rs.next() == false) {
	    			throw new Exception("요청한 역할[" + authrity + "] 은 현재 등록 되어 있지 않습니다. 등록하여 주시기 바랍니다.");
	    		}
	    		
	    		principalUpdateSql.delete(0, principalUpdateSql.length());
	    		principalUpdateSql.append("INSERT INTO SECURITY_USER_ROLE (user_id, role_id) VALUES(");
	    		principalUpdateSql.append("(SELECT principal_id FROM security_principal WHERE principal_type='U' AND short_path=?), ");
	    		principalUpdateSql.append("(SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=?) ");
	    		principalUpdateSql.append(")");
	    		principalUpdateStmt = conn.prepareStatement( principalUpdateSql.toString() );
    			principalUpdateStmt.setString(1, userId);
    			principalUpdateStmt.setString(2, authrity);
    			principalUpdateStmt.executeUpdate();
    		}
			
    	} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
    		throw e;
        } 
    	finally 
    	{
            try {
            	if (null != rs) rs.close();
            	if (null != selectStmt) selectStmt.close();
            	if (null != userpassUpdateStmt) userpassUpdateStmt.close();
            	if (null != credentialUpdateStmt) credentialUpdateStmt.close();
            	if (null != principalUpdateStmt) principalUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public void updateUserExtraInfo(ConnectionContext connCtx, Map paramMap) throws Exception {
		Connection conn = null;
		
		String userId = (String)paramMap.get("user_id");
		String imei = (String)paramMap.get("imei");
		
		PreparedStatement userpassUpdateStmt = null;
		StringBuffer userpassUpdateSql = new StringBuffer();
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
    	try {
    		conn = connCtx.getConnection();    	    
    		
			/* USERPASS TABLE 업데이트*/
    		if( imei != null && imei.length()>0 ) {
				userpassUpdateSql.append( "UPDATE USERPASS SET USER_INFO10=?, UPD_DATIM=? WHERE USER_ID=? ");
	    		userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
	    		userpassUpdateStmt.setString(1, imei);
	    		userpassUpdateStmt.setTimestamp(2, time);
	    		userpassUpdateStmt.setString(3, userId);
				userpassUpdateStmt.executeUpdate();
    		}
    	} 
    	catch (Exception e) 
    	{
    		//e.printStackTrace();
    		throw e;
        } 
    	finally 
    	{
            try {
            	if (null != userpassUpdateStmt) userpassUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	private boolean isAllDigits(String argvalue) 
	{
		String validChars = "0123456789";
		int startFrom = 0;
		if (argvalue.substring(0, 2) == "0x") {
		   validChars = "0123456789abcdefABCDEF";
		   startFrom = 2;
		} else if (argvalue.charAt(0) == '0') {
		   validChars = "01234567";
		   startFrom = 1;
		} else if (argvalue.charAt(0) == '-') {
			startFrom = 1;
		}
		
		for (int n = startFrom; n < argvalue.length(); n++) {
			if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
		}
		return true;
	}
}
