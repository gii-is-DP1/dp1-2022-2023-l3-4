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
		<virus:inputField label="First Name" name="firstName"/>
		<virus:inputField label="Last Name" name="lastName"/>
		<virus:inputField label="Description" name="description"/>
		<mvc:label path="status">Select status:</mvc:label>
		<mvc:checkbox path="status"/> Online
		<virus:inputField label="Select profile Image" name="profileImage"/>
		<input type="submit" value="Save" />
  </mvc:form>
</virus:layout>