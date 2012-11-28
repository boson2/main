package org.tok.cust.user.control;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;
import org.tok.view.util.HttpUtil;

public class TalkPasswordChangeController extends SimpleFormController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SiteUserManager enviewSiteUserManager;
	private SessionManager enviewSessionManager;
	
	public TalkPasswordChangeController()
	{
		setCommandName("user");
        setCommandClass(UserVo.class);
        
        this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public SiteUserManager getUserManager() {
		return enviewSiteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.enviewSiteUserManager = siteUserManager;
	}

	protected Map referenceData (HttpServletRequest request, Object command, Errors errors) throws Exception 
	{
		UserVo user = (UserVo)command;
		Map          model   = new HashMap();
		
		return model;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
	 */
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException ex) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering 'User showForm' method...");
		}
		
		//return new ModelAndView(getSuccessView(), model);
		return super.showForm(request, response, ex);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering 'formBackingObject' method...");
		}
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		UserVo user = new UserVo();
		
		List langKndList = (List)enviewCodeBundle.getCodes("PT", "105", 1, true);
		user.setLangKndList( langKndList );
		
		request.setAttribute("langKndList", langKndList);
		request.setAttribute("langKnd", request.getLocale().getLanguage());
		
		String loginUrl = request.getContextPath() + Talk.getConfiguration().getString("sso.login.page");
		String destination = request.getParameter("destination");
		if( destination != null && destination.length()>0 ) {
			loginUrl += "?destination=" + destination;
		}
		request.setAttribute("loginUrl", loginUrl);
		
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		// TODO Auto-generated method stub
		
		if (logger.isInfoEnabled()) {
			logger.info("onBindAndValidate() 호출");
		}
		
		UserVo user = (UserVo)command;
		
		// 아이디 체크
		if (user.getPassword() == null || user.getPassword().equals("")) {
			if( logger.isErrorEnabled() ) {
				logger.error("비밀번호가  입력되지 않았습니다.");
			}				
			
			errors.rejectValue("password", "pt.ev.login.error.hasNotPassword");
		}
		
		// 비밀번호 체크
		if (user.getPasswordNew() == null || user.getPasswordNew().equals("")) {
			if( logger.isErrorEnabled() ) {
				logger.error("새로운 비밀번호가 입력되지 않았습니다.");
			}				
			
			errors.rejectValue("passwordNew", "pt.ev.login.error.hasNotPasswordNew");
		}
		
		// 비밀번호 체크
		if (user.getPasswordConfirm() == null || user.getPasswordConfirm().equals("")) {
			if( logger.isErrorEnabled() ) {
				logger.error("새로운 비밀번호 확인이 입력되지 않았습니다.");
			}				
			
			errors.rejectValue("passwordConfirm", "pt.ev.login.error.hasNotPasswordConfirm");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException exception) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("entering 'User onSubmit' method...");
            logger.debug("User : " + command);
        }
        
        String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );

        UserVo user = (UserVo)command;
        HttpSession session = request.getSession(true);

        String current = user.getCurrent();
        String destination = user.getDestination();
        String userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
        
        if( userId == null ) {
        	destination = Talk.getConfiguration().getString("sso.login.page", "/user/login.cust");
        	return new ModelAndView( "redirect:" + destination );
        }
        
        String password = null;
        String encorderPassword = user.getPassword();
        String passwordNew = null;
        String encorderPasswordNew = user.getPasswordNew();
        String encorderPasswordConfirm = user.getPasswordConfirm();
        
        if( encorderPasswordNew.equals(encorderPasswordConfirm) == false ) {
	        request.setAttribute("errorMessage", enviewMessages.getString("pt.ev.login.label.ErrorCode.9"));
	        return new ModelAndView( this.getFormView() );
        }
        
        if (encorderPassword != null) {
            boolean enableInterEncryption = Talk.getConfiguration().getBoolean("sso.interEncryption", true);
            if( enableInterEncryption ) {
	        	byte[] value = encorderPassword.getBytes();
		        for (int i=0; i<value.length; i++) {
		        	value[i] = (byte)(value[i] + ((i+2) % 7));
		        }
		        password = new String(value);
		        
		        value = encorderPasswordNew.getBytes();
		        for (int i=0; i<value.length; i++) {
		        	value[i] = (byte)(value[i] + ((i+2) % 7));
		        }
		        passwordNew = new String(value);
            }
            else {
            	password = encorderPassword;
            	passwordNew = encorderPasswordNew;
            }
        }
        
        if( user.getLangKnd() != null ) {
        	langKnd = user.getLangKnd();
        }
        
    	try {
    		
    		Map userInfoMap = (Map)enviewSessionManager.getUserData(request);
    		if( userInfoMap == null ) {
    			destination = Talk.getConfiguration().getString("sso.login.page", "/user/login.cust");
    			return new ModelAndView( "redirect:" + destination );
    		}
    		
    		enviewSiteUserManager.changePassword(userInfoMap, userId, password, passwordNew);
    		
    		userInfoMap.put("update_required", "0");

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
	        		destination = "/";
	        		/*
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
	        		*/
	        	}
            }
    	}
		catch(UserException se) 
		{
	        String msgKey = se.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
	        }
	        
	        return new ModelAndView( this.getFormView() );
        }
		
        log.debug("*** destination=" + destination + ", current=" + current);
        int pos = destination.indexOf( request.getContextPath() );
        if( "/".equals(request.getContextPath()) == false && pos > -1 ) {
        	destination = destination.substring(request.getContextPath().length());
        	log.debug("*** destination=" + destination);
        }
        
		return new ModelAndView("redirect:" + destination);
    }
	
	protected void afterPropertiesSetInternal() throws Exception {
        // nothing to do
    }

    public final void afterPropertiesSet() throws Exception {
        afterPropertiesSetInternal();
    }

}
