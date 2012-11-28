package org.tok.cust.user.control;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.security.passcodec.PasswordEncoder;
import org.tok.cust.user.dao.SiteUserManagerDAO;
import org.tok.cust.user.dao.SiteUserManagerDAOFactory;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.mail.TempPasswordMailSender;
import org.tok.cust.user.model.UserVo;
import org.tok.view.Talk;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;
import org.tok.view.exception.EnfaceException;
import org.tok.view.idgenerator.IdGenerator;
import org.tok.view.statistics.PortalStatistics;

public class SiteUserManagerImpl implements SiteUserManager
{
    private static final Log log = LogFactory.getLog(SiteUserManagerImpl.class);
    
    private SiteUserManagerDAO siteUserManagerDAO = null;
	private PasswordEncoder passwordEncoder;
	private TempPasswordMailSender tempPasswordMailSender;
	 private IdGenerator idGenerator = null;
	    
    public SiteUserManagerImpl() {
    	String dbType = Talk.getConfiguration().getString("tok.db.type");
    	this.siteUserManagerDAO = SiteUserManagerDAOFactory.getDAO( dbType );
    	this.idGenerator = ((IdGenerator)Talk.getComponentManager().getComponent("IdGenerator"));
    }
    
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
    
    public void setTempPasswordMailSender(TempPasswordMailSender tempPasswordMailSender) {
		this.tempPasswordMailSender = tempPasswordMailSender;
	}

	public void login(Map userInfoMap, String userId, String passwd) throws UserException, Exception
    {
    	ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		String encPasswd = passwordEncoder.encode(userId, passwd);
    		
    		siteUserManagerDAO.authenticate(connCtx, userId, encPasswd);
    		
    		Map userMap = siteUserManagerDAO.getUserInfoMap(connCtx, userId);
    		userInfoMap.putAll( userMap );
    		
    		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_LOGGED_IN);
    		
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
    		String msgKey = ue.getMessageKey();
    		if( "pt.ev.login.label.ErrorCode.2".equals(msgKey) ) {
    			try {
	    			Map userMap = siteUserManagerDAO.getUserInfoMap(connCtx, userId);
	        		userInfoMap.putAll( userMap );
	        		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_ERROR_PASSWORD);
	        		connCtx.commit();
    			}
    			catch(Exception ie) {
    				ie.printStackTrace();
    			}
    		}
    		
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public Map getUserInfoMap(String userId) throws UserException, Exception
    {
    	Map userInfoMap = null;     
        
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		userInfoMap = siteUserManagerDAO.getUserInfoMap(connCtx, userId);
    		
    	}
    	catch (UserException e)
        {
            e.printStackTrace();
            throw e;
        } 
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	
        return userInfoMap;        
    }  
    
    public void log(Map userInfoMap) throws UserException, Exception
    {
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_LOGGED_IN);
    		connCtx.commit();
    	}
    	catch (UserException e)
        {
    		if( connCtx != null ) connCtx.rollback();
            e.printStackTrace();
            throw e;
        } 
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }  
    
    public void log(Map userInfoMap, int status) throws UserException, Exception
    {
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		siteUserManagerDAO.log(connCtx, userInfoMap, status);
    		connCtx.commit();
    	}
    	catch (UserException e)
        {
    		if( connCtx != null ) connCtx.rollback();
            e.printStackTrace();
            throw e;
        } 
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }  
    
    public void changePassword(Map userInfoMap, String userId, String password, String passwordNew) throws UserException, Exception
    {
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		String encPasswd = passwordEncoder.encode(userId, password);
    		String encPasswdNew = passwordEncoder.encode(userId, passwordNew);
    		
    		siteUserManagerDAO.changePassword(connCtx, userId, encPasswd, encPasswdNew);
    		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_CHANGE_PASSWORD);
    		
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }  
    
    public boolean isOverlap(String userId) throws Exception {
        boolean isOverlap = true;
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		isOverlap = siteUserManagerDAO.isOverlap(connCtx, userId);
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	
        return isOverlap;       
    }

	public List getUserList(String name, String regNo) throws Exception {
		ConnectionContext connCtx = null;
		List userIdList = new ArrayList();
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		userIdList.addAll(siteUserManagerDAO.getUserList(connCtx, name, regNo));
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	return userIdList;
	}
	
	public void authenticate(String userId, String passwd) throws UserException, Exception
    {
    	ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		String encPasswd = passwordEncoder.encode(userId, passwd);
    		siteUserManagerDAO.authenticate(connCtx, userId, encPasswd);
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
	
	public void join(UserVo user) throws UserException, Exception {
    	ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		String encPasswd = passwordEncoder.encode(user.getUser_id(), user.getPassword());
    		int principalKey = idGenerator.getNextIntegerId("SECURITY_PRINCIPAL");
    		user.setPrincipalKey(principalKey);
    		int credentialKey = idGenerator.getNextIntegerId("SECURITY_CREDENTIAL");
    		user.setCredentialKey(credentialKey);
    		siteUserManagerDAO.insert(connCtx, user, encPasswd);
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	public void changeUserInfo(Map userInfoMap) throws UserException, Exception {
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		UserVo user = (UserVo)userInfoMap.get("user");
    		siteUserManagerDAO.update(connCtx, user);
    		
    		Map userMap = siteUserManagerDAO.getUserInfoMap(connCtx, user.getUser_id());
    		userInfoMap.putAll( userMap );
    		
    		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_CHANGE_USERINFO);
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}

	public String reissuePassword(Map userInfoMap) throws UserException {
		String userId = (String)userInfoMap.get("user_id");
		String userName = (String)userInfoMap.get("nm_kor");
		String regNo = (String)userInfoMap.get("reg_no");
		
		String time = Long.toString(System.currentTimeMillis());
		String temp_pw = time.substring(time.length()-6, time.length());
		String encPasswd = passwordEncoder.encode(userId, temp_pw);
		ConnectionContext connCtx = null;
		
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		userInfoMap.putAll(siteUserManagerDAO.reissuePassword(connCtx, userId, encPasswd, userName, regNo));
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    		tempPasswordMailSender.sendMessage(userId, sdf.format((Date)userInfoMap.get("reg_datim")),
    											temp_pw, (String)userInfoMap.get("email_addr"));
    		siteUserManagerDAO.log(connCtx, userInfoMap, PortalStatistics.STATUS_SEARCH_USERINFO);
    		connCtx.commit();
    		return (String)userInfoMap.get("email_addr");
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	public void setUserByUserInfoMap(UserVo user) throws UserException, Exception{
		Map userInfoMap = new HashMap();     
        
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		userInfoMap.putAll(siteUserManagerDAO.getUserInfoMap(connCtx, user.getUser_id()));
    		
    		/**
    		 * 필요에 따라 추가적으로 정보를 저장하면 된다.
    		 */
    		user.setUser_name((String)userInfoMap.get("nm_kor"));
			user.setUser_hp((String)userInfoMap.get("mobile_tel"));
			user.setUser_email((String)userInfoMap.get("email_addr"));
			user.setHomeZip((String)userInfoMap.get("homeZip"));
			user.setHomeAddr1((String)userInfoMap.get("homeAddr1"));
			user.setHomeAddr2((String)userInfoMap.get("homeAddr2"));
			connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	public Map getUserInfoMapFromUserVo(UserVo user) throws Exception{
		try{
			/**
			 * 단순히 user 객체의 정보를 map 형태로 바꾸어주는 함수이다.
			 * 필요에 따라 추가적으로 정보를 저장하면 된다.
			 */
			Map userInfoMap = new HashMap();
			userInfoMap.put("user_id", user.getUser_id());
			userInfoMap.put("user_name", user.getUser_name());
			userInfoMap.put("nm_kor", user.getUser_name());
			userInfoMap.put("reg_no", user.getUser_jumin1() + user.getUser_jumin2());
			userInfoMap.put("user_jumin1", user.getUser_jumin1());
			userInfoMap.put("user_jumin2", user.getUser_jumin2());
			userInfoMap.put("mobile_tel", user.getUser_hp1() + "-" + user.getUser_hp2() + "-" + user.getUser_hp3());
			userInfoMap.put("user_hp1", user.getUser_hp1());
			userInfoMap.put("user_hp2", user.getUser_hp2());
			userInfoMap.put("user_hp3", user.getUser_hp3());
			userInfoMap.put("email_addr", user.getUser_email1() + user.getUser_email2());
			userInfoMap.put("user_email1", user.getUser_email1());
			userInfoMap.put("user_email2", user.getUser_email2());
			userInfoMap.put("homeZip", user.getHomeZip());
			userInfoMap.put("homeAddr1", user.getHomeAddr1());
			userInfoMap.put("homeAddr2", user.getHomeAddr2());
			
			return userInfoMap;
		}
		catch (Exception e)
        {
            e.printStackTrace();
            throw new UserException(e);
        }
	}
	
	public List getZipCodes(String dong, String langKnd) {
		ConnectionContext connCtx = null;
		List zipCodes = new ArrayList();
		List tempList = new ArrayList();
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		tempList.addAll(siteUserManagerDAO.getZipCodes(connCtx, dong, langKnd));
    		if(!tempList.isEmpty()){
    			zipCodes.add(tempList.get(tempList.size()-1));
        		for(int i = 0 ; i < tempList.size()-1 ; i++){
        			zipCodes.add(tempList.get(i));
        		}	
    		}
    		connCtx.commit();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	return zipCodes;
	}

	public boolean isJoined(UserVo user) throws Exception {
		ConnectionContext connCtx = null;
		List userIdList;
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		userIdList = siteUserManagerDAO.getUserList(connCtx, user.getUser_name(), user.getRegNo());
    		connCtx.commit();
    	}
    	catch (UserException ue)
        {
            if( connCtx != null ) connCtx.rollback();
            throw ue;
        }
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw new UserException(e);
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	System.out.println("*** userIdList.size()=" + userIdList.size());
    	if(userIdList.size() > 0){
    		return true;
    	}
    	return false;
	}
	
	public void addUserInfo(Map userInfoMap) throws EnfaceException, Exception
	{
		ConnectionContext connCtx = null;
		List userIdList;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		int principalKey = idGenerator.getNextIntegerId("SECURITY_PRINCIPAL");
    		userInfoMap.put("principal_id", String.valueOf(principalKey));
    		int credentialKey = idGenerator.getNextIntegerId("SECURITY_CREDENTIAL");
    		userInfoMap.put("credential_id", String.valueOf(credentialKey));
    		
    		String userId = (String)userInfoMap.get("user_id");
    		String password = (String)userInfoMap.get("password");
    		if( password != null ) {
	    		String encPasswd = passwordEncoder.encode(userId, password);
	    		userInfoMap.put("encPasswd", encPasswd);
    		}
    		
    		siteUserManagerDAO.addUserInfo(connCtx, userInfoMap);
    		
    		connCtx.commit();
    		
    		Map userMap = getUserInfoMap(userId);
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw e;
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	public void updateUserInfo(Map userInfoMap) throws EnfaceException, Exception
	{
		ConnectionContext connCtx = null;
		List userIdList;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		String userId = (String)userInfoMap.get("user_id");
    		String password = (String)userInfoMap.get("password");
    		if( password != null ) {
	    		String encPasswd = passwordEncoder.encode(userId, password);
	    		userInfoMap.put("encPasswd", encPasswd);
    		}
    		
    		siteUserManagerDAO.updateUserInfo(connCtx, userInfoMap);
    		
    		connCtx.commit();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw e;
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	public void updateUserExtraInfo(Map paramMap) throws EnfaceException, Exception
	{
		ConnectionContext connCtx = null;
		List userIdList;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
    		siteUserManagerDAO.updateUserExtraInfo(connCtx, paramMap);
    		
    		connCtx.commit();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
            throw e;
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
}
