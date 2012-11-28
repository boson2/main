package org.tok.cust.tool.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;


public class TalkAjaxSearchZipController extends MultiActionController {
	
	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	private String langKnd;
	private String formName;
	
	public TalkAjaxSearchZipController() {
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
		
		request.setAttribute("langKnd", langKnd);
		request.setAttribute("formName", formName);
		return request.getParameter("m");
		
	}
	
	public ModelAndView ajaxSearchZip(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s ajaxSearchZip' method... parameter method is '" + request.getParameter("m") + "'");
		}
		String method = initData(request);
		if(method != null && !method.equals("")){
			if(method.equals("search")){
				return responseSearch(request);
			}
			else{
				return responseForm(request);
			}
		}
		else{
			return new ModelAndView("tool/ajaxSearchZip");
		}
		
	}
	
	protected ModelAndView responseForm(HttpServletRequest request)	throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseForm' method...");
		}
		return new ModelAndView("tool/ajaxSearchZip");
	}
	
	protected ModelAndView responseSearch(HttpServletRequest request) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s responseSearch' method...");
		}
		String dong = request.getParameter("homeAddr1");
		List zipCodes = new ArrayList();
		zipCodes.addAll(toolManager.getZipCodes(dong, langKnd));
		if(zipCodes.isEmpty()){
			request.setAttribute("dong", dong);
		}
		request.setAttribute("zipCodes", zipCodes);
		return new ModelAndView("tool/ajaxZipResultSet");
	}

}
