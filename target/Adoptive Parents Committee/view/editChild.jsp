<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page
	import="com.committee.model.data.User , com.committee.model.data.Child"%>
<%@ page import="java.util.List , java.util.LinkedList"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String message = (String) request.getAttribute("message");
%>
<%
	Child childForEdit = (Child) request.getAttribute("childForEdit");
%>
<%
	User matherOfChildForEdit = (User) request
			.getAttribute("matherOfChildForEdit");
%>
<!-- mather  -->
<%
	List<String> telMather = new LinkedList<String>();
	telMather = (List<String>) request.getAttribute("telMather");
%>
<%
	List<String> emMather = new LinkedList<String>();
	emMather = (List<String>) request.getAttribute("emMather");
%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Adoptive Parents Committee</title>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
</head>
<script>
	function saveChild() {
		var btn = document.getElementById("btnSave");
		btn.setAttribute("disabled", "disabled");
		document.getElementById("formEditChild").submit();
	}
</script>
<body>

	<div class="header">
		<div class="header_content"><%@ include file="header.jsp"%></div>
	</div>
	<div class="container">
		<div class="content">
			<div class="menu"><%@ include file="menu.jsp"%></div>
			<div class="main">
				<h1>
					<label><jgc:Language>editStudent</jgc:Language></label>
				</h1>
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
				<div align="center">
					<c:set var="message" scope="page" value="<%=message%>" />

					<%
						String telMather1 = "", telMather2 = "", emMather1 = "", emMather2 = "" , birthday="";
					%>
					
						<c:if test="${telMather.size()>=1}">
							<%
								telMather1 = telMather.get(0);
							%>
						</c:if>
						<c:if test="${telMather.size()>=2}">
							<%
								telMather2 = telMather.get(1);
							%>
						</c:if>
						<c:if test="${emMather.size()>=1}">
							<%
								emMather1 = emMather.get(0);
							%>
						</c:if>
						<c:if test="${emMather.size()>=2}">
							<%
								emMather2 = emMather.get(1);
							%>
						</c:if>
						<c:if test="${!empty childForEdit.birthday}">
							<%	if(childForEdit.getBirthday().compareTo("null") == 0){
									birthday="";
								}else
									birthday = childForEdit.getBirthday();
							%>
						</c:if>
					
				</div>

				<div align="center">
					<form action="ServletAdmin" method="POST" >
						<select name="idChildForEdit">
							<c:forEach items="${listOfChild}" var="listOfChild">
							<option value="${listOfChild.idChild}">${listOfChild.lastName}
								</option></c:forEach>
						</select>
						<input
							type="submit" name="btnIdChild"
							value="<jgc:Language>search</jgc:Language>">
					</form>
				</div>
				<div>
					<form action="ServletAdmin" id="formEditChild" method="POST">
						<input type="hidden" name="editChild" value="true">
						<table border="1" align="center" CLASS="outline2" >
							<tbody CLASS="outline2">
								<tr>
									<input type="hidden" size="15" name="idChildForEdit"
										value="${childForEdit.idChild}">
									<td><label><jgc:Language>lastName</jgc:Language>*</label></td>
									<td><input type="text" size="30" name="inLastName"
										value="${childForEdit.lastName}"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>firstName</jgc:Language>*</label></td>
									<td><input type="text" size="30" name="inFirstName"
										value="${childForEdit.firstName}"></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><label> <jgc:Language>parents</jgc:Language>
											:
									</label></td>
								</tr>
								<tr>
									<td><label><jgc:Language>lastName</jgc:Language>*</label></td>
									<td><input type="text" size="30" name="inLastNameParent1"
										value="${matherOfChildForEdit.lastName}"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>firstName</jgc:Language></label></td>
									<td><input type="text" size="30" name="inFirstNameParent1"
										value="${matherOfChildForEdit.firstName}"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>byFather</jgc:Language></label></td>
									<td><input type="text" size="30" name="inByFather1"
										value="${matherOfChildForEdit.byFather}"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>tel</jgc:Language> 1*:</label></td>
									<td><input type="tel" size="30" name="inP1Tel1"
										value="<%=telMather1%>"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>tel</jgc:Language>2:</label></td>
									<td><input type="tel" size="30" name="inP1Tel2"
										value="<%=telMather2%>"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>email</jgc:Language>1:</label></td>
									<td><input type="email" size="30" name="inP1Email1"
										value="<%=emMather1%>"></td>
								</tr>
								<tr>
									<td><label><jgc:Language>email</jgc:Language>2:</label></td>
									<td><input type="email" size="30" name="inP1Email2"
										value="<%=emMather2%>"></td>
								</tr>

							</tbody>
						</table>
						<div align="center">
							<input type="reset" value="<jgc:Language>reset</jgc:Language>">

							<!--input type="submit" name="editChild"
								value="<jgc:Language>save</jgc:Language>" onsubmit=this.disabled();this.submit(); -->
							<input type="button" value="Save" id="btnSave" onClick="saveChild()" />

						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="realign"></div>
	</div>
	<div class="footer"></div>

</body>
</html>