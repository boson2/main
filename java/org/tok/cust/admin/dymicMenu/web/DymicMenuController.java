
package org.tok.cust.admin.dymicMenu.web;

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
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;
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

import org.tok.board.util.ValidateUtil;
import org.tok.board.vo.CatebaseVO;
import org.tok.cust.admin.dymicMenu.web.DymicMenuForm;
import org.tok.cust.admin.dymicMenu.service.DymicMenuService;
import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.dymicMenu.service.DymicMenuPK;

/**  
 * @Class Name : DymicMenuController.java
 * @Description : 동적메뉴관리 Controller
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class DymicMenuController extends MultiActionController
{
	private final Log   log = LogFactory.getLog(getClass());
	
	private DymicMenuService dymicMenuService;
	private DefaultBeanValidator beanValidator;
	private SessionManager enviewSessionManager;
	
	public void setDymicMenuService(DymicMenuService dymicMenuService)
	{
		this.dymicMenuService = dymicMenuService;
	}
	
	public void setBeanValidator(DefaultBeanValidator beanValidator)
	{
		this.beanValidator = beanValidator;
	}

	public DymicMenuController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	/**
	 * Retrieve dymicMenu list by condition
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception {
		
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
    		Collection results = dymicMenuService.findByPage(searchCondition);
			
			int totalCount = ((ResultSetList)results).getTotalCount();
			String pageIteratorHtmlString = PageNavigationUtil.getInstance().getPageIteratorHtmlString(results.size(), formData.getPageNo(), formData.getPageSize(), totalCount, "DymicMenuManager_SearchForm", "aDymicMenuManager.doPage"); 
			
			
			request.setAttribute("applyFeedList", (List)enviewCodeBundle.getCodes("PT", "119", 1, false));		
			request.setAttribute("writeAuthList", (List)enviewCodeBundle.getCodes("PT", "118", 1, false));		
			request.setAttribute("readAuthList", (List)enviewCodeBundle.getCodes("PT", "118", 1, false));		
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
		return new ModelAndView("dymicMenu/dymicMenuList");
	
	}
	
	/**
	 * Retrieve dymicMenu list by condition
	 * and response with json format
	 */
	public ModelAndView listForAjax(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		Map searchCondition = formData.getSearchCondition();
    		searchCondition.put("langKnd", langKnd);
    		Collection results = dymicMenuService.findByPage(searchCondition);
			
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
		
		return new ModelAndView("dymicMenu/retrieveForAjax");
	}
	
	/**
	 * Acquire dymicMenu detail information
	 * and response with json format
	 */
	public ModelAndView detailForAjax(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception 
	{
		String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
		CodeBundle enviewCodeBundle = TalkCodeManager.getInstance().getBundle( langKnd );
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		DymicMenuVO dymicMenuVO = dymicMenuService.detail(formData.getPrimaryKey(), true);
    		
			formData.setResultStatus("success");
    		formData.setFailureReason("");
    		
    		request.setAttribute("inform", formData);
    		request.setAttribute("dymicMenu", dymicMenuVO);
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
		
		return new ModelAndView("dymicMenu/detailForAjax");
	}
	
	/**
	 * Add new dymicMenu information
	 * and response with json format
	 */
	public ModelAndView addForAjax(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		formData.setRequest(request);
			DymicMenuVO dymicMenuVO = formData.getUpdateObject();
			
			boolean exist = dymicMenuService.exist(dymicMenuVO);
			if( exist == true ) {
				BaseException be = new BaseException("pt.ev.error.Duplicated");
				throw be;
			}
			
    		dymicMenuService.insert(dymicMenuVO);
    		
			DymicMenuPK dymicMenuPK = dymicMenuVO.getPrimaryKey();
    		buffer.append("{");
    		buffer.append("\"Status\": \"success\"");
		
			buffer.append(",").append("\"menuId\": \"").append( dymicMenuPK.getMenuId() ).append("\"");
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
	 * Modify dymicMenu information
	 * and response with json format
	 */
	public ModelAndView updateForAjax(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		formData.setRequest(request);
    		DymicMenuVO dymicMenuVO = formData.getUpdateObject();
    		dymicMenuService.update(dymicMenuVO);
    		
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
	 * Remove dymicMenu information
	 * and response with json format
	 */
	public ModelAndView removeForAjax(HttpServletRequest request, HttpServletResponse response, DymicMenuForm formData) throws Exception 
	{
		
		StringBuffer buffer = new StringBuffer();

    	try {
    		formData.setRequest(request);
			List keyList = formData.getRemoveKeyList();
			for(int i=0; i<keyList.size(); i++) {
				DymicMenuPK dymicMenuPK = (DymicMenuPK)keyList.get(i);
				dymicMenuPK.setRequest(request);
			}
			dymicMenuService.delete(formData.getRemoveKeyList(), false);
			
    		
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
			
			dymicMenuService.changeTreeOrder( paramMap );
    		
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
	/*********************************************************************************************************
	 * 
	 *********************************************************************************************************/
	public ModelAndView mainMenu (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		ConnectionContext connCtxt = null;
		
		try {
			connCtxt = new ConnectionContextForRdbms( true );

			List menuList = dymicMenuService.getMainMenuList (request, connCtxt);
			List cafeList = dymicMenuService.getMainCafeList (request, connCtxt);
			
			model.put ("menuList", menuList);
			model.put ("cafeList", cafeList);

			connCtxt.commit();
		} catch (BaseException be) {
    		rollback (connCtxt);
			be.printStackTrace();
	        String errorMessage = be.getMessage();
	        String msgKey = be.getMessageKey();
	        if( msgKey != null ) {
	        	String langKnd = TalkLocale.getLocale(request, enviewSessionManager);
	    		MultiResourceBundle enviewMessages = TalkMultiResourceManager.getInstance().getBundle( langKnd );
	        	errorMessage = enviewMessages.getString( msgKey );
	        }
	        
			StringBuffer buffer = new StringBuffer();
	        buffer.append("{");
    		buffer.append("\"Status\": \"fail\",");
    		buffer.append("\"Reason\": \"").append( errorMessage ).append("\"");
    		buffer.append("}");
	        response.setContentType("text/json;charset=UTF-8");
	        response.getWriter().print(buffer.toString());
    	} finally {
    		release (connCtxt);
    	}
    	return new ModelAndView ("dymicMenu/mainMenu", model);		
	}

	protected void rollback (ConnectionContext connCtxt) {
		if (connCtxt != null) connCtxt.rollback();
	}
	protected void release (ConnectionContext connCtxt) {
		if (connCtxt != null) connCtxt.release();
	}
}
