package org.tok.cust.user.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tok.cust.user.model.UserVo;


public class TalkConfirmController extends SimpleFormController {
	
	private SiteUserManager siteUserManager;
	
	public TalkConfirmController() {
		setCommandName("user");
        setCommandClass(UserVo.class);
        
	}
	
	public SiteUserManager getUserManager() {
		return siteUserManager;
	}

	public void setUserManager(SiteUserManager siteUserManager) {
		this.siteUserManager = siteUserManager;
	}
	
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		String user_id = (String)request.getParameter("check_id");
		if(user_id != null && !user_id.equals("")){
			request.setAttribute("isOverlap", new Boolean(siteUserManager.isOverlap(user_id)));
			request.setAttribute("check_id", user_id);
		}
		return new ModelAndView(getFormView());
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		UserVo user = (UserVo)command;
		if(user.getUser_id() != null && !user.getUser_id().equals("")){
			request.setAttribute("isOverlap", new Boolean(siteUserManager.isOverlap(user.getUser_id())));
			request.setAttribute("check_id", user.getUser_id());
		}
		return new ModelAndView(getSuccessView());
	}

}
