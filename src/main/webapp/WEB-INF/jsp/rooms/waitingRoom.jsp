<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="room">

<script type="text/javascript">
function actualizar(){location.reload(true);}
//Función para actualizar cada 10 segundos(10000 milisegundos)
setInterval("actualizar()",10000);
</script>

    <div class="row">
      <tr >
        <h2>Room Name:  <b><c:out value="${room.roomName}"/></b></h2>
        <h2>Num Players:  <b><c:out value="${countPlayer}"/></b><b>/</b><b><c:out value="${room.numMaxPlayers}"/></b></h2>
        <h2>Host:  <b><c:out value="${room.host.firstName}"/></b></h2>
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
          <c:forEach items="${players}" var="player">
            <tr>
              <td>
                <c:out value="${player.firstName}"/>
              </td>
            </tr>
        </c:forEach>
            <tr> 
              <td>
                <c:choose>
                  <c:when test="${host}">
                    <a href='<spring:url value="/room/edit" htmlEscape="true"/>'>
                                <button>
                                    Edit Room
                                </button>
                    </a>
                    <a href='<spring:url value="/room/delete/${room.id}" htmlEscape="true"/>'>
                                <button>
                                    Delete Room
                                </button>
                    </a>
                    <a href='<spring:url value="/game/start/${room.id}" htmlEscape="true"/>'>
                                <button>
                                    Start Game
                                </button>
                    </a>
                    <a href='<spring:url value="/friend/myFriends" htmlEscape="true"/>'>
                                <button>
                                    Invite friend
                                </button>
                    </a>
                  </c:when>
                    <c:otherwise>
                      <a href='<spring:url value="/room/exit/${room.id}" htmlEscape="true"/>'>
                        <button>
                            Exit
                        </button>
                      </a>
                      <a href='<spring:url value="/friend/myFriends" htmlEscape="true"/>'>
                                <button>
                                    Invite friend
                                </button>
                      </a>
                    </c:otherwise>
                </c:choose>
                
              </td>
            </tr>
        </tbody>
      </table>





</virus:layout>