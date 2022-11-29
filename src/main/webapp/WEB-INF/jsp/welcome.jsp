<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<virus:layout pageName="home">
  <h2><fmt:message key="welcome"/></h2>
  <div class="row">
  <h2> Project ${title} </h2>
  <p><h2> Group ${group}</h2></p>
  <p><ul>
    <c:forEach items="${persons}" var="person">
      <li>${person.firstName} ${person.lastName}</li>
    </c:forEach>
  </ul></p>
    <div class="col-md-12">
      <spring:url value="/resources/images/logo_US.png" htmlEscape="true" var="logoUS"/>
      <img class="img-responsive" src="${logoUS}"/>
    </div>
  </div>
  <tr>
    <td>
        <a href="<spring:url value="/room/new" htmlEscape="true" />">New room</a>
    </td>
    <br>
    <td>
      <a href="<spring:url value="/room/createSearch" htmlEscape="true" />">Searc room </a>
  </td>          
  <br>
  <td>
    <a href="<spring:url value="/room/find" htmlEscape="true" />">Rooms listing </a>
</td>


</tr>
</virus:layout>
