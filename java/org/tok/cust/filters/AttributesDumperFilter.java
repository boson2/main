package org.tok.cust.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Request 파라미터 덤프 필터. 디버깅용. 운영시에는 반드시 web.xml 에서 제거.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public class AttributesDumperFilter implements Filter {

	private Log console = LogFactory.getLog("console");
    protected FilterConfig filterConfig = null;
    protected boolean ignore = true;

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest hrequest = (HttpServletRequest) request;
	    
	    String uri = hrequest.getRequestURI();
	    if (uri.endsWith(".gif") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".js") || uri.endsWith(".css"))
	    	return;
		

		console.info("********************************************************************************");
		Enumeration ctxtAttrs = ((HttpServletRequest)request).getSession().getServletContext().getAttributeNames();
		Enumeration sessAttrs = ((HttpServletRequest)request).getSession().getAttributeNames();
		Enumeration rqstAttrs = ((HttpServletRequest)request).getAttributeNames();
		
		
		console.info("ServletContext's attributes");
		while(ctxtAttrs.hasMoreElements()) {
			String attr = (String)ctxtAttrs.nextElement();
			if(!(attr.startsWith("org.apache.catalina"))) {
				Object obj = ((HttpServletRequest)request).getSession().getServletContext().getAttribute(attr);
				console.info("\t[" + attr + "] = [" + obj.getClass().getName() + "]"
		                     //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
					         + "[" + obj.toString() + "]");
		
			}
		}
		
		console.info("--------------------------------------------------------------------------------");
		console.info("Sessiion's attributes");
		while(sessAttrs.hasMoreElements()) {
			String attr = (String)sessAttrs.nextElement();
			if(!(attr.startsWith("org.apache.jetspeed.resovler.cache"))) {
				Object obj = ((HttpServletRequest)request).getSession().getAttribute(attr);
				console.info("\t[" + attr + "]=[" + obj.getClass().getName() + "]"
			             //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
						 + "[" + obj.toString() + "]");
			}
		}
		console.info("--------------------------------------------------------------------------------");
		console.info("Request's attributes");
		while(rqstAttrs.hasMoreElements()) {
			String attr = (String)rqstAttrs.nextElement();
			Object obj = ((HttpServletRequest)request).getAttribute(attr);
			console.info("\t[" + attr + "]=[" + obj.getClass().getName() + "]"
		             //+ ( obj instanceof String ? "[" + ((String)obj).toString() + "]" : ""));
					 + "[" + obj.toString() + "]");
		}
		console.info("********************************************************************************");
	
		chain.doFilter(request, response);
	}

    public void init(FilterConfig filterConfig) throws ServletException {

    	this.filterConfig = filterConfig;
        String value = filterConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;
    }
}
