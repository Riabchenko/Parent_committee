<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Adoptive Parents Committee</title>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
</head>
<%--
	<div  align = "center">
		<c:choose>
        	<c:when test="${!empty error}">
					<font size="5" color="red">${error}</font>
			</c:when>
		</c:choose>
	</div>
	 --%>

<div class="header">
	<div class="header_content"><%@ include file="header.jsp"%></div>
</div>
<div class="container">
	<div class="content">
		<div class="menu"><%@ include file="menu.jsp"%></div>
		<div class="main">
			<div align="center">
				<c:choose>
					<c:when test="${!empty message}">
						<c:if test="${message == '001'}">
							<font size="5" color="red"><jgc:Language>message001</jgc:Language></font>
						</c:if>
						<c:if test="${message == '002'}">
							<font size="5" color="red"><jgc:Language>message002</jgc:Language></font>
						</c:if>
						<c:if test="${message == '003'}">
							<font size="5" color="red"><jgc:Language>message003</jgc:Language></font>
						</c:if>
						<c:if test="${message == '004'}">
							<font size="5" color="red"><jgc:Language>message004</jgc:Language></font>
						</c:if>
						<c:if test="${message == '005'}">
							<font size="5" color="blue"><jgc:Language>message005</jgc:Language></font>
						</c:if>
						<c:if test="${message == '006'}">
							<font size="5" color="red"><jgc:Language>message006</jgc:Language></font>
						</c:if>
						<c:if test="${message == '007'}">
							<font size="5" color="red"><jgc:Language>message007</jgc:Language></font>
						</c:if>
						<c:if test="${message == '008'}">
							<font size="5" color="blue"><jgc:Language>message008</jgc:Language></font>
						</c:if>
						<c:if test="${message == '009'}">
							<font size="5" color="red"><jgc:Language>message009</jgc:Language></font>
						</c:if>
						<c:if test="${message == '010'}">
							<font size="5" color="blue"><jgc:Language>message010</jgc:Language></font>
						</c:if>
						<c:if test="${message == '011'}">
							<font size="5" color="blue"><jgc:Language>message011</jgc:Language></font>
						</c:if>
						<c:if test="${message == '012'}">
							<font size="5" color="red"><jgc:Language>message012</jgc:Language></font>
						</c:if>
						<c:if test="${message == '013'}">
							<font size="5" color="red"><jgc:Language>message013</jgc:Language></font>
						</c:if>
						<c:if test="${message == '014'}">
							<font size="5" color="red"><jgc:Language>message014</jgc:Language></font>
						</c:if>

					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
	<div class="realign"></div>
</div>
<div class="footer"></div>

</body>
</html>