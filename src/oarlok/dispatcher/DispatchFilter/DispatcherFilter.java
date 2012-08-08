package oarlok.dispatcher.DispatchFilter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class DispatcherFilter implements Filter {
	
	public void  init(FilterConfig config) throws ServletException{}
		
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		  
		//Cast to a HttpServlet* object.
		HttpServletRequest httpRequest=(HttpServletRequest)request;
	    HttpServletResponse httpResponse=(HttpServletResponse)response;
	    
	    //Check if it is a valid service call.
	    String service = httpRequest.getServletPath();	    
	    boolean ValidService = new Router().isValidService(service);
	    
	    if((request.getParameter("RequestType")==null) && (!(ValidService) || (httpRequest.getMethod()).equals("GET"))) {	    	
	    	httpResponse.sendError(403, "Permission Denied");
	    	return;
	    } 	    
	    
	    //Pass request back down the filter chain
	    chain.doFilter(request,response);	    		
	}	
	public void destroy() { }	
}
