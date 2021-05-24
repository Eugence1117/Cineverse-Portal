package com.ms.error;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AjaxRequestHandlingFilter implements Filter {
	
	public static Logger log = LogManager.getLogger(AjaxRequestHandlingFilter.class);
	
    private int errorCode = 401;
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                log.info("Principal: " + principal);
                if("anonymousUser".equals(principal.toString())){
                    resp.setStatus(this.errorCode);
                    resp.sendError(this.errorCode, "Ajax time out");
                    resp.addHeader("SESSION_EXPIRED","true");
                    SecurityContextHolder.clearContext();
                    throw new AccessDeniedException("Ajax request time out.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }
    }
 
    public void init(FilterConfig config) throws ServletException {
    }
 
    public void destroy() {
    }
 
}