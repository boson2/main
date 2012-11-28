
package org.tok.cust.admin.grade.web;

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
import org.tok.view.admin.codebase.service.CodebasePK;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.codebase.Codebases;
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

import org.tok.cust.admin.grade.web.GradeForm;
import org.tok.cust.admin.grade.service.GradeService;
import org.tok.cust.admin.grade.service.GradeVO;
import org.tok.cust.admin.grade.service.GradePK;

/**  
 * @Class Name : GradeController.java
 * @Description : 등급관리 Controller
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradeController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	
	private GradeService gradeService;
	private DefaultBeanValidator beanValidator;
	private SessionManager enviewSessionManager;
	private Codebases enviewCodebase;
	
	public void setGradeService(GradeService gradeService)
	{
		this.gradeService = gradeService;
	}
	
	public void setBeanValidator(DefaultBeanValidator beanValidator)
	{
		this.beanValidator = beanValidator;
	}

	public GradeController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
		this.enviewCodebase = (Codebases)Talk.getComponentManager().getComponent("org.tok.view.codebase.Codebases");
	}
	
	/**
	 * Retrieve grade list by condition
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception {
		
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
    		Collection results = gradeService.findByPage(searchCondition);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getInstance().getPageIteratorHtmlString(results.size(), formData.getPageNo(), formData.getPageSize(), totalCount, "GradeManager_SearchForm", "aGradeManager.doPage"); 
			
			
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
		return new ModelAndView("grade/gradeList");
	
	}
	
	/**
	 * Retrieve grade list by condition
	 * and response with json format
	 */
	public ModelAndView listForAjax(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = gradeService.findByPage(searchCondition);
			
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
		
		return new ModelAndView("grade/retrieveForAjax");
	}
	
	/**
	 * Acquire grade detail information
	 * and response with json format
	 */
	public ModelAndView detailForAjax(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		GradeVO gradeVO = gradeService.detail(formData.getPrimaryKey(), true);
    		
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("grade", gradeVO);
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
		
		return new ModelAndView("grade/detailForAjax");
	}
	
	/**
	 * Add new grade information
	 * and response with json format
	 */
	public ModelAndView addForAjax(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			GradeVO gradeVO = formData.getUpdateObject();
			
			boolean exist = gradeService.exist(gradeVO);
			if( exist == true ) {
				BaseException be = new BaseException("pt.ev.error.Duplicated");
				throw be;
			}
			
    		gradeService.insert(gradeVO);
    		
    		enviewCodebase.syncCache("ko", "PT", "118", "insert");
    		
			GradePK gradePK = gradeVO.getPrimaryKey();
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
		
			buffer.append(",").append("\"code\": \"").append( gradePK.getCode() ).append("\"");
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
	 * Modify grade information
	 * and response with json format
	 */
	public ModelAndView updateForAjax(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		gradeService.update(formData.getUpdateObject());
    		enviewCodebase.syncCache("ko", "PT", "118", "update");
    		
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
	 * Remove grade information
	 * and response with json format
	 */
	public ModelAndView removeForAjax(HttpServletRequest request, HttpServletResponse response, GradeForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			
			gradeService.delete(formData.getRemoveKeyList(), false);
			enviewCodebase.syncCache("ko", "PT", "118", "remove");
    		
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
	 * Change page order in children
	 * and response with json format
	 */
	public ModelAndView changeOrderForAjax(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		StringBuffer buffer = new StringBuffer();

    	try {
			Map paramMap = new HashMap();
			String id = request.getParameter( "id" );
			if( id != null && id.length() > 0 ) {
				paramMap.put("id", id);
			}
			else {
				paramMap.put("id", "0");
			}
			
			String toDown = request.getParameter( "toDown" );
			if( toDown != null && toDown.length() > 0 ) {
				paramMap.put("toDown", toDown);
			}
			else {
				paramMap.put("toDown", "true");
			}
			
			gradeService.changeOrder( paramMap );
    		
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
