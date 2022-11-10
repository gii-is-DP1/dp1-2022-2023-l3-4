<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="room">
    <div class="row">
      <tr >
        <h2>Room Name:  <b><c:out value="${room.roomName}"/></b></h2>
        <h2>Num Players:  <b><c:out value="${countPlayer}"/></b><b>/</b><b><c:out value="${room.numMaxPlayers}"/></b></h2>
      </tr>

    </div>



    <table class="table">
        <thead>
            <tr >
              <th>
                Player Name:
              </th>
              
            </tr>
          </thead>
        <tbody>
          <c:forEach items="${owners}" var="owner">
            <tr>
              <td>
                <c:out value="${owner.firstName}"/>
              </td>
            </tr>
        </c:forEach>
        </tbody>
      </table>





</virus:layout>