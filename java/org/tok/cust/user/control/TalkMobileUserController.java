package org.tok.cust.user.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.cust.common.UserInfomationHandler;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.cust.security.TalkMobileAuthentication;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.sso.TalkSSOManager;
import org.tok.view.statistics.PortalStatistics;
import org.tok.view.util.HttpUtil;

public class TalkMobileUserController extends MultiActionController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SiteUserManager siteUserManager;
	private UserInfomationHandler userInfomationHandler;
	
	public TalkMobileUserController()
	{
        this.userInfomationHandler = (UserInfomationHandler)Talk.getComponentManager().getComponent("org.tok.cust.common.UserInfomationHandler");
	}
	
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}

	protected Map referenceData (HttpServletRequest request, Object command, Errors errors) throws Exception 
	{
		UserVo user = (UserVo)command;
		Map          model   = new HashMap();
		
		return model;
	}

	/**
	 * Show login form
	 */
	public ModelAndView firstLogin(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		List langKndList = (List)enviewCodeBundle.getCodes("PT", "105", 1, true);
		user.setLangKndList( langKndList );
		
		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("isFirst", new Boolean(true));
		
		return new ModelAndView("/user/m_login");
	}
	
	/**
	 * Show login form
	 */
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		List langKndList = (List)enviewCodeBundle.getCodes("PT", "105", 1, true);
		user.setLangKndList( langKndList );
		
		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("isFirst", new Boolean(false));
		
		return new ModelAndView("/user/m_login");
	}
	
	public ModelAndView profile(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		List authList = (List)enviewCodeBundle.getCodes("PT", "118", 1, false);
		
		request.setAttribute("authList", authList);
		request.setAttribute("langKnd", langKnd);
		Map userInfoMap = (Map)request.getAttribute(LoginConstants.SSO_LOGIN_INFO);
		if( userInfoMap == null ) {
			userInfoMap = TalkSSOManager.getUserInfo (request);
		}
		request.setAttribute("userInfoMap", userInfoMap);
		
		return new ModelAndView("/user/m_profile");
	}
	
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String destination = request.getParameter(LoginConstants.DESTINATION);
		HttpSession session = request.getSession(false);
    	if( session != null ) {
	        
	    	if( destination == null ) {
	        	destination = Talk.getConfiguration().getString("sso.login.page");
	        }
	        
	        try {
	        	session.invalidate();
	        	Cookie[] cookies = request.getCookies();
	            for(int i=0; i<cookies.length; i++) {
	            	if( cookies[i].getName().equals(TalkMobileAuthentication.USER_ID_KEY) ) {
	            		Cookie tmp = new Cookie(TalkMobileAuthentication.USER_ID_KEY, cookies[i].getValue());
	    	            tmp.setPath("/");
	    	            tmp.setMaxAge(0);
	    	            //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "talk") );
	    	            response.addCookie( tmp );
	            		break;
	            	}
	            }
	    		
	    		return new ModelAndView("redirect:" + destination);
	        }
	        catch(Exception e)
	        {
	        	log.info("*** " + e.getMessage());
	        	response.sendRedirect( request.getContextPath() + destination );
	        }
    	}
    	else {
    		if( destination == null ) {
	        	destination = Talk.getConfiguration().getString("sso.login.page");
	        }
    		return new ModelAndView("redirect:" + destination);
    	}
        
        return null;
	}
	
	/**
	 * Show login form
	 */
	public ModelAndView getAddress(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		List langKndList = (List)enviewCodeBundle.getCodes("PT", "105", 1, true);
		user.setLangKndList( langKndList );
		
		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("isFirst", new Boolean(false));
		
		return new ModelAndView("/user/m_google_map");
	}
}
