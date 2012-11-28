package org.tok.cust.tool.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.cust.tool.model.MultipartFileVo;
import org.tok.cust.util.FileUtil;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.util.TalkLocale;


public class TalkMultipartUploadController extends MultiActionController  {

	private ToolManager toolManager;
	private SessionManager enviewSessionManager;
	private MultiResourceBundle enviewMessages; 
	private CodeBundle enviewCodeBundle;
	
	private String langKnd;
	
	public TalkMultipartUploadController() {
		super();
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	private void initData(HttpServletRequest request) throws Exception {
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
		
		String loginUrl = Talk.getConfiguration().getString("enface.login.page", "http://localhost:8080/enview/user/login.cust");
		request.setAttribute("loginUrl", loginUrl);
		
		if(langKnd == null){
			langKnd = "ko";
		}
		request.setAttribute("langKnd", langKnd);
		logger.debug("initData: Attribute 'langKnd' is '" + langKnd + "'");
	}
	
	public ModelAndView fileUpload(HttpServletRequest request, HttpServletResponse response, MultipartFileVo multipartFile) throws Exception {
		initData(request);
		
		if(multipartFile.getMultipartFile() != null){
			logger.debug("multipartFile's OriginalFileName = " + multipartFile.getMultipartFile().getOriginalFilename());
			HttpSession session = request.getSession(true);
			FileUtil.uploadMultipartFile(Talk.getConfiguration().getString("enface.upload.tool.dir") + (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID), multipartFile.getMultipartFile(), true);
			
			request.setAttribute("fileName", multipartFile.getMultipartFile().getOriginalFilename());
			request.setAttribute("fileSize", new Long(multipartFile.getMBSize()));
			request.setAttribute("fileType", multipartFile.getMultipartFile().getContentType());
		}
		return new ModelAndView("tool/multipart");
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
}
