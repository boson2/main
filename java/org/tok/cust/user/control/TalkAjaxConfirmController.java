package org.tok.cust.user.control;

import java.util.ArrayList;
import java.util.List;

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

public class TalkAjaxConfirmController extends MultiActionController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	
	public TalkAjaxConfirmController() {
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
	
	private String initData(HttpServletRequest request){
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s initData' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		request.setAttribute("langKnd", langKnd);
		
		String method = request.getParameter("m");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Parameter method is '" + method + "'");
		}
		return method;
	}
	
	public ModelAndView ajaxConfirm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s confirm' method...parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("confirmRegNo")){
				reponseConfirmRegNo(request);
			}
			else if(method.equals("confirmPassword")){
				reponseConfirmPassword(request);
			}
			else if(method.equals("confirmOverlap")){
				reponseConfrimOverlap(request);
			}	
			else {
				errorReport(request);
			}
		}
		return new ModelAndView("user/confirm");
	}
	
	private void reponseConfirmRegNo(HttpServletRequest request)throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s confirmRegNo' method...");
		}
		
		String user_name = (String)request.getParameter("user_name");
		String user_jumin1 = (String)request.getParameter("user_jumin1");
		String user_jumin2 = (String)request.getParameter("user_jumin2");
		UserVo user = new UserVo();
		
		if(user_name != null && !user_name.equals("") &&
				user_jumin1 != null && !user_jumin1.equals("") &&
				user_jumin2 != null && !user_jumin2.equals("")){
			user.setUser_name(user_name);
			user.setUser_jumin1(user_jumin1);
			user.setUser_jumin2(user_jumin2);
			user.setRegNo(user_jumin1 + user_jumin2);
			try{			
				enviewUserValidator.validateRegistryNumber(user);
				if(siteUserManager.isJoined(user)){
					//joined
					request.setAttribute("isSuccess", "joined");
					request.setAttribute("result", user_name + "님, 실명 인증이 완료되었습니다.!");
				}
				else {
					//not join
					request.setAttribute("isSuccess", "not join");
					request.setAttribute("result", user_name + "님, 실명 인증이 완료되었습니다.!");
				}
				System.out.println(user_name + "님, 실명 인증이 완료되었습니다.!");
			}catch(UserException e){
				String msgKey = e.getMessageKey();
				if( msgKey != null ){
					String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
					MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
					String errorMessage = enviewMessages.getString( msgKey );
					logger.debug("*** errorMessage=" + errorMessage);
					//fail(not match)
					request.setAttribute("isSuccess", "no");
					request.setAttribute("result", errorMessage);
				}
			}
		}else{
			List parameters = new ArrayList();
			parameters.add("user_name=" + user_name);
			parameters.add("user_jumin1=" + user_jumin1);
			parameters.add("user_jumin2=" + user_jumin2);
			errorReport(request, parameters);
		}
	}
	
	private void reponseConfirmPassword(HttpServletRequest request)throws Exception{
		String user_id = (String)request.getParameter("firstJoinUser");
		if(user_id == null || user_id.equals("")){
			HttpSession session = request.getSession(true);
			user_id = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		}
		String password = (String)request.getParameter("password");
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s reponseConfirmPassword' method...");
			logger.debug("request parameters are user_id='" + user_id + 
						 " and password=" + password);
		}
		if(user_id != null && !user_id.equals("") &&
				password != null && !password.equals("")){
			try{
				siteUserManager.authenticate(user_id, password);
				request.setAttribute("result", "인증이 완료되었습니다.");
				request.setAttribute("isSuccess", "yes");
			}catch(UserException e){
				String msgKey = e.getMessageKey();
				if( msgKey != null ){
					String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
					MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
					String errorMessage = enviewMessages.getString( msgKey );
					logger.debug("*** errorMessage=" + errorMessage);
					request.setAttribute("result", "잘못된 비밀번호 입니다. 비밀번호를 모르신다면 <a href='help.cust' target='_blank'>비밀번호 찾기</a>를 눌러주세요");
					request.setAttribute("isSuccess", "no");
				}
			}
		}else{
			List parameters = new ArrayList();
			parameters.add("user_id=" + user_id);
			parameters.add("password=" + password);
			errorReport(request, parameters);
		}
	}
	
	public void reponseConfrimOverlap(HttpServletRequest request)throws Exception{
		String user_id = (String)request.getParameter("user_id");
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s reponseConfrimOverlap' method...");
			logger.debug("request parameter is user_id='" + user_id);
		}
		if(user_id != null && !user_id.equals("")){
			if(!new Boolean(siteUserManager.isOverlap(user_id)).booleanValue()){
				request.setAttribute("result", "사용 가능한 아이디 입니다.");
				request.setAttribute("isSuccess", "yes");
			}else{
				request.setAttribute("result", "중복된 아이디 입니다.");
				request.setAttribute("isSuccess", "no");
			}
		}else{
			List parameters = new ArrayList();
			parameters.add("user_id=" + user_id);
			errorReport(request, parameters);
		}
	}
	
	private void errorReport(HttpServletRequest request, List parameters){
		if (logger.isDebugEnabled()) {
			String wrongParameters = "";
				
			for(int i = 0 ; i < parameters.size() ; i++){
				wrongParameters += ", ";
				wrongParameters += parameters.get(i).toString();
			}
			logger.debug("wrong parameter method='" + request.getParameter("m") + wrongParameters);
			request.setAttribute("isSuccess", "no");
			request.setAttribute("result", "잘못된 파라미터 값 입니다.");
		}
	}
	
	private void errorReport(HttpServletRequest request){
		errorReport(request, null);
	}
}
