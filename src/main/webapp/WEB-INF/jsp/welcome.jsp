<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
  
  <h2><fmt:message key="welcome"/></h2>
  <div class="row">
  
  <tr>
    <div>
      <a href="/room/new" class="btn btn-primary btn-lg btn-block">Create Room</a>
  </div>
  <br/>
  <div>
    <a href="/room/createSearch" class="btn btn-primary btn-lg btn-block">Search Room</a>
</div>





</tr>
</petclinic:layout>
