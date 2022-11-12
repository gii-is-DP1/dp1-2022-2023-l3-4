<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="mvc"%>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="player">
<title>Profile</title>
</head>
<body>
	<h2>Profile</h2>
	<mvc:form modelAttribute="player">
    <table>
			<tr>
				<td><mvc:label path="user.username">Username:</mvc:label></td>
				<td style="padding-left: 20px"><mvc:input path="user.username"/></td>
			</tr>
			<tr>
				<td><mvc:label path="description">Description:</mvc:label></td>
				<td style="padding-left: 20px"><mvc:input path="description"/></td>
			</tr>
			<tr>
				<td><mvc:label path="status">Select status:</mvc:label>
        <td style="padding-left: 20px"><mvc:checkbox path="status"/> Online</td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Save" /></td>
			</tr>
		</table>
  </mvc:form>
</virus:layout>