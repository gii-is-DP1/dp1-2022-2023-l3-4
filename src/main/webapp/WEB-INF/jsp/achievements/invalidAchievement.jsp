<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="Achievements">
    <jsp:attribute name="customScript">
    </jsp:attribute>
    <jsp:body>
        <h2>
        	INVALID ACHIEVEMENT!
    	</h2>
        <p>The achievement name is duplicated and that cannot happen.</p>
        <div class="container">
          <a href="<spring:url value="/statistics/achievements/new" htmlEscape="true" />">Back</a>
        </div>
    </jsp:body>
  </virus:layout>