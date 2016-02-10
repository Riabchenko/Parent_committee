<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Adoptive Parents Committee</title>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<!--  ------------------------- init ------------------------ -->
	<jsp:useBean id="admin" type="com.committee.model.dao.AdminDAO"
		scope="request" />
	<c:set var="myAccount" scope="page" value="<%=admin.getMyAccont()%>" />
	<!-- -------------------------- ----- ------------------------ -->

	<div class="header">
		<div class="header_content"><%@ include file="header.jsp"%>
			<div align="right">
				<medium> <jgc:Language>welcome</jgc:Language> <c:choose>
					<c:when test="${!empty myAccount.firstName}">
										, <c:out value="${myAccount.firstName}" />
					</c:when>
					<c:when test="${!empty myAccount.byFather}">
						<c:out value="${myAccount.byFather}" />
					</c:when>
				</c:choose> </medium>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="content">
			<div class="menu"><%@ include file="menu.jsp"%></div>
			<div class="main" align="center">
				<h1>
					<jgc:Language>contactsOfParent</jgc:Language>
				</h1>

				<form action="ServletAdmin" method="POST">
					<div align="center"><input type="submit" name="goToAddChild"
											   value="<jgc:Language>addStudent</jgc:Language>"> <input
							type="submit" name="goToEditChild"
							value="<jgc:Language>editStudent</jgc:Language>"> <input
							type="submit" name="changePassword" value="<jgc:Language>changePassword</jgc:Language>"></div>
				</form>

				<table border="1" CLASS="outline2">
					<tr align="center">
						<td><h5>
								<jgc:Language>fiChild</jgc:Language>
							</h5></td>
						<td><h5>
								<jgc:Language>fioParent</jgc:Language>
							</h5></td>
						<td><h5>
								<jgc:Language>telephones</jgc:Language>
							</h5></td>
						<td><h5>
								<jgc:Language>email</jgc:Language>
							</h5></td>
					</tr>
					<c:forEach var="allChildren" items="${requestScope.listOfChild}">
						<tr>
							<%--<td><c:out value="${allChildren.idChild}"></c:out></td>--%>
							<td><c:out value="${allChildren.lastName}"></c:out> <c:out
									value="${allChildren.firstName}"></c:out></td>
							<c:forEach var="parentsAllChildren"
								items="${allChildren.parents}">
								<%--<td><c:out value="${parentsAllChildren.idAccount}"></c:out></td>--%>
								<td><c:out value="${parentsAllChildren.lastName}"></c:out>
									<c:out value="${parentsAllChildren.firstName}"></c:out> <c:out
										value="${parentsAllChildren.byFather}"></c:out></td>
								<td><c:forEach var="parentsTelephones"
										items="${parentsAllChildren.telephones}">
										<c:out value="${parentsTelephones}"></c:out>
									</c:forEach></td>
								<td><c:forEach var="parentsEmail"
										items="${parentsAllChildren.emails}">
										<c:out value="${parentsEmail}"></c:out>
									</c:forEach></td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>

			</div>
		</div>
		<div class="realign"></div>
	</div>
	<div class="footer"></div>

</body>
</html>