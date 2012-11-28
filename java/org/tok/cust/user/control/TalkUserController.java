package org.tok.cust.user.control;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.cust.user.dao.UserException;
import org.tok.view.Talk;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;
import org.tok.view.util.HttpUtil;

public class TalkUserController extends MultiActionController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager; 

	public TalkUserController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	public ModelAndView changeUser(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		
		String destination = request.getParameter(LoginConstants.DESTINATION);
		String userId = request.getParameter("userId");
        HttpSession session = request.getSession();
        
        if( userId == null || userId.length()==0 ) {
        	if (destination == null) {
    			destination = Talk.getConfiguration().getString("sso.login.page", "/login.do");
	        }
        	log.error("UserId has not hear !!!");
        	return new ModelAndView( "redirect:" + destination );
        }
        
        log.debug("*** userId=" + userId);
        try {
        	session.invalidate();
        	log.debug("*** success invalidate");
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        session = request.getSession(true);
        
        try {
        	Map userInfoMap = new HashMap();
    		userInfoMap.put("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
    		userInfoMap.put("user-agent", HttpUtil.getUserAgent(request));
    		
    		Map userMap = siteUserManager.getUserInfoMap(userId);
    		String dbLangKnd = (String)userMap.get("lang_knd");
    		if( dbLangKnd == null ) {
    			userInfoMap.put("lang_knd", langKnd);
    		}
    		
    		userInfoMap.putAll( userMap );
    		
    		request.getSession().removeAttribute(LoginConstants.ERRORCODE);
    		session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);

        	enviewSessionManager.setUserData(request, userInfoMap);

        	if (destination == null) {
        		if( userInfoMap != null ) {
        			String defaultPage = (String)userInfoMap.get("default_page");
    				if( defaultPage != null ) {
    					if( defaultPage.indexOf(".page") > -1 ) {
    						destination = "/portal" + defaultPage;
    					}
    					else {
    						destination = defaultPage;
    					}
    				}
    				else {
    					destination = "";
    				}
        		}
            }
        	
        	Cookie tmp = new Cookie("TalkSessionID", session.getId());
            tmp.setPath("/");
            //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "enview") );
            tmp.setMaxAge( -1 );
            response.addCookie( tmp );
    	}
		catch(UserException se) 
		{
			se.printStackTrace();
			
			
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
	        }
	        
	        if (destination == null) {
    			destination = Talk.getConfiguration().getString("sso.login.page", "/user/login.cust");
	        }
	        
	        return new ModelAndView( "redirect:" + destination );
        }
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
        int pos = destination.indexOf( request.getContextPath() );
        if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
        	destination = destination.substring(request.getContextPath().length());
        	log.debug("*** destination=" + destination);
        }
        
		return new ModelAndView("redirect:" + destination);
	}

}
