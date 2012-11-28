package org.tok.cust.common;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.user.control.SiteUserManager;
import org.tok.view.Talk;
import org.tok.view.login.LoginConstants;
import org.tok.view.security.UserServiceDAC;
import org.tok.view.session.SessionManager;
import org.tok.view.statistics.PortalStatistics;
import org.tok.view.util.HttpUtil;


public class UserInfomationHandler
{
	private static Log log = LogFactory.getLog(UserInfomationHandler.class);
	
	private SessionManager sm = null;
	private SiteUserManager siteUserManager = null;
	private UserServiceDAC userServiceDAC = null;
	
	public UserInfomationHandler()
	{
		this.sm = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
		this.siteUserManager = (SiteUserManager)Talk.getComponentManager().getComponent("org.tok.cust.user.control.UserManager");
		this.userServiceDAC = (UserServiceDAC)Talk.getComponentManager().getComponent("org.tok.view.admin.user.service.UserService");
	}
	
	public Map createUserInfomation(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception	
	{
		Map userInfoMap = null;
        
        try {
        	HttpSession session = request.getSession();
        	if( session != null ) {
        		session.removeAttribute(LoginConstants.ERRORCODE);
        	}

    		String encoding = request.getCharacterEncoding();
    		String langKnd = request.getParameter("langKnd");

        	userInfoMap = siteUserManager.getUserInfoMap( userId );
        	
    		if( langKnd != null ) {
    			userInfoMap.put("lang_knd", langKnd);
    		}
    		
    		sm.setUserData(request, userInfoMap);
        	
        	Cookie tmp = new Cookie("TalkSessionID", session.getId());
            tmp.setPath("/");
            //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "enview") );
            tmp.setMaxAge( -1 );
            response.addCookie( tmp );
            
            this.userServiceDAC.getUserPermission( userInfoMap );
        }
        catch(Exception e) {
        	throw e;
        }
        
        return userInfoMap;
	}
	
	
}
