<%@ taglib prefix="jgc" uri="/WEB-INF/javacodegeeks.tld"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import = "java.util.ResourceBundle, java.util.Locale" %>
<form name="language">

<%!Locale language;%>
<%
			ResourceBundle resourceBundle;
			String lan = request.getParameter("language");
			Locale language = null;
			if(lan != null){
				if(lan.compareTo("en") == 0)language = new Locale("en","US");
				else if(lan.compareTo("uk") == 0)language = new Locale("uk","UA");
				else if(lan.compareTo("ru") == 0)language = new Locale("ru","RU");
				else language = new Locale("","");
				resourceBundle = ResourceBundle.getBundle("i18n.lang", language);   
				request.getSession().setAttribute("language", resourceBundle);
				
			} 
			else{
			resourceBundle = (ResourceBundle) session.getAttribute("language");
			if(resourceBundle == null){
				language = request.getLocale();
				resourceBundle = ResourceBundle.getBundle("i18n.lang", language);   
				request.getSession().setAttribute("language", resourceBundle);
				
			}
			}
 %>
 
<div align="left">
	<input type="image" name="language" src="resources/img/Ukraine-Flag.png" alt="УКР" value ="uk" width ="40"  height="30"> 
	<input type="image" name="language" src="resources/img/ru.png" alt="РУС" value ="ru" width ="40"  height="30"> 
	<input type="image" name="language" src="resources/img/united-kingdom-flag.png" alt="ENG" value ="en" width ="40"  height="30"> 
</div>


</form>

