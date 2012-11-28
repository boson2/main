package org.tok.cust.tool.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tok.cust.tool.model.ZipCodeVo;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;


public class TalkSearchZipController extends SimpleFormController {
	
	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	private String langKnd;
	private String formName;
	
	public TalkSearchZipController() {
		super();
		setCommandName("zipCode");
        setCommandClass(ZipCodeVo.class);
        
        this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName()+ "'s formBackingObject' method...");
		}
		if( this.langKnd == null ) {
			this.langKnd = TalkLocale.getLocale(request, enviewSessionManager);
			System.out.println("***    langKnd=" + langKnd);
		}
		
		if( this.enviewMessages == null ) {
			this.enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		}
		
		if( this.enviewCodeBundle == null ) {
			this.enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		}
		request.setAttribute("langKnd", langKnd);
		formName = (String)request.getParameter("formName");
		request.setAttribute("formName", formName);
		return new ZipCodeVo();
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering TalkSearchZipController 'referenceData' method...");
		}
		return super.referenceData(request, command, errors);
	}

	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering TalkSearchZipController 'showForm' method...");
		}
		request.setAttribute("isFirst", new Boolean(true));
		request.setAttribute("zipCodes",  new ArrayList());
		return super.showForm(request, response, errors, controlModel);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (logger.isDebugEnabled()) {
		    logger.debug("entering TalkSearchZipController 'onSubmit' method...");
		}
		if(langKnd == null){
			langKnd = "ko";
		}
		String dong = request.getParameter("homeAddr1");
		List zipCodes = new ArrayList();
		zipCodes.addAll(toolManager.getZipCodes(dong, langKnd));
		if(zipCodes.isEmpty()){
			request.setAttribute("dong", dong);
		}
		request.setAttribute("isFirst", new Boolean(false));
		request.setAttribute("zipCodes", zipCodes);
		return super.onSubmit(request, response, command, errors);
	}

}
