package org.tok.cust.user.control;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.cust.user.validator.TalkUserValidator;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;
import org.tok.view.util.HttpUtil;

public class TalkUserHelpController extends SimpleFormController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	
	public TalkUserHelpController()
	{
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
			logger.debug("entering '" + this.getClass().getName()+ "'s formBackingObject' method...");
		}
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		request.setAttribute("langKnd", request.getLocale().getLanguage());
		return super.formBackingObject(request);
	}
	
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering '" + this.getClass().getName()+ "'s onBindAndValidate' method...");
		}
		enviewUserValidator.validateRegistryNumber(command, errors);
	}
	
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName()+ "'s showForm' method...");
		}
		return super.showForm(request, response, errors, controlModel);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName()+ "'s onSubmit' method...");
		}
		UserVo user = (UserVo)command;
		Map model = new HashMap();
		try{
			String searchItem = (String)request.getParameter("searchItem");

			if(searchItem.equals("id")){
				List userList = 
					siteUserManager.getUserList(user.getUser_name(), 
												user.getUser_jumin1() + user.getUser_jumin2());
				for(int i = 0 ; i < userList.size() ; i++){
					UserVo usr = (UserVo)userList.get(i);
					usr.setUser_id(usr.getUser_id().substring(0, 4) + "****");
				}
				model.put("userList", userList);
				model.put("isIdSearch", new Boolean(true));
			}
			else if(searchItem.equals("password")){
				Map userInfoMap = siteUserManager.getUserInfoMapFromUserVo(user);
				userInfoMap.put("remote_address", InetAddress.getByName(request.getRemoteAddr()).getHostAddress());
	    		userInfoMap.put("user-agent", HttpUtil.getUserAgent(request));
	    		
	    		String to = siteUserManager.reissuePassword(userInfoMap);
				String[] splitedEmail = to.split("@");
				String email_id = splitedEmail[0].substring(0, 4) + "****";
				model.put("user_email", email_id + "@" + splitedEmail[1]);
				model.put("isIdSearch", new Boolean(false));
			}
		}catch(UserException e){
			String msgKey = e.getMessageKey();
			if( msgKey != null ){
				errors.reject(msgKey);
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
				String errorMessage = enviewMessages.getString( msgKey );
				logger.debug("*** errorMessage=" + errorMessage);
				request.setAttribute("errorMessage", errorMessage);
			}
	        return showForm(request, response, errors);
		}
		String loginUrl = Talk.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.cust");
		model.put("loginUrl", loginUrl);
		return new ModelAndView(getSuccessView(), model);		
	}
}
