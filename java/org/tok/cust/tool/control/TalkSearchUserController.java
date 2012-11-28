package org.tok.cust.tool.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.cust.tool.model.SimpleUserVo;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;


public class TalkSearchUserController extends MultiActionController {
	
	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	protected String userId;
	protected String langKnd;
	protected String formName;
	
	public TalkSearchUserController() {
		super();
        this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
	
	private String initData(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s initData' method...");
		}
		if( this.langKnd == null ) {
			this.langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		}
		
		if( this.enviewMessages == null ) {
			this.enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		}
		
		if( this.enviewCodeBundle == null ) {
			this.enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		}
		
		formName = (String)request.getParameter("formName");
		
		String loginUrl = Talk.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.cust");
		request.setAttribute("loginUrl", loginUrl);
		
		if(langKnd == null){
			langKnd = "ko";
		}
		HttpSession session = request.getSession(true);
		userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);	
		
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("formName", formName);
		return request.getParameter("m");
		
	}
	
	public ModelAndView searchUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s ajaxSearchUser' method... parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("search")){
				return responseSearch(request);
			}
		}
		return responseForm(request);
	}
	
	protected ModelAndView responseForm(HttpServletRequest request)	throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseForm' method...");
		}
		return new ModelAndView("tool/searchUser");
	}
	
	protected ModelAndView responseSearch(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseSearch' method...");
		}
		String searchName = request.getParameter("searchName");
		int searchType = Integer.parseInt(request.getParameter("searchType"));
		List userList = toolManager.getUserList(searchName, searchType, userId);
		String returnText = "";
		for(int i = 0 ; i < userList.size() ; i++){
			SimpleUserVo user = (SimpleUserVo)userList.get(i);
			returnText += user.toString() + ",";
		}
		request.setAttribute("returnText", returnText);
		return new ModelAndView("tool/ajaxUserResultSet");
	}

}
