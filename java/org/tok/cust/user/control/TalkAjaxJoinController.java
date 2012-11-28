package org.tok.cust.user.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
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

public class TalkAjaxJoinController extends MultiActionController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	private String firstJoinUser;
	private String loginUrl;
	private UserVo user;
	
	private int joinLimit;
	public TalkAjaxJoinController() {
        
		user = new UserVo();
		loginUrl = Talk.getConfiguration().getString("enface.login.page", "http://localhost:8081/enview/user/login.cust");
        this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
        this.joinLimit = Integer.parseInt(Talk.getConfiguration().getString("enface.join.limit"));
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
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		request.setAttribute("langKnd", langKnd);
		return request.getParameter("m");
	}
	
	public ModelAndView ajaxJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s ajaxJoin' method... parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("start")){
				return responseStart(request);
			}
			else if(method.equals("provision")){
				return responseProvision(request);
			}
			else if(method.equals("provisionText")){
				return responseProvisionText(request);
			}
			else if(method.equals("regNoConfirm")){
				return responseRegNoConfirm(request);
			}
			else if(method.equals("idList")){
				return responseIdList(request);
			}	
			else if(method.equals("passwordConfirm")){
				return responsePasswordConfirm(request);
			}
			else if(method.equals("userInfo")){
				return responseUserInfo(request);
			}
			else if(method.equals("userInfoConfirm")){
				return responseUserInfoConfirm(request);
			}
			else if(method.equals("userInfoChange")){
				return responseUserInfoChange(request);
			}
			else if(method.equals("complete")){
				return responseComplete(request);
			}
		}
		else{
			return responseJoin(request);
		}
		return null;
	}
	
	private ModelAndView responseJoin(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseJoin' method...");
		}
		return new ModelAndView("user/ajax_join/join");
	}
	
	private ModelAndView responseStart(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseStart' method...");
		}
		request.setAttribute("loginUrl", loginUrl); 
		request.setAttribute("joinLimit", new Integer(joinLimit));
		return new ModelAndView("user/ajax_join/start");
	}
	
	private ModelAndView responseProvision(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseProvision' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("useProvision", Talk.getConfiguration().getString("enface.join.useProvision"));
		return new ModelAndView("user/ajax_join/provision");
	}
	
	private ModelAndView responseProvisionText(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseProvisionText' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		request.setAttribute("langKnd", langKnd);
		return new ModelAndView("user/ajax_join/provisionText");
	}
	
	private ModelAndView responseRegNoConfirm(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseRegNoConfirm' method...");
		}
		return new ModelAndView("user/ajax_join/regNoConfirm");
	}
	
	private ModelAndView responseIdList(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseIdList' method...");
		}
		user.setUser_name((String)request.getParameter("user_name"));
		user.setUser_jumin1((String)request.getParameter("user_jumin1"));
		user.setUser_jumin2((String)request.getParameter("user_jumin2"));
		user.setRegNo(user.getUser_jumin1() + user.getUser_jumin2());
		List userList = siteUserManager.getUserList(user.getUser_name(), user.getRegNo());
		request.setAttribute("userList", userList);
		request.setAttribute("userListSize", new Integer(userList.size()));
		if( userList.size() > 0 ){
			firstJoinUser = getFirstJoinUser(userList).getUser_id();
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("joinLimit", new Integer(joinLimit));
		return new ModelAndView("user/ajax_join/idList");
	}
	
	private ModelAndView responsePasswordConfirm(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responsePasswordConfirm' method...");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("firstJoinUser", firstJoinUser);
		return new ModelAndView("user/ajax_join/passwordConfirm");
	}
	
	private ModelAndView responseUserInfo(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfo' method...");
		}
		request.setAttribute("user", user);
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		request.setAttribute("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false));
		return new ModelAndView("user/ajax_join/userInfo");
	}
	
	private ModelAndView responseUserInfoConfirm(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfoConfirm' method...");
		}
		String user_id = request.getParameter("user_id");
		String password = request.getParameter("password");
		String homeZip1 = request.getParameter("homeZip1");
		String homeZip2 = request.getParameter("homeZip2");
		String homeAddr1 = request.getParameter("homeAddr1");
		String homeAddr2 = request.getParameter("homeAddr2");
		String user_hp1 = request.getParameter("user_hp1");
		String user_hp2 = request.getParameter("user_hp2");
		String user_hp3 = request.getParameter("user_hp3");
		String user_email1 = request.getParameter("user_email1");
		String user_email2 = request.getParameter("user_email2");
		
		user.setUser_id(user_id);
		user.setPassword(password);
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
			return responseUserInfoChange(request);
		}
		return new ModelAndView("user/ajax_join/userInfoConfirm");
	}
	
	private ModelAndView responseUserInfoChange(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseUserInfoChange' method...");
		}
		request.setAttribute("user", user);
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		request.setAttribute("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false));
		return new ModelAndView("user/ajax_join/userInfoChange");
	}
	
	private ModelAndView responseComplete(HttpServletRequest request) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseComplete' method...");
		}
		request.setAttribute("user", user);
		try{
			siteUserManager.join(user); 
			System.out.println(user);
			if (logger.isDebugEnabled()) {
				logger.debug("*** Join complete!");
			}
			//가입 완료 성공 시 complete 페이지로 이동
			return new ModelAndView("user/ajax_join/complete");
		}catch(UserException ue){
			//실패했을 경우 에러메시지 설정 후 같은 페이지 재로딩
			alertError(request, ue);
			return responseUserInfoConfirm(request);
		}
	}
	
	/*
	 * 최초 가입한 아이디를 추출
	 */
	private UserVo getFirstJoinUser(List userList) {
		int cursor = 0;
		if(!userList.isEmpty()){
			for(int i = 0 ; i < userList.size() ; i++){
				if(((UserVo)userList.get(cursor)).getRegDatim().after(((UserVo)userList.get(i)).getRegDatim())){
					cursor = i;
				}
			}
		}
		return (UserVo)userList.get(cursor);
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
