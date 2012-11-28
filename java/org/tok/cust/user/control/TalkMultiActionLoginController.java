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
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.statistics.PortalStatistics;
import org.tok.view.util.HttpUtil;

public class TalkMultiActionLoginController extends MultiActionController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SiteUserManager siteUserManager;
	private UserInfomationHandler userInfomationHandler;
	
	public TalkMultiActionLoginController()
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
		
		String loginUrl = Talk.getConfiguration().getString("sso.login.destination" );
		//String loginUrl = request.getContextPath() + "/user/loginProcess.cust";
		String destination = request.getParameter("destination");
		if( destination != null && destination.length()>0 ) {
			loginUrl += "?destination=" + destination;
		}
		request.setAttribute("loginUrl", loginUrl);
		
		log.info("*** loginUrl=" + loginUrl);
		System.out.println("############ loginUrl=" + loginUrl);
		
		return new ModelAndView("/user/login");
	}
	
	/*
	 * validation and login for enview
	 */
	public ModelAndView loginProcess(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		ModelAndView modelAndView = null;
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );

        String userId = request.getParameter("userId");
        String encorderPassword = request.getParameter("password");
        String password = null;
        
        try {
        	HttpSession session = request.getSession(false);
        	if( session == null ) session = request.getSession(true);
        	
	        // 아이디 체크
			if (userId == null || userId.equals("")) {
				throw new UserException("pt.ev.login.error.hasNotUserId");
			}
			
			// 비밀번호 체크
			if (encorderPassword == null || encorderPassword.equals("")) {
				throw new UserException("pt.ev.login.error.hasNotPassword");
			}
	        
	        if (encorderPassword != null) {
	            boolean enableInterEncryption = Talk.getConfiguration().getBoolean("sso.interEncryption", true);
	            if( enableInterEncryption ) {
		        	byte[] value = encorderPassword.getBytes();
			        for (int i=0; i<value.length; i++) {
			        	value[i] = (byte)(value[i] + ((i+2) % 7));
			        }
			        password = new String(value);
	            }
	            else {
	            	password = encorderPassword;
	            }
	        }
	        
	        siteUserManager.authenticate(userId, password);
	        
	        session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);
	        Map userInfoMap = this.userInfomationHandler.createUserInfomation(request, response, userId);
	        //userInfoMap.put("extraUserInfo01", "TEST_PART01");
			//userInfoMap.put("extraUserInfo02", "TEST_PART02");
			//userInfoMap.put("extraUserInfo03", "TEST_PART03");
			//log.debug("*** TalkLoginController userInfoMap=" + userInfoMap);
            siteUserManager.log(userInfoMap, PortalStatistics.STATUS_LOGGED_IN);
            session.setAttribute(LoginConstants.USERNAME, userInfoMap.get("nm_kor"));
            session.setAttribute("langKnd", langKnd);
	        modelAndView = getDestination(request, userInfoMap);
    	}
        catch(UserException se) 
		{
			se.printStackTrace();
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
		        if( "pt.ev.login.label.ErrorCode.1".equals(msgKey) ) {
		        	// 사용자 없음
		        }
		        else if( "pt.ev.login.label.ErrorCode.2".equals(msgKey) ) {
		        	// 비밀번호 오류
		        	Map userInfoMap = siteUserManager.getUserInfoMap( userId );
		        	userInfoMap.put("user-agent", HttpUtil.getUserAgent(request));
		        	userInfoMap.put("remote_address", HttpUtil.getClientIp(request));
		        	siteUserManager.log(userInfoMap, PortalStatistics.STATUS_ERROR_PASSWORD);
		        }
		        else if( "pt.ev.login.label.ErrorCode.3".equals(msgKey) ) {
		        	// 비활성 ID
		        }
		        else if( "pt.ev.login.label.ErrorCode.8".equals(msgKey) ) {
		        	// 비밀번호 오류횟수 초과
		        }
	        }
	        
	        String destination = Talk.getConfiguration().getString("sso.login.page");
	        log.info("*** login destination=" + destination);
	        request.getRequestDispatcher( destination ).forward(request, response);
	        return null;
	        //return new ModelAndView("/user/login");
        }
		catch(Exception e) 
		{
			e.printStackTrace();
	        
			String destination = Talk.getConfiguration().getString("sso.login.page");
	        log.info("*** login destination=" + destination);
	        request.getRequestDispatcher( destination ).forward(request, response);
	        return null;
	        //return new ModelAndView("/user/login");
        }
		
        return modelAndView;
    }
	
	/*
	 * login for enpass
	 */
	public ModelAndView enpassLoginProcess(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String langKnd = request.getParameter("langKnd") == null ? request.getLocale().getLanguage() : request.getParameter("langKnd");
		ModelAndView modelAndView = null;
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		
        try {
        	HttpSession session = request.getSession(false);
        	if( session == null ) {
        		log.info("*** Session is null. so now create new session");
        		session = request.getSession(true);
        	}
        	String ssoLoginId = (String)session.getAttribute( LoginConstants.SSO_LOGIN_ID );
			String errorCode = request.getParameter("epErrCode");
			if( errorCode != null ) {
				throw new UserException( "pt.ev.login.label.ErrorCode." + errorCode );
			}
			
			/*
        	if( ssoLoginId == null || "guest".equals(ssoLoginId) ) {
    			if( errorCode != null ) {
    				throw new UserException( "pt.ev.login.label.ErrorCode." + errorCode );
    			}
    			else {
    				throw new UserException( "pt.ev.login.label.ErrorCode.7" );
    			}
    		}
    		*/
			
			Map userInfoMap = this.userInfomationHandler.createUserInfomation(request, response, ssoLoginId);
			//userInfoMap.put("extraUserInfo01", "TEST_PART01");
			//userInfoMap.put("extraUserInfo02", "TEST_PART02");
			//userInfoMap.put("extraUserInfo03", "TEST_PART03");
			//log.debug("*** TalkLoginController userInfoMap=" + userInfoMap);
	        
	        siteUserManager.log(userInfoMap, PortalStatistics.STATUS_LOGGED_IN);
	        session.setAttribute(LoginConstants.USERNAME, userInfoMap.get("nm_kor"));
	        session.setAttribute("langKnd", langKnd);
			modelAndView = getDestination(request, userInfoMap);
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
	        
	        String destination = Talk.getConfiguration().getString("sso.login.page");
	        log.info("*** login destination=" + destination);
	        request.getRequestDispatcher( destination ).forward(request, response);
	        return null;
	        //return new ModelAndView("/user/login");
        }
		catch(Exception e) 
		{
			e.printStackTrace();
	        
			String destination = Talk.getConfiguration().getString("sso.login.page");
	        log.info("*** login destination=" + destination);
	        request.getRequestDispatcher( destination ).forward(request, response);
	        return null;
	        //return new ModelAndView("/user/login");
        }
		
        return modelAndView;
    }
	
	/*
	 * common login process
	 */
	private ModelAndView getDestination(HttpServletRequest request, Map userInfoMap) throws Exception 
	{
		String current = request.getParameter("current");
        String destination = request.getParameter("destination");
		
    	if (destination == null) {
    		if( current != null && current.length() > 0) {
        		if( current.indexOf("?") > -1 ) {
        			destination = current.substring(0, current.indexOf("?"));
        		}
        		else {
        			destination = current;
        		}
        	}
        	else {
        		//destination = "/";
        		
        		if( userInfoMap != null ) {
        			String defaultPage = (String)userInfoMap.get("default_page");
    				if( defaultPage != null ) {
    					destination = "/portal" + defaultPage + ".page";
    				}
    				else {
    					destination = "";
    				}
        		}
        		
        	}
        }
    	
        int pos = destination.indexOf( request.getContextPath() );
        if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
        	destination = destination.substring(request.getContextPath().length());
        }
        
    	log.info("*** destination=" + destination);
		String updateRequired = (String)userInfoMap.get("update_required");
        if( updateRequired != null && "1".equals(updateRequired) ) {
        	return new ModelAndView("redirect:/user/changePassword.cust");
        }
        else {
        	return new ModelAndView("redirect:" + destination);
        }
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
	            	if( cookies[i].getName().equals("TalkSessionID") ) {
	            		Cookie tmp = new Cookie("TalkSessionID", cookies[i].getValue());
	    	            tmp.setPath("/");
	    	            tmp.setMaxAge(0);
	    	            //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "enview") );
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
}
