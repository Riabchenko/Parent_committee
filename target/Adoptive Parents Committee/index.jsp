<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String message = (String) request.getAttribute("message");
	boolean click = true;
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Adoptive Parents Committee</title>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<!-- <script type="text/javascript" src="jquery-1.2.3.js"></script> -->

</head>

<body>
	<%@ include file="view/header.jsp"%>

	<%------------------------- Error --------------------------%>
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
					<font size="5" color="red"><jgc:Language>message005</jgc:Language></font>
				</c:if>
				<c:if test="${message == '006'}">
					<font size="5" color="red"><jgc:Language>message006</jgc:Language></font>
				</c:if>
				<c:if test="${message == '007'}">
					<font size="5" color="red"><jgc:Language>message007</jgc:Language></font>
				</c:if>
				<c:if test="${message == '008'}">
					<font size="5" color="red"><jgc:Language>message008</jgc:Language></font>
				</c:if>
				<c:if test="${message == '013'}">
					<font size="5" color="red">Нет соединения с БД!!!</font>
				</c:if>
				<c:if test="${message == '018'}">
					<font size="5" color="red">Ошибка доступа!</font>
				</c:if>

			</c:when>
		</c:choose>
	</div>
	<div>
		<form method="post" action="ServletStart"
			onsubmit="this.enter.value='Submitting ..';this.enter.disabled='disabled'; this.submit();">
			<div align="center">
				<h1><label><%=resourceBundle.getString("identification")%></label></h1>
				<table align="center" CLASS="outline2">
					<tbody>
						<tr>
							<td><label><%=resourceBundle.getString("tel")%>
									<h2><%=resourceBundle.getString("primeTel")%></h2> </label></td>
							<td><input type="telephone" name="telephone"></td>
						</tr>
						<tr>
							<td><label><%=resourceBundle.getString("password")%></label></td>
							<td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td><img class="lock"></td>
							<td><input type="reset"
								value="<%=resourceBundle.getString("reset")%>"> <input
								type="submit" name="enter" 
								value="<%=resourceBundle.getString("enter")%>"></td>
						</tr>
						<tr>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>

</body>
</html>