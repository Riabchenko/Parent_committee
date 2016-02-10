<%@ taglib prefix="jgc" uri="/WEB-INF/javacodegeeks.tld"%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  	<title> Adoptive Parents Committee </title>
  	<link href="resources/css/style.css" rel="stylesheet" type="text/css" >
 </head>
<body> 
<jsp:useBean id="user" type="com.committee.model.dao.UserDAO" scope="request"/>
	<c:set var="myAccount" scope="page" value ="<%= user.getMyAccont() %>"/>
<div class="header">
   <div class="header_content"><%@ include file="header.jsp" %><div align="right">
		<medium>
			<jgc:Language>welcome</jgc:Language> 
				<c:choose>
					<c:when test="${!empty myAccount.firstName}">
						, <c:out value="${myAccount.firstName}"/>
					</c:when>
					<c:when test="${!empty myAccount.byFather}">
						<c:out value="${myAccount.byFather}"/>
					</c:when>
				</c:choose>
		</medium>
	</div></div>
</div>
<div class="container">
  <div class="content">
    <div class="menu"></div>
    <div class="main"><div>
	<h1 align = "center" ><jgc:Language>personalData</jgc:Language></h1>
		<table border="1" align = "center" CLASS = "outline2">
			<tbody align = "center">
				<tr>
					<td>
						<h3><c:choose>
        						<c:when test="${!empty myAccount.lastName}">
									<c:out value="${myAccount.lastName}"/>
								</c:when>
								<c:when test="${!empty myAccount.firstName}">
									<c:out value="${myAccount.firstName}"/>
								</c:when>
								<c:when test="${!empty myAccount.byFather}">
									<c:out value="${myAccount.byFather}"/>
								</c:when>
						</c:choose></h3>
					</td>
				</tr>
				<tr>
					<td>
						<font color="#8A8A8A"><h3><jgc:Language>childOfUser</jgc:Language>:</h3></font>
					</td>
				</tr>
				<tr>
					<td>
						<c:forEach var="myChildren" items="${myAccount.children}">
								<c:out value="${myChildren.lastName}"></c:out>
								<c:out value="${myChildren.firstName}"></c:out>
								<c:choose>
									<c:when test="${myChildren.birthday != 'null'}">
										<c:out value="${myChildren.birthday}"></c:out>
									</c:when>
								</c:choose>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td>
						<font color="#8A8A8A"><h3><jgc:Language>telephones</jgc:Language>:</h3></font>
					</td>
				</tr>
				<tr>
					<td>
						<c:forEach var="telephone" items="${myAccount.telephones}">
								<c:out value="${telephone}"></c:out>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td>
						<font color="#8A8A8A"><h3><jgc:Language>email</jgc:Language>:</h3></font>
					</td>
				</tr>
				<tr>
					<td>
						<c:forEach var="email" items="${myAccount.emails}">
								<c:out value="${email}"></c:out>
						</c:forEach>
					</td>
				</tr>
			</tbody>
		</table></div>
    <div class="right"></div>
  </div>
  <div class="realign"></div>
</div>
<div class="footer"></div>

</body>
</html>