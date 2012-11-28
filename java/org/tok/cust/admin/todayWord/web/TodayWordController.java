
package org.tok.cust.admin.todayWord.web;

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

import org.tok.cust.admin.todayWord.web.TodayWordForm;
import org.tok.cust.admin.todayWord.service.TodayWordService;
import org.tok.cust.admin.todayWord.service.TodayWordVO;
import org.tok.cust.admin.todayWord.service.TodayWordPK;

/**  
 * @Class Name : TodayWordController.java
 * @Description : 오늘의 말씀 Controller
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	
	private TodayWordService todayWordService;
	private DefaultBeanValidator beanValidator;
	private SessionManager enviewSessionManager;
	
	public void setTodayWordService(TodayWordService todayWordService)
	{
		this.todayWordService = todayWordService;
	}
	
	public void setBeanValidator(DefaultBeanValidator beanValidator)
	{
		this.beanValidator = beanValidator;
	}

	public TodayWordController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	/**
	 * Retrieve todayWord list by condition
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception {
		
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
    		Collection results = todayWordService.findByPage(searchCondition);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getPageIteratorHtmlString(results.size(), formData.getPageNo(), formData.getPageSize(), totalCount, "TodayWordManager_SearchForm", "aTodayWordManager.doPage"); 
			
			
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
		return new ModelAndView("todayWord/todayWordList");
	
	}
	
	/**
	 * Retrieve todayWord list by condition
	 * and response with json format
	 */
	public ModelAndView listForAjax(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = todayWordService.findByPage(searchCondition);
			
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		formData.setTotalSize( ((ResultSetList)results).getTotalCount() );
    		formData.setResultSize( results.size() );
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("results", results);
			/*
    		buffer.append("{");
    		buffer.append("\"Reason\": \"success\",");
    		buffer.append("\"TotalSize\": \"").append( ((ResultSetList)results).getTotalCount() ).append("\",");
    		buffer.append("\"ResultSize\": \"").append( results.size() ).append("\",");
    		buffer.append("\"Data\":");
    		buffer.append("[");
    		int idx = 0;
    		for(Iterator it=results.iterator(); it.hasNext(); idx++) {
    			TodayWordVO aTodayWordVO = (TodayWordVO)it.next();
    			if( idx > 0 ) {
    				buffer.append(",");
    			}
    			buffer.append(" {");
		 
				buffer.append("\"wordId\": \""); buffer.append( aTodayWordVO.getWordId() ); buffer.append("\" ");buffer.append(", "); 
				buffer.append("\"title\": \""); buffer.append( aTodayWordVO.getTitle() ); buffer.append("\" ");buffer.append(", "); 
				buffer.append("\"content\": \""); buffer.append( aTodayWordVO.getContent() ); buffer.append("\" ");buffer.append(", "); 
				buffer.append("\"startDate\": \""); buffer.append( aTodayWordVO.getStartDate() ); buffer.append("\" ");
    			buffer.append("}");
    		}
    		buffer.append("]}");
    		
    		log.debug("*** result=" + buffer.toString());
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
			*/
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
		
		return new ModelAndView("todayWord/retrieveForAjax");
	}
	
	/**
	 * Retrieve todayWord list by condition for chooser
	 */
	public ModelAndView listForChooser(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception {
		
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = todayWordService.findByPage(searchCondition);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getPageIteratorHtmlString(results.size(), 1, 10, totalCount, "TodayWordManager_SearchForm", "aTodayWordManager.doPage"); 
			
			
    		request.setAttribute("resultMessage", enviewMessages.getString("pt.ev.message.ResultSize", String.valueOf(totalCount)));
			request.setAttribute("langKnd", langKnd);
			request.setAttribute("pageIterator", pageIteratorHtmlString);
			request.setAttribute("messages", enviewMessages);
    		request.setAttribute("results", results);
			request.setAttribute("isMaster", "true");
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
		return new ModelAndView("todayWord/todayWordChooser");
	}
	
	/**
	 * Acquire todayWord detail information
	 */
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
    	try {
    		TodayWordVO aTodayWordVO = todayWordService.detail(formData.getPrimaryKey(), true);
			
			
			request.setAttribute("langKnd", langKnd);
			request.setAttribute("messages", enviewMessages);
			request.setAttribute("isCreate", "false");
			request.setAttribute("aTodayWordVO", aTodayWordVO);
			request.setAttribute("searchCondition", formData.getSearchCondition());
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
		
		return new ModelAndView("todayWord/todayWordDetail");
	}
	
	
	
	/**
	 * Acquire todayWord detail information
	 * and response with json format
	 */
	public ModelAndView detailForAjax(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		TodayWordVO todayWordVO = todayWordService.detail(formData.getPrimaryKey(), true);
    		
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("todayWord", todayWordVO);
			/*
    		buffer.append("{");
		 
			buffer.append("\"wordId\": \""); buffer.append( aTodayWordVO.getWordId() ); buffer.append("\" ");buffer.append(", "); 
			buffer.append("\"title\": \""); buffer.append( aTodayWordVO.getTitle() ); buffer.append("\" ");buffer.append(", "); 
			buffer.append("\"content\": \""); buffer.append( aTodayWordVO.getContent() ); buffer.append("\" ");buffer.append(", "); 
			buffer.append("\"startDate\": \""); buffer.append( aTodayWordVO.getStartDate() ); buffer.append("\" ");
    		buffer.append("}");
    		
    		log.debug("*** result=" + buffer.toString());
    		
    		response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
			*/
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
		
		return new ModelAndView("todayWord/detailForAjax");
	}
	
	/**
	 * Add new todayWord information
	 * and response with json format
	 */
	public ModelAndView addForAjax(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			TodayWordVO todayWordVO = formData.getUpdateObject();
			
			boolean exist = todayWordService.exist(todayWordVO);
			if( exist == true ) {
				BaseException be = new BaseException("pt.ev.error.Duplicated");
				throw be;
			}
			
    		todayWordService.insert(todayWordVO);
    		
			TodayWordPK todayWordPK = todayWordVO.getPrimaryKey();
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
		
			buffer.append(",").append("\"wordId\": \"").append( todayWordPK.getWordId() ).append("\"");
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
	 * Modify todayWord information
	 * and response with json format
	 */
	public ModelAndView updateForAjax(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
		todayWordService.update(formData.getUpdateObject());
    		
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
	 * Remove todayWord information
	 * and response with json format
	 */
	public ModelAndView removeForAjax(HttpServletRequest request, HttpServletResponse response, TodayWordForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
			
			todayWordService.delete(formData.getRemoveKeyList(), false);
    		
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
