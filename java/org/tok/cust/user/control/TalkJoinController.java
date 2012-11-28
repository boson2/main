package org.tok.cust.user.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
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

public class TalkJoinController extends AbstractWizardFormController {
	
	private SiteUserManager siteUserManager;
	private SessionManager enviewSessionManager;
	
	private TalkUserValidator enviewUserValidator;
	private int joinLimit;
	private String useProvision; 
		
	private String firstJoinUser;
	
	public TalkJoinController() {
		setCommandName("user");
        setCommandClass(UserVo.class);
        
        this.useProvision = Talk.getConfiguration().getString("enface.join.useProvision");
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
	
	/** 
	 * HTTP GET 요청이 들어올 시 파라미터 값을 저장할 커맨드 객체를 생성.
	 * 최초에 한 번 로딩 된다. 재호출을 하기 위해선
	 * showNewForm(request, response); - processCancel 메소드 구현시 초기화하기위해서 구현. -  
	 * 메소드를 호출해야 하며, 이 경우 제일 첫번째 페이지로 이동하게 된다.
	 **/
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		initializeData(request);
		
		if (logger.isDebugEnabled()) {
		    logger.debug("entering '" + this.getClass().getName() + "'s formBackingObject' method...");
		}
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		request.setAttribute("langKnd", langKnd);
		return user;
	}
	
	private void initializeData(HttpServletRequest request){
		if (logger.isDebugEnabled()) {
		    logger.debug("entering '" + this.getClass().getName() + "'s initializeData' method...");
		}
		firstJoinUser = null;
		request.setAttribute("userList", null);
		request.setAttribute("userListSize", null);
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s referenceData' method... Page [" + page + "] requested");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		Map model = new HashMap();
		switch(page){
			case 0: model.put("joinLimit", new Integer(joinLimit)); break; //start Page  
			case 1: model.put("useProvision", useProvision); break; //provision Page
			case 2: break;	//regNoConfirm page
			case 3: model.put("joinLimit", new Integer(joinLimit)); break;	//idList page
			case 4: model.put("firstJoinUser", firstJoinUser); break;	//confirm page
			case 5: model.put("hpList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false)); break; //userInfo page 
			case 6: break;	//userInfoConfirm page
			case 7: break;	//complete page
			default: break;
		}
		return model;
	}
	
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s postProcessPage' method... page [" + page + "] requested");
		}
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		UserVo user = (UserVo)command;
		try{
			switch(page){
				case 0: break; //start Page
				case 1: break; //provision Page
				case 2:	//regNoConfirm page
					enviewUserValidator.validateRegistryNumber(command, errors);
					List userList = siteUserManager.getUserList(user.getUser_name(), 
							user.getUser_jumin1() + user.getUser_jumin2());
					request.setAttribute("userList", userList);
					request.setAttribute("userListSize", new Integer(userList.size()));
					if( userList.size() > 0 ){
						firstJoinUser = getFirstJoinUser(userList).getUser_id();
					}
					user.setRegNo(user.getUser_jumin1() + user.getUser_jumin2());
					break;
				case 3:	break; //idList page
				case 4:	//confirm page
					siteUserManager.authenticate(firstJoinUser, user.getPassword());
					break;
				case 5:	//userInfo page
					enviewUserValidator.validateUserId(command, errors);
					enviewUserValidator.validateUserHp(command, errors);
					enviewUserValidator.validateUserEmail(command, errors);
					user.setUser_hp(user.getUser_hp1() + "-" + user.getUser_hp2() + "-" + user.getUser_hp3());
					user.setUser_email(user.getUser_email1() + "@" + user.getUser_email2());
					break;
				case 6: break;	//userInfoConfirm page
				case 7:	break;	//complete page
				default: break;
			}
		}catch(UserException e){
			String msgKey = e.getMessageKey();
			if( msgKey != null ){
				//errors.reject 혹은 errors.rejectValue 를 해야 페이지가 안넘어고 해당 페이지를 재로딩한다.
				errors.reject(msgKey); 
				String errorMessage = enviewMessages.getString( msgKey );
				logger.debug("*** errorMessage=" + errorMessage);
				request.setAttribute("errorMessage", errorMessage); 
			}
		}
	}
	
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

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s processCancel' method...");
			logger.debug("*** Join cancel!");
		}
		return showNewForm(request, response);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse reponse, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s processFinish' method...");
		}
		
		UserVo user = (UserVo)command;
		try{
			siteUserManager.join(user); 
			if (logger.isDebugEnabled()) {
				logger.debug("*** Join complete!");
			}
			String loginUrl = Talk.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.cust");
			request.setAttribute("loginUrl", loginUrl);
			return showPage(request, errors, getPageCount()-1);
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
			return showPage(request, errors, getPageCount()-2);
		}
	}
}
