<%@ taglib prefix="jgc" uri="/WEB-INF/javacodegeeks.tld"%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<body>
	<c:set var="message" scope="page" value="<%=message%>" />
	<div class="header">
		<div class="header_content"><%@ include file="header.jsp"%></div>
	</div>
	<div class="container">
		<div class="content">
			<div class="menu"><%@ include file="menu.jsp"%></div>
			<div class="main" align="center">
				<h1 align="center">
					<label><jgc:Language>newStudent</jgc:Language></label>
				</h1>
				<div>
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
							<font size="5" color="red">Данные сохранены.Письмо с
								паролем не отправлено.</font>
						</c:if>
						<c:if test="${message == '010'}">
							<font size="5" color="blue">Пароль успешно сохранен.Письмо
								с паролем отправлено.</font>
						</c:if>
						<c:if test="${message == '011'}">
							<font size="5" color="blue">Письмо успешно отправленно</font>
						</c:if>
						<c:if test="${message == '012'}">
							<font size="5" color="red">Письмо не удалось отправить</font>
						</c:if>
						<c:if test="${message == '013'}">
							<font size="5" color="red">Нет соединения с БД!!!</font>
						</c:if>
						<c:if test="${message == '014'}">
							<font size="5" color="red">У этого родителя нет email! Письмо не отправленно!</font>
						</c:if>
						</c:when>
					</c:choose>
				</div>
				<form action="ServletAdmin" method="POST">
					<table border="1" align="center" CLASS="outline2">
						<tbody CLASS="outline2">

							<tr>
								<td><label><jgc:Language>lastName</jgc:Language>*</label></td>
								<td><input type="text" size="30" name="inLastName"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>firstName</jgc:Language>*</label></td>
								<td><input type="text" size="30" name="inFirstName"></td>
							</tr>
							<%--<tr>--%>
								<%--<td><label><jgc:Language>birthday</jgc:Language></label></td>--%>
								<%--<td><input type="date" size="30" name="inBirthday"></td>--%>
							<%--</tr>--%>
							<tr>
								<td colspan="2" align="center"><label><h4>
											<jgc:Language>parents</jgc:Language>
											:
										</h4></label></td>
							<tr>
								<td><label><jgc:Language>lastName</jgc:Language>*</label></td>
								<td><input type="text" size="30" name="inLastNameParent1"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>firstName</jgc:Language></label></td>
								<td><input type="text" size="30" name="inFirstNameParent1"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>byFather</jgc:Language></label></td>
								<td><input type="text" size="30" name="inByFather1"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>tel</jgc:Language>1*:</label></td>
								<td><input type="tel" size="30" name="inP1Tel1"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>tel</jgc:Language>2:</label></td>
								<td><input type="tel" size="30" name="inP1Tel2"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>email</jgc:Language>1:</label></td>
								<td><input type="email" size="30" name="inP1Email1"></td>
							</tr>
							<tr>
								<td><label><jgc:Language>email</jgc:Language>2:</label></td>
								<td><input type="email" size="30" name="inP1Email2"></td>
							</tr>
							<%--<tr>--%>
								<%--<td colspan="2"><%@ include file="setting.jsp"%>--%>
								<%--</td>--%>
							<%--</tr>--%>

						</tbody>
					</table>

					<input type="reset" value="<jgc:Language>reset</jgc:Language>">
					<input type="submit" name="addChild"
						value="<jgc:Language>add</jgc:Language>" onclic="/ServletStart">

				</form>
			</div>
		</div>

		<div class="realign"></div>
	</div>
	<div class="footer"></div>

</body>
</html>