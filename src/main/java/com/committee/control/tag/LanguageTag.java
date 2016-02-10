package com.committee.control.tag;

/**
 *
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 *
 *
 */
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class LanguageTag extends SimpleTagSupport {

	ResourceBundle resourceBundle;
	Locale language;

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		PageContext pageContext = (PageContext) getJspContext();

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// -------language ------//
		HttpSession session = pageContext.getSession();
		resourceBundle = (ResourceBundle) session.getAttribute("language");
		if (resourceBundle == null) {
			language = request.getLocale();
			resourceBundle = ResourceBundle.getBundle("i18n.lang", language);
			request.getSession().setAttribute("language", resourceBundle);
		}
		// -------------------//
		StringWriter sw = new StringWriter();
		getJspBody().invoke(sw);
		StringBuilder bsw = new StringBuilder();
		bsw.append(sw);
		String message = resourceBundle.getString(bsw.toString());
		out.print(message);
	}

}
