package com.tmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.tmall.beans.Admin;


public class AdminFilter implements Filter {

	
	public void destroy() {
		// TODO Auto-generated method stub
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getContextPath();
		String fullUri = req.getRequestURI();
		String uri = StringUtils.remove(fullUri, path);
		Admin admin = (Admin) req.getSession().getAttribute("admin");
		if (uri.startsWith("/admin_") && admin == null) {
			req.getSession().setAttribute("uri", fullUri);
			req.getRequestDispatcher("admin/adminLogin.jsp").forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
