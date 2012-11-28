package org.tok.cust.filters;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Request 덤프. 디버깅용 필터. 운영시에는 web.xml에서 반드시 제거.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public final class RequestDumperFilter implements Filter {

	private FilterConfig filterConfig = null;
    private String logLevel = null;
    protected final Log log = LogFactory.getLog(this.getClass());

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (filterConfig == null)
		    return;
	
	    if("nothing".equals(logLevel))
	    	return;
	    
	    if("verySimple".equals(logLevel)) {
	    	if (request instanceof HttpServletRequest) {
	    	    HttpServletRequest hrequest = (HttpServletRequest) request;
	    	    
	    	    String uri = hrequest.getRequestURI();
	    	    if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css")) {
	    	        chain.doFilter(request, response);	
	    	    	return;
	    	    }
	    		
	    	    //log.info("********************************************************************************");
	    	    log.info("\n\n");
	    	    log.info("Request Received " + hrequest.getRequestURI() 
	    	    		+ (hrequest.getQueryString() == null? "" : "?" + hrequest.getQueryString()));
	    		//log.info("********************************************************************************");
	    	    log.info("\n\n");
	    	}
	    	
	        chain.doFilter(request, response);
	        return;
	    }

	    if("simple".equals(logLevel)) {
	    	if (request instanceof HttpServletRequest) {
	    	    HttpServletRequest hrequest = (HttpServletRequest) request;
	    	    
	    	    String uri = hrequest.getRequestURI();
	    	    if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css")) {
	    	        chain.doFilter(request, response);	
	    	    	return;
	    	    }
	    		//log.info("********************************************************************************");
	    		log.info("\n\n");
	    	    log.info("Request Received " + hrequest.getRequestURI() 
	    	    		+ (hrequest.getQueryString() == null? "" : "?" + hrequest.getQueryString()));
	    		//log.info("--------------------------------------------------------------------------------");
	    		Enumeration names = request.getParameterNames();
	    		while (names.hasMoreElements()) {
	    		    String name = (String) names.nextElement();
	    		    String valueStr = "";
	    		    String values[] = request.getParameterValues(name);
	    		    for (int i = 0; i < values.length; i++) {
	    		        if (i > 0) valueStr += ", ";
	    		        valueStr += values[i];
	    		    }
	    		    log.info("         parameter=" + name + "=" + valueStr);
	    		}
	    		log.info("\n\n");
	    		//log.info("********************************************************************************");
	    	}
	    	
	        chain.doFilter(request, response);	
	        return;
	    }
	    
		//log.info("********************************************************************************");
		log.info("Request Received at " + (new Timestamp(System.currentTimeMillis())));
		log.info(" characterEncoding=" + request.getCharacterEncoding());
		log.info("     contentLength=" + request.getContentLength());
		log.info("       contentType=" + request.getContentType());
		log.info("            locale=" + request.getLocale());
		log.info("           locales=");
		Enumeration locales = request.getLocales();
		boolean first = true;
		while (locales.hasMoreElements()) {
		    Locale locale = (Locale) locales.nextElement();
		    if (first)
		        first = false;
		    else
		    	log.info(", ");
		    log.info(locale.toString());
		}
		//log.info("--------------------------------------------------------------------------------");
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
		    String name = (String) names.nextElement();
		    String valueStr = "";
		    String values[] = request.getParameterValues(name);
		    for (int i = 0; i < values.length; i++) {
		        if (i > 0) valueStr += ", ";
		        valueStr += values[i];
		    }
		    log.info("         parameter=" + name + "=" + valueStr);
		}
		//log.info("--------------------------------------------------------------------------------");
		log.info("          protocol=" + request.getProtocol());
		log.info("        remoteAddr=" + request.getRemoteAddr());
		log.info("        remoteHost=" + request.getRemoteHost());
		log.info("            scheme=" + request.getScheme());
		log.info("        serverName=" + request.getServerName());
		log.info("        serverPort=" + request.getServerPort());
		log.info("          isSecure=" + request.isSecure());
	
		//log.info("--------------------------------------------------------------------------------");
		if (request instanceof HttpServletRequest) {
		    HttpServletRequest hrequest = (HttpServletRequest) request;
		    log.info("       contextPath=" + hrequest.getContextPath());
		    Cookie cookies[] = hrequest.getCookies();
	            if (cookies == null)
	                cookies = new Cookie[0];
		    for (int i = 0; i < cookies.length; i++) {
		    	log.info("            cookie=" + cookies[i].getName() + "=" + cookies[i].getValue());
		    }
		    names = hrequest.getHeaderNames();
		    while (names.hasMoreElements()) {
		        String name = (String) names.nextElement();
			String value = hrequest.getHeader(name);
			log.info("            header=" + name + "=" + value);
		    } 
		    log.info("            method=" + hrequest.getMethod());
		    log.info("          pathInfo=" + hrequest.getPathInfo());
		    log.info("       queryString=" + hrequest.getQueryString());
		    log.info("        remoteUser=" + hrequest.getRemoteUser());
		    log.info("requestedSessionId=" + hrequest.getRequestedSessionId());
		    log.info("        requestURI=" + hrequest.getRequestURI());
		    log.info("       servletPath=" + hrequest.getServletPath());
		}
		//log.info("********************************************************************************");
	
		chain.doFilter(request, response);
		
    }


    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException {

    	this.filterConfig = filterConfig;
    	this.logLevel = filterConfig.getInitParameter("logLevel");
    	if( logLevel == null ) logLevel = "simple";
    }


    /**
     * Return a String representation of this object.
     */
    public String toString() {

    	if (filterConfig == null)
    		return ("RequestDumperFilter()");
    	
    	StringBuffer sb = new StringBuffer("RequestDumperFilter(");
    	sb.append(filterConfig);
    	sb.append(")");
    	
    	return (sb.toString());
    }


}

