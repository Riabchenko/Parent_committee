package com.committee.control;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Servlet Filter implementation class CharsetEncodingFilter
 */
// @WebFilter(filterName = "CharacterEncodingFilter", urlPatterns =
// {"/pages/*"})
public class CharsetEncodingFilter implements Filter {

	// кодировка
	private String encoding;

	public void init(FilterConfig config) throws ServletException {
		// читаем из конфигурации
		encoding = config.getInitParameter("requestEncoding");

		// если не установлена - устанавливаем "UTF-8"
		if (encoding == null)
			encoding = "UTF-8";
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain next) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		next.doFilter(request, response);
	}

	public void destroy() {
	}

}
