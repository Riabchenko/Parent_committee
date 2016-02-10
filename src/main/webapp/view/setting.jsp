<%@ taglib prefix="jgc" uri="/WEB-INF/javacodegeeks.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div align="center">


	<table class="settingEm">
		<tbody align="center">
			<tr>
				<td colspan="2" align="center"><h4><jgc:Language>settingForMail</jgc:Language></h4></td>
			</tr>
			<tr>
				<td align="right"><jgc:Language>adressMail</jgc:Language></td>

				<td><input "text" size="30" name="sender"></td>
			</tr>
			<tr>
				<td align="right"><jgc:Language>loginEmail</jgc:Language></td>

				<td><input "text" size="30" name="login"></td>
			</tr>
			<tr>
				<td align="right"><jgc:Language>passwordEmail</jgc:Language></td>

				<td><input type="password" size="30" name="fieldOfpassword"></td>
			</tr>
		</tbody>
	</table>
</div>