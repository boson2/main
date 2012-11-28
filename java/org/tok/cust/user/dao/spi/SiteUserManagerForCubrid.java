package org.tok.cust.user.dao.spi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.user.dao.AbstractSiteUserManagerDAO;
import org.tok.cust.user.model.UserVo;
import org.tok.view.Talk;
import org.tok.view.components.dao.ConnectionContext;


public class SiteUserManagerForCubrid extends AbstractSiteUserManagerDAO
{
	private static final Log log = LogFactory.getLog( SiteUserManagerForCubrid.class );
	
	public SiteUserManagerForCubrid() { }
	
	
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
    		throw e;
        } finally {
            try {
            	if (null != principalUpdateStmt) principalUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    	try {
    		credentialUpdateSql.append( "INSERT INTO SECURITY_CREDENTIAL(CREDENTIAL_ID, PRINCIPAL_ID, COLUMN_VALUE, \"TYPE\", CLASSNAME, UPDATE_REQUIRED, ");
    		credentialUpdateSql.append( "IS_ENCODED, IS_ENABLED, AUTH_FAILURES, IS_EXPIRED, CREATION_DATE, MODIFIED_DATE) " );
    		credentialUpdateSql.append( "VALUES(" + user.getCredentialKey() + ", " + user.getPrincipalKey() + ", '" + encPasswd + "', 0,'" + this.getClass().getName() + "', 0, ");
    		credentialUpdateSql.append( "1, 1, 0, 0, '" + time + "', '" + time + "') ");
    		credentialUpdateStmt = conn.prepareStatement( credentialUpdateSql.toString() );
			credentialUpdateStmt.executeUpdate();
			
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(credentialUpdateSql.toString());
    		throw e;
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
    		userpassUpdateSql.append( "'" + time + "','" + user.getLangKnd() + "','" + user.getHomeZip() + "','" + user.getHomeAddr1() + "', '" + user.getHomeAddr2() + "') ");

    		userpassUpdateStmt = conn.prepareStatement( userpassUpdateSql.toString() );
			userpassUpdateStmt.executeUpdate();
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println(userpassUpdateSql.toString());
    		throw e;
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
    		throw e;
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
    		throw e;
        } finally {
            try {
            	if (null != roleUpdateStmt) roleUpdateStmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	
}
