<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Adoptive Parents Committee</title>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div class="header">
		<div class="header_content"><%@ include file="header.jsp"%></div>
	</div>
	<div class="container">
		<div class="content">
			<div class="menu"><%@ include file="menu.jsp"%></div>
			<div class="main">
				<h1><jgc:Language>reports</jgc:Language></h1>
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
							<c:if test="${message == '015'}">
								<font size="5" color="red"><jgc:Language>message015</jgc:Language></font>
							</c:if>
							<c:if test="${message == '016'}">
								<font size="5" color="red"><jgc:Language>message016</jgc:Language></font>
							</c:if>
							<c:if test="${message == '017'}">
								<font size="5" color="red"><jgc:Language>message017</jgc:Language></font>
							</c:if>
						</c:when>
					</c:choose>
				</div>
				<div align="center">
					<table border="1" CLASS="outline2">
						<tr align="center">
							<td><h5><jgc:Language>idCash</jgc:Language></h5></td>
							<td><h5><jgc:Language>dateTime</jgc:Language></h5></td>
							<td><h5><jgc:Language>in</jgc:Language></h5></td>
							<td><h5><jgc:Language>out</jgc:Language></h5></td>
							<td><h5><jgc:Language>info</jgc:Language></h5></td>
							<td><h5><jgc:Language>whoGave</jgc:Language></h5></td>
							<td><h5><jgc:Language>admin</jgc:Language></h5></td>
						</tr>
						<c:forEach var="reports" items="${reports}">
							<tr>
								<td><c:out value="${reports.id_cash}"></c:out></td>
								<td><c:out value="${reports.dateTime}"></c:out></td>
								<td><font color="blue"><c:out value="${reports.in}"></c:out></font></td>
								<td><font color="red"><c:out value="${reports.out}"></c:out></font></td>
								<td><c:out value="${reports.info}"></c:out></td>
								<td><c:out value="${reports.whoGaveCash}"></c:out></td>
								<td><c:out value="${reports.admin}"></c:out></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div align="center">---------------------------------------------------------------</div>
				<div align="center">
					<h1><jgc:Language>addWriting</jgc:Language></h1>

					<form action="ServletAdmin" method="POST">
						<table class="outline2">
							<tbody>
								<tr>
									<td>
										<table border="1">
											<tbody>
												<tr>
													<td colspan="2" align="center"><font color="blue"><h3><jgc:Language>in</jgc:Language></h3></font></td>

												</tr>

												<tr>
													<td align="right"><font color="blue"><jgc:Language>sum</jgc:Language></font></td>
													<td><input type="text" name="inCash" size="16px"></td>
												</tr>
												<tr>
													<td align="right"><font color="blue"><jgc:Language>whoGave</jgc:Language></font></td>
													<td><select name="idParent">
															<c:forEach items="${parents}" var="parents">
																<option value="${parents.idAccount}">${parents.lastName}
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td align="right"><font color="blue"><jgc:Language>info</jgc:Language></font></td>
													<td><textarea name="infoIn" cols="14" rows="1"></textarea></td>
												</tr>
												<tr>
													<td align="center" colspan="2"><input type="submit"
														name="btnAddIn" value="<jgc:Language>add</jgc:Language>"></td>
												</tr>
											</tbody>
										</table>
									</td>
									<td>
										<table border="1">
											<tbody>
												<tr>
													<td colspan="2" align="center"><font color="red"><h3><jgc:Language>out</jgc:Language></h3></font></td>

												</tr>

												<tr>
													<td align="right"><font color="red"><jgc:Language>sum</jgc:Language></font></td>
													<td><input type="text" name="outCash" size="16px"></td>
												</tr>
												<tr>
													<td><font color="#F2F2F2">.</font></td>
													<td></td>
												</tr>
												<tr>
													<td align="right"><font color="red"><jgc:Language>info</jgc:Language></font></td>
													<td><textarea name="outInfo" cols="14" rows="1"></textarea></td>
												</tr>
												<tr>
													<td align="center" colspan="2"><input type="submit"
														name="btnAddOut" value="<jgc:Language>add</jgc:Language>"></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>

					</form>
				</div>
			</div>
		</div>
		<div class="realign"></div>
	</div>
	<div class="footer"></div>

</body>
</html>