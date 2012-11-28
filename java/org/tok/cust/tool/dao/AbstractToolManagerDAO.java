package org.tok.cust.tool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.tool.model.SimpleUserVo;
import org.tok.cust.tool.model.ZipCodeVo;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.StandardDaoSupport;


public abstract class AbstractToolManagerDAO extends StandardDaoSupport implements ToolManagerDAO
{
	private static final Log log = LogFactory.getLog( AbstractToolManagerDAO.class );
	
	public AbstractToolManagerDAO()
	{
	}
	
	/* ZipCode */
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
    	}catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
            	if (selectStmt != null ) selectStmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
		return zipCodes;
	}
	
	
	/**
	 *  search user functions 
	 */
	public int getGroupId(ConnectionContext connCtx, String userId) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		StringBuffer selectQuery = new StringBuffer();
		
		int group_id = -1;
    	try {
    		selectQuery.append( " SELECT " );
   			selectQuery.append( " B.GROUP_ID " );
    		selectQuery.append( " FROM SECURITY_PRINCIPAL A, SECURITY_USER_GROUP B " );
    		selectQuery.append( " WHERE " );
    		selectQuery.append( " A.PRINCIPAL_TYPE = 'U' " );
    		selectQuery.append( " AND A.SHORT_PATH = ? " );
    		selectQuery.append( " AND A.PRINCIPAL_ID = B.USER_ID" );
    	    log.info("getGroupId SQL: " + selectQuery);
    	    
    	    conn = connCtx.getConnection();
    	    selectStmt = conn.prepareStatement( selectQuery.toString() );
    	    selectStmt.setString(1, userId);
    		selectStmt.executeUpdate();
    		rs = selectStmt.executeQuery();
    		
    		if( rs.next() ) {
    			group_id = rs.getInt(1);
    		}
    		if (null != rs) rs.close();
    		if (null != selectStmt) selectStmt.close();
    		
    	}catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
            	if (null != selectStmt) selectStmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.info("Get Group Id : " + group_id);
        return group_id;
	}
	
	/* search User */
	public Collection getUserList(ConnectionContext connCtx, String searchName, int searchType, String userId, int groupId) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;
		StringBuffer selectQuery = new StringBuffer();
		Collection users = new ArrayList();
    	try {
    		selectQuery.append( " SELECT " );
   			selectQuery.append( " A.PRINCIPAL_ID, A.PRINCIPAL_TYPE, A.PRINCIPAL_NAME, A.SHORT_PATH, " );
   			selectQuery.append( " B.GROUP_ID " );
    		selectQuery.append( " FROM SECURITY_PRINCIPAL A " );
    		selectQuery.append( " JOIN SECURITY_USER_GROUP B ON A.PRINCIPAL_ID = B.USER_ID ");
    		selectQuery.append( " WHERE " );
    		selectQuery.append( " A.SHORT_PATH != ? " );
    		selectQuery.append( " AND B.GROUP_ID = ? " );
    		switch(searchType){
				case 0: selectQuery.append( " AND A.PRINCIPAL_NAME = ? " ); break;
				case 1: selectQuery.append( " AND A.SHORT_PATH = ? " ); 	break;
				case 2: break;
				default: break;
    		}
    		selectQuery.append( " ORDER BY PRINCIPAL_NAME ASC");
    	    log.info("getUserList SQL: " + selectQuery);
    	    
    	    conn = connCtx.getConnection();
    	    selectStmt = conn.prepareStatement( selectQuery.toString() );
    	    selectStmt.setString(1, userId);
    	    selectStmt.setInt(2, groupId);
    	    selectStmt.setString(3, searchName);
    		selectStmt.executeUpdate();
    		rs = selectStmt.executeQuery();
    		
    		while( rs.next() ) {
    			SimpleUserVo userInfo = new SimpleUserVo();
    			userInfo.setPrincipal_id(rs.getInt(1));
    			userInfo.setPrincipal_tpye(rs.getString(2));
    			userInfo.setPrincipal_name(rs.getString(3));
    			userInfo.setShort_path(rs.getString(4));
    			
    			userInfo.setGroup_id(rs.getInt(5));
    			
    			userInfo.setUserName(rs.getString(3));
    			userInfo.setUserId(rs.getString(4));
    			log.debug("getUserList Searched User: " + userInfo.toString());
    			users.add(userInfo);
    		}
    		if (null != rs) rs.close();
    		if (null != selectStmt) selectStmt.close();
    		
    	}catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
            	if (null != selectStmt) selectStmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.debug("getUserList users.size()=" + users.size());
        return users;
	}
}
