package com.tmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class ForeFilter implements Filter {
	//前台过滤器
	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		String contextPath = request.getContextPath();
		String url = request.getRequestURI();
		url = StringUtils.remove(url, contextPath);//将contextPath从url中移除
		if (url.startsWith("/fore")) {
			String method = StringUtils.remove(url, "/fore");
			request.setAttribute("method", method);
			request.getRequestDispatcher("/IndexServlet").forward(req, rep);
			return;
		}

		chain.doFilter(req, rep);
	}

}
