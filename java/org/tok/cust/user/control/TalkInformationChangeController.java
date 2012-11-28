package org.tok.cust.user.control;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.util.WebUtils;
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

public class TalkInformationChangeController extends AbstractWizardFormController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	
	public TalkInformationChangeController() {
		setCommandName("user");
        setCommandClass(UserVo.class);
        
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
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering '" + this.getClass().getName() + "'s formBackingObject' method...");
		}
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();	
		user.setUser_id((String)WebUtils.getSessionAttribute(request, LoginConstants.SSO_LOGIN_ID));
		
		return user;
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,	
			Errors errors, int page) throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering '" + this.getClass().getName() + "'s referenceData' method... page [" + page + "] requested");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		Map model = new HashMap();
		switch(page){
			case 0: break;
			case 1: break;
			case 2: model.put("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false)); break;
			case 3: break;
			default: break;
		}
		model.put("langKnd", langKnd);
		return model;
	}
	
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s postProcessPage' method... Page [" + page + "] requested");
		}
		UserVo user = (UserVo)command;
		try{
			switch(page){
				case 0: break; //start Page
				case 1:	//confirm page
					siteUserManager.authenticate(user.getUser_id(), user.getPassword());
					siteUserManager.setUserByUserInfoMap(user);
					break;
				case 2:	//userInfo page
					enviewUserValidator.validateUserHp(command, errors);
					enviewUserValidator.validateUserEmail(command, errors);
					user.setUser_hp(user.getUser_hp1() + "-" + user.getUser_hp2() + "-" + user.getUser_hp3());
					user.setUser_email(user.getUser_email1() + "@" + user.getUser_email2());
					break;
				case 3: break; //userInfoConfirm page
				case 4: break; //complete page
				default: break;
			}
		}catch(UserException ue){
			String msgKey = ue.getMessageKey();
			if( msgKey != null ){
				errors.reject(msgKey);
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
				String errorMessage = enviewMessages.getString( msgKey );
				logger.debug("*** errorMessage=" + errorMessage);
				request.setAttribute("errorMessage", errorMessage);
			}
		}
	}
	
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s processCancel' method...");
			logger.debug("*** InformationChange cancel!");
		}
		return showNewForm(request, response);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse reponse, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s processFinish' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );

		Map userInfoMap = new HashMap();
		UserVo user = (UserVo)command;
		try{
			userInfoMap.put("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
    		userInfoMap.put("user-agent", HttpUtil.getUserAgent(request));
    		userInfoMap.put("user", user);
    		
			siteUserManager.changeUserInfo(userInfoMap);
			request.getSession().removeAttribute(LoginConstants.ERRORCODE);
			
    		if( langKnd != null ) {
    			userInfoMap.put("lang_knd", langKnd);
    		}
        	enviewSessionManager.setUserData(request, userInfoMap);

			if (logger.isDebugEnabled()) {
				logger.debug("*** TalkInformationChangeController userInfoMap=" + userInfoMap);
				logger.debug("*** InformationChange complete!");
			}
			return showPage(request, errors, getPageCount()-1);
		}catch(UserException e){
			String msgKey = e.getMessageKey();
			if( msgKey != null ){
				errors.reject(msgKey);
				String errorMessage = enviewMessages.getString( msgKey );
				logger.debug("*** errorMessage=" + errorMessage);
				request.setAttribute("errorMessage", errorMessage);
			}
			return showPage(request, errors, getPageCount()-2);
		}
		
	}
	
}
