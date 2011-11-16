/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Milan
 */
@WebFilter (filterName = "LoginFilter", urlPatterns = {"/secure/*"})
public class LoginFilter implements Filter {
	private FilterConfig filterConfig = null;

	/**
	 * Init method for this filter.
	 */
	@Override
	public void init (FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * Redirects user to login page if he's got no business in here.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param chain The filter chain we are processing
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	@Override
	public void doFilter (ServletRequest request, ServletResponse response,
						  FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String username = (String) httpServletRequest.getSession ().getAttribute ("username");
		
		if (username == null || username.isEmpty ()) {
			httpServletResponse.sendRedirect (httpServletRequest.getContextPath () + "/index.jsp");
		} else {
			chain.doFilter (request, response);
		}
	}

	@Override
	public void destroy () {
	}
}
