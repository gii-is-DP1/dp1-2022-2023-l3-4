<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="Users">
  <jsp:body>
      <h2>
        INVALID USER!
    </h2>
      <p>The username already exists in the database.</p>
      <div class="container">
        <a href="<spring:url value="/user/new" htmlEscape="true" />">Back</a>
      </div>
  </jsp:body>
</virus:layout>