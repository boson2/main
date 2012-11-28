package org.tok.cust.engine;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.common.UserInfomationHandler;
import org.tok.cust.user.control.SiteUserManager;
import org.tok.view.Talk;
import org.tok.view.engine.TalkGatewayInterceptor;
import org.tok.view.login.LoginConstants;
import org.tok.view.pipeline.PipelineException;
import org.tok.view.pipeline.valve.TalkValve;
import org.tok.view.request.RequestContext;
import org.tok.view.security.UserManager;
import org.tok.view.security.UserServiceDAC;
import org.tok.view.session.SessionManager;
import org.tok.view.statistics.PortalStatistics;


public class TalkSSOValveImpl implements TalkValve
{
	private static Log log = LogFactory.getLog(TalkSSOValveImpl.class);
	
	private UserInfomationHandler userInfomationHandler;
	
	public TalkSSOValveImpl()
	{
		this.userInfomationHandler = (UserInfomationHandler)Talk.getComponentManager().getComponent("org.tok.cust.common.UserInfomationHandler");
	}
	
	/**
     * Initialize the valve before using in a pipeline.
     */
    public void initialize() throws PipelineException
    {
    	
    }
	
	public boolean invoke(RequestContext context) throws PipelineException	
	{
		log.info("[ TalkSSOValveImpl ] invoke() start");
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		String encoding = request.getCharacterEncoding();
		
		if( log.isDebugEnabled() ) { 
	        log.debug("Encoding: " + encoding);
	    	java.util.Enumeration em = request.getParameterNames();
	        for( ; em.hasMoreElements(); ) {
	            String k = (String)em.nextElement();
	            String val = request.getParameter( k );
	            //String decVal = URLDecoder.decode(val, "UTF-8");
	            //String decdecVal = URLDecoder.decode(decVal, "UTF-8");
	            //log.debug( "TalkServlet Request : " + k + " = " + val + "," + decVal + "," + decdecVal);
	            log.debug( "TalkServlet Request : " + k + " = " + val);
	        }
	        
	        log.debug("*** TalkServlet: pathInfo=" + request.getPathInfo());
	        log.debug("*** TalkServlet: queryString=" + request.getQueryString());
	        log.debug("*** TalkServlet: requestURI=" + request.getRequestURI());
	        log.debug("*** TalkServlet: requestURL=" + request.getRequestURL());
		}
        
        try {
	        HttpSession session = request.getSession();
	        //System.out.println("######## session=" + session);
	        if( session != null ) {
		        String userId = (String)session.getAttribute(LoginConstants.SSO_LOGIN_ID);
		        Map userInfoMap = (Map)session.getAttribute(LoginConstants.SSO_LOGIN_INFO);
		        //System.out.println("######## userId=" + userId + ", userInfoMap=" + userInfoMap);
		        if( userId != null && "guest".equals(userId)==false && (userInfoMap==null || "guest".equals((String)userInfoMap.get("user_id"))) ) 
		        {
		        	userInfoMap = this.userInfomationHandler.createUserInfomation(request, response, userId);
		        	log.info("[ TalkSSOValveImpl ] Acquired userinfo again from DB.");
		        }
	        }
        }
        catch(Exception e) {
        	e.printStackTrace();
        	return false;
        }
        
        log.info("[ TalkSSOValveImpl ] invoke() end");
        return true;
	}
	
	public boolean postHandle(HttpServletRequest request, HttpServletResponse response)
	{
		return true;
	}
}
