
package org.tok.cust.admin.userpass.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.exception.BaseException;
import org.tok.view.login.LoginConstants;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.session.SessionManager;
import org.tok.view.sso.TalkSSOManager;
import org.tok.view.util.PageNavigationUtil;
import org.tok.view.util.TalkLocale;
import org.tok.view.util.HttpUtil;
import org.tok.view.util.ResultSetList;

import org.tok.cust.admin.userpass.web.UserpassForm;
import org.tok.cust.admin.userpass.service.UserpassService;
import org.tok.cust.admin.userpass.service.UserpassVO;
import org.tok.cust.admin.userpass.service.UserpassPK;

/**  
 * @Class Name : UserpassController.java
 * @Description : 개인정보 Controller
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	
	private UserpassService userpassService;
	private DefaultBeanValidator beanValidator;
	private SessionManager enviewSessionManager;
	
	public void setUserpassService(UserpassService userpassService)
	{
		this.userpassService = userpassService;
	}
	
	public void setBeanValidator(DefaultBeanValidator beanValidator)
	{
		this.beanValidator = beanValidator;
	}

	public UserpassController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	/**
	 * Retrieve userpass list by condition
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception {
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
    	try {
			Map userInfoMap = TalkSSOManager.getUserInfo(request);
 			if( userInfoMap == null ) {
            	log.debug("*** You have to login !!!");
            	throw new BaseException("You have to login !!!");
            }
			else {
				request.setAttribute("evSecurityCode", (String)userInfoMap.get("evSecurityCode"));
			}
			
			Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = userpassService.findByPage(searchCondition);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getInstance().getPageIteratorHtmlString(results.size(), formData.getPageNo(), formData.getPageSize(), totalCount, "UserpassManager_SearchForm", "aUserpassManager.doPage"); 
			
			//System.out.println("########### gradeCdList=" + (List)enviewCodeBundle.getCodes("PT", "118", 2, 2, true));
			request.setAttribute("gradeCdList", (List)enviewCodeBundle.getCodes("PT", "118", 2, 2, true));		
			request.setAttribute("emailAddrList", (List)enviewCodeBundle.getCodes("PT", "018", 1, false));		
			request.setAttribute("homeTelList", (List)enviewCodeBundle.getCodes("PT", "003", 1, false));		
			request.setAttribute("mobileTelList", (List)enviewCodeBundle.getCodes("PT", "002", 1, false));		
    		request.setAttribute("resultMessage", enviewMessages.getString("pt.ev.message.ResultSize", String.valueOf(totalCount)));
			request.setAttribute("langKnd", langKnd);
			request.setAttribute("pageIterator", pageIteratorHtmlString);
			request.setAttribute("messages", enviewMessages);
    		request.setAttribute("results", results);
			request.setAttribute("isMaster", "true");
			if( request.getAttribute("searchCondition") == null ) {
				request.setAttribute("searchCondition", formData.getSearchCondition());
			}
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
	        	String errorMessage = enviewMessages.getString( msgKey );
		        request.setAttribute("errorMessage", errorMessage);
		        log.debug("*** errorMessage=" + errorMessage);
	        }
        }
		return new ModelAndView("userpass/userpassList");
	
	}
	
	/**
	 * Retrieve userpass list by condition
	 * and response with json format
	 */
	public ModelAndView listForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = userpassService.findByPage(searchCondition);
			
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		formData.setTotalSize( ((ResultSetList)results).getTotalCount() );
    		formData.setResultSize( results.size() );
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("results", results);
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String msgKey = be.getMessageKey();
	        String errorMessage = be.getMessage();
	        if( msgKey != null ) {
	        	errorMessage = enviewMessages.getString( msgKey );
		        //log.debug("*** errorMessage=" + errorMessage);
	        }
	        request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
	        formData.setResultStatus("fail");
    		formData.setFailureReason( "[" + be.getClass() + "] " + errorMessage );
    		request.setAttribute("inform", formData);
        }
		
		return new ModelAndView("userpass/retrieveForAjax");
	}
	
	/**
	 * Retrieve userpass list by condition
	 * and response with json format
	 */
	public ModelAndView listNewForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = userpassService.findNewByPage(searchCondition);
			
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		formData.setTotalSize( ((ResultSetList)results).getTotalCount() );
    		formData.setResultSize( results.size() );
    		
    		System.out.println("###### totalSie=" + formData.getTotalSize());
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("results", results);
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String msgKey = be.getMessageKey();
	        String errorMessage = be.getMessage();
	        if( msgKey != null ) {
	        	errorMessage = enviewMessages.getString( msgKey );
		        //log.debug("*** errorMessage=" + errorMessage);
	        }
	        request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
	        formData.setResultStatus("fail");
    		formData.setFailureReason( "[" + be.getClass() + "] " + errorMessage );
    		request.setAttribute("inform", formData);
        }
		
		return new ModelAndView("userpass/retrieveForAjax");
	}
	
	/**
	 * Acquire userpass detail information
	 * and response with json format
	 */
	public ModelAndView detailForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		UserpassVO userpassVO = userpassService.detail(formData.getPrimaryKey(), true);
    		
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("userpass", userpassVO);
    		//System.out.println("########## guide=" + userpassVO.getGuide());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String msgKey = be.getMessageKey();
	        String errorMessage = be.getMessage();
	        if( msgKey != null ) {
	        	errorMessage = enviewMessages.getString( msgKey );
		        //log.debug("*** errorMessage=" + errorMessage);
	        }
	        request.setAttribute("errorMessage", "[" + be.getClass() + "] " + errorMessage);
	        formData.setResultStatus("fail");
    		formData.setFailureReason( "[" + be.getClass() + "] " + errorMessage );
    		request.setAttribute("inform", formData);
        }
		
		return new ModelAndView("userpass/detailForAjax");
	}
	
	/**
	 * Add new userpass information
	 * and response with json format
	 */
	public ModelAndView addForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			UserpassVO userpassVO = formData.getUpdateObject();
			
			boolean exist = userpassService.exist(userpassVO);
			if( exist == true ) {
				BaseException be = new BaseException("pt.ev.error.Duplicated");
				throw be;
			}
			
    		userpassService.insert(userpassVO);
    		
			UserpassPK userpassPK = userpassVO.getPrimaryKey();
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
		
			buffer.append(",").append("\"userId\": \"").append( userpassPK.getUserId() ).append("\"");
    		buffer.append("}");
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
        }
		
		return null; //new ModelAndView();
	}

	/**
	 * Modify userpass information
	 * and response with json format
	 */
	public ModelAndView updateForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
		userpassService.update(formData.getUpdateObject());
    		
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
    		buffer.append("}");
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
        }
		
		return null; //new ModelAndView();
	}

	/**
	 * Remove userpass information
	 * and response with json format
	 */
	public ModelAndView removeForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			
			userpassService.delete(formData.getRemoveKeyList(), false);
    		
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
    		buffer.append("}");
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
        }
		
		return null; //new ModelAndView();
	}
	
	/**
	 * AccessDeny userpass information
	 * and response with json format
	 */
	public ModelAndView accessDenyForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			
			userpassService.accessDeny(formData.getUpdateKeyList(), false);
    		
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
    		buffer.append("}");
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
        }
		
		return null; //new ModelAndView();
	}
	
	/**
	 * Approve userpass information
	 * and response with json format
	 */
	public ModelAndView approveForAjax(HttpServletRequest request, HttpServletResponse response, UserpassForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			
			userpassService.approve(formData.getUpdateKeyList(), false);
    		
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
    		buffer.append("}");
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	}
		catch(BaseException be) 
		{
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
				String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
				MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
        }
		
		return null; //new ModelAndView();
	}
	
}
