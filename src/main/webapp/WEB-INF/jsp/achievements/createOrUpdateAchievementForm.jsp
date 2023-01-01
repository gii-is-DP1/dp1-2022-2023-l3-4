<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="mvc"%>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="achievements">
<title>Achievement</title>
</head>
<body>
	<h2>Achievement</h2>
	<mvc:form modelAttribute="achievement">
		<table>
			<tr>
				<td><mvc:label path="name">Name:</mvc:label></td>
				<td style="padding-left: 10px;"><mvc:input path="name"/></td>
			</tr>
			<tr>
				<td><mvc:label path="description">Description:</mvc:label></td>
				<td style="padding-left: 10px;"><mvc:input path="description" /></td>
			</tr>
			<tr>
				<td><mvc:label path="threshold">Threshold:</mvc:label></td>
				<td style="padding-left: 10px;"><mvc:input path="threshold" /></td>
			</tr>
			<tr>
				<td><mvc:label path="type">Tipo: </mvc:label></td>
				<td style="padding-left: 10px;">
					<mvc:select path="type">
						<mvc:options items="${types}" size="1"/>
					</mvc:select>
				</td>
			</tr>
			<tr>
				<td><mvc:label path="badgeImage">Badge Image: </mvc:label></td>
				<td style="padding-left: 10px;"><mvc:input path="badgeImage"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Save" /></td>
			</tr>
		</table>
	</mvc:form>

</body>
</virus:layout>