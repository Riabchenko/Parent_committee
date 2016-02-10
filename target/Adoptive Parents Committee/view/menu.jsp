<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import = "java.util.ResourceBundle, java.util.Locale" %>

<%-- <%@ include file="header.jsp" %> --%>
<form action="ServletAdmin" method = "POST">
	<ul id="navigation">
      <li><input class ="btn" type="submit" name="infoParents" value="<jgc:Language>infoParents</jgc:Language>"></li>
      <li><input class ="btn" type="submit" name="reports" value="<jgc:Language>reports</jgc:Language>"></li>
      <li><input class ="btn" type="submit" name="newMail" value="<jgc:Language>newMail</jgc:Language>"></li>
	</ul>	
</form>	
