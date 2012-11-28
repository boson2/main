package org.tok.cust.user.control;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.cust.user.validator.TalkUserValidator;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;
import org.tok.view.util.HttpUtil;

public class TalkAjaxInfoChangeController extends MultiActionController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	
	private UserVo user;
	
	public TalkAjaxInfoChangeController() {
        user = new UserVo();
        this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	public void setTalkUserValidator(TalkUserValidator enviewUserValidator) {
		this.enviewUserValidator = enviewUserValidator;
	}
	
	private String initData(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s initData' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		
		request.setAttribute("langKnd", langKnd);
		return request.getParameter("m");
	}
	
	public ModelAndView ajaxChangeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s ajaxChange' method... parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("start")){
				return responseStart(request);
			}
			else if(method.equals("passwordConfirm")){
				return responsePasswordConfirm(request);
			}
			else if(method.equals("userInfo")){
				return responseUserInfo(request);
			}
			else if(method.equals("userInfoChange")){
				return responseUserInfoChange(request);
			}
			else if(method.equals("userInfoConfirm")){
				return responseUserInfoConfirm(request);
			}
			else if(method.equals("complete")){
				return responseComplete(request);
			}
		}
		else{
			return responseChange(request);
		}
		return null;
	}
	
	private ModelAndView responseChange(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseChange' method...");
		}
		HttpSession session = request.getSession(true);
		user.setUser_id((String)session.getAttribute(LoginConstants.SSO_LOGIN_ID));
		return new ModelAndView("user/ajax_change/change");
	}
	
	private ModelAndView responseStart(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseStart' method...");
		}
		request.setAttribute("user_id", user.getUser_id());
		return new ModelAndView("user/ajax_change/start");
	}
	
	private ModelAndView responsePasswordConfirm(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responsePasswordConfirm' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		request.setAttribute("langKnd", langKnd);
		return new ModelAndView("user/ajax_change/passwordConfirm");
	}
	
	private ModelAndView responseUserInfo(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfo' method...");
		}
		siteUserManager.setUserByUserInfoMap(user);
		request.setAttribute("user", user);
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		request.setAttribute("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false));
		return new ModelAndView("user/ajax_change/userInfo");
	}
	
	private ModelAndView responseUserInfoChange(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfoChange' method...");
		}
		
		request.setAttribute("user", user);
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		request.setAttribute("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false));
		return new ModelAndView("user/ajax_change/userInfoChange");
	}
	
	
	private ModelAndView responseUserInfoConfirm(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfoConfirm' method...");
		}
		String homeZip1 = request.getParameter("homeZip1");
		String homeZip2 = request.getParameter("homeZip2");
		String homeAddr1 = request.getParameter("homeAddr1");
		String homeAddr2 = request.getParameter("homeAddr2");
		String user_hp1 = request.getParameter("user_hp1");
		String user_hp2 = request.getParameter("user_hp2");
		String user_hp3 = request.getParameter("user_hp3");
		String user_email1 = request.getParameter("user_email1");
		String user_email2 = request.getParameter("user_email2");
		
		user.setHomeZip(homeZip1 + homeZip2);
		user.setHomeAddr1(homeAddr1);
		user.setHomeAddr2(homeAddr2);
		user.setUser_hp1(user_hp1);
		user.setUser_hp2(user_hp2);
		user.setUser_hp3(user_hp3);
		user.setUser_hp(user_hp1 + "-" + user_hp2 + "-" + user_hp3);
		user.setUser_email1(user_email1);
		user.setUser_email2(user_email2);
		user.setUser_email(user_email1 + "@" + user_email2);
		
		request.setAttribute("user", user);
		
		try {
			enviewUserValidator.validateUserHp(user);
			enviewUserValidator.validateUserEmail(user);	
		}catch(UserException ue){
			alertError(request, ue);
			return new ModelAndView("user/ajax_change/userInfoChange");
		}
		return new ModelAndView("user/ajax_change/userInfoConfirm");
	}
	
	private ModelAndView responseComplete(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseComplete' method...");
		}
		request.setAttribute("user", user);
		
		Map userInfoMap = new HashMap();
		userInfoMap.put("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
		userInfoMap.put("user-agent", HttpUtil.getUserAgent(request));
		userInfoMap.put("user", user);
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		if( langKnd != null ) {
			userInfoMap.put("lang_knd", langKnd);
		}
		try{
			siteUserManager.changeUserInfo(userInfoMap);
			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			enviewSessionManager.setUserData(request, userInfoMap);
	    	
			if (logger.isDebugEnabled()) {
				logger.debug("*** TalkInformationChangeController userInfoMap=" + userInfoMap);
				logger.debug("*** InformationChange complete!");
			}
		}catch(UserException ue){
			alertError(request, ue);
			return new ModelAndView("user/ajax_change/userInfoConfirm");
		}
		//가입 완료 성공 시 complete 페이지로 이동
		return new ModelAndView("user/ajax_change/complete");
	}
	
	private void alertError(HttpServletRequest request, UserException ue){
		String msgKey = ue.getMessageKey();
		if( msgKey != null ){
			String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
			MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
			String errorMessage = enviewMessages.getString( msgKey );
			logger.debug("*** errorMessage=" + errorMessage);
			request.setAttribute("errorMessage", errorMessage);
		}
	}
}
