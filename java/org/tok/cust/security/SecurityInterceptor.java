package org.tok.cust.security;

import java.security.AccessControlException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tok.view.Talk;
import org.tok.view.TalkActions;
import org.tok.view.administration.PortalConfiguration;
import org.tok.view.security.DefaultSecurityPermission;
import org.tok.view.security.TalkAuthorityManager;
import org.tok.view.security.TalkSecurityPermission;
import org.tok.view.session.SessionManager;
import org.tok.view.sso.TalkSSOManager;

public class SecurityInterceptor extends HandlerInterceptorAdapter 
{
	private final Log   log = LogFactory.getLog(getClass());

	private SessionManager sessionManager;
	protected PortalConfiguration configuration;
	
	public SecurityInterceptor()
	{
		this.configuration = Talk.getConfiguration();
		this.sessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    	throws Exception 
    {
		if (log.isDebugEnabled()) {
			java.util.Enumeration em = request.getParameterNames();
	        for( ; em.hasMoreElements(); ) {
	            String k = (String)em.nextElement();
	            String val = request.getParameter( k );
	            log.debug( "SecurityInterceptor Request : " + k + " = " + val);
	        }
	        log.debug("AuthorityCode=" + TalkAuthorityManager.getInstance().getBinaryAuthority( TalkAuthorityManager.getInstance().getAuthorityCode(request) ));
		}
		
		Map userInfoMap = null;
		String sessionUserId = null;
		HttpSession session = request.getSession();
		boolean isAjaxCall = (request.getParameter("__ajax_call__") != null ) ? true : false;
		if (session == null || (sessionUserId = (String)session.getAttribute(configuration.getString("sso.login.id.key"))) == null) {

			try {
				sessionUserId = TalkSSOManager.getUserId(request, null);
				System.out.println("*** session userId=" + sessionUserId + " from SessionManager");
				if (sessionUserId == null) {
					if( isAjaxCall ) {
						response.setHeader("tok.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
					}
					else {
						response.sendRedirect( request.getContextPath() + configuration.getString("sso.login.page") );
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				if( isAjaxCall ) {
					response.setHeader("tok.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
				}
				else {
					response.sendRedirect( request.getContextPath() + configuration.getString("sso.login.page") );
				}
				return false;
			}
		}
		else {
			userInfoMap = (Map)TalkSSOManager.getUserInfo(request);
			if( userInfoMap == null ) {
				try {
					if( isAjaxCall ) {
						response.setHeader("tok.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
					}
					else {
						response.sendRedirect( request.getContextPath() + configuration.getString("sso.login.page") );
					}
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					if( isAjaxCall ) {
						response.setHeader("tok.ajax.control", request.getContextPath() + configuration.getString("sso.login.page"));
					}
					else {
						response.sendRedirect( request.getContextPath() + configuration.getString("sso.login.page") );
					}
					return false;
				}
			}
			else {
				System.out.println("*** session check userId=" + (String)session.getAttribute(configuration.getString("sso.login.id.key")));
			}
		}
		
		if( configuration.getBoolean("security.portlet.check") == true ) {
			// windowId는 포틀릿 ID이므로 이 값이 있으면 페이지안에 포함된 형태이므로 권한체크를 안한다.
			String windowId = (String)request.getAttribute("windowId");
			if( windowId == null ) {
				//Map userInfoMap = TalkSSOManager.getUserInfo(request);
				if( userInfoMap != null ) {
					TalkSecurityPermission enviewSecurityPermission = (TalkSecurityPermission)userInfoMap.get("enviewSecurityPermission");
					if( enviewSecurityPermission != null ) {
						String requestPath = request.getRequestURI();
						//String requestPath = request.getPathInfo();
						requestPath = requestPath.substring( request.getContextPath().length() );
						System.out.println("*** requestPath=" + requestPath);
						try {
							DefaultSecurityPermission perm = (DefaultSecurityPermission)enviewSecurityPermission.getSecurityPermission("PT");
							perm.checkExtraUrl(requestPath, TalkActions.MASK_VIEW);
		    			}
		                catch (AccessControlException ace)
		                {
		                	System.out.println("*** Access denied [" + requestPath + "]");
		                	if( isAjaxCall ) {
								response.setHeader("tok.ajax.control", request.getContextPath() + configuration.getString("permission.access.error.page"));
							}
							else {
								response.sendRedirect( request.getContextPath() + configuration.getString("permission.access.error.page") );
							}
		                }
					}
				}
			}
		}
		
		return true;
	}
}
