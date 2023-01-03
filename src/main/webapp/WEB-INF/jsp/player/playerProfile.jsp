<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="mvc"%>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<virus:layout pageName="players">
  <table width="100%">
    <tr>
      <td>
        <table width="50%">
          <thead>
            <tr>
              <td rowspan="2">
                <img src="/resources/images/user.png" width="50">
              </td>
              <td>
                <h1><c:out value="${player.user.username}"/></h1>
              </td>
              <td>
                <c:choose>
                  <c:when test="${player.status}">
                      Online
                  </c:when>
                  <c:otherwise>
                      Offline
                  </c:otherwise>
                </c:choose>
              </td>
              <td style="padding-left: 10px;">
                <a href='<spring:url value="/player/me/edit" htmlEscape="true"/>'>
                  <button>
                    Edit
                  </button>
                </a>
              </td>
            </tr>
            <tr>
              <td colspan="3">
                <c:out value="${player.description}"/>
              </td>
            </tr>
          </thead>
        </table>
      </td>
      <td rowspan="2">
        <table>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <h2>Number of games played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${statistics.numPlayedGames}"/> (<c:out value="${statistics.numWonGames} W - ${statistics.numPlayedGames - statistics.numWonGames} L"/>)
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Win ratio</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${(statistics.numWonGames / statistics.numPlayedGames)*100} %"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <h2>Total Time Played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${totalTimePlayed}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Win ratio</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${(statistics.numWonGames / statistics.numPlayedGames)*100} %"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <h2>Achievements</h2>
                  </td>
                </tr>
                <c:forEach items="${player.achievements}" var="achievement">
                  <tr>
                    <td>
                      <c:out value="${achievement.name}"/>
                    </td>
                  </tr>
                </c:forEach>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td rowspan="2" style="padding-right: 20px;">
        <h2>Game history</h2>
        <table class="table table-sm">
          <c:choose>
            <c:when test="${games.size() > 0}">
                <thead>
                  <th>Game</th><th>Initial Hour</th><th>Duration</th><th></th>
                </thead>
                <tbody>
                  <c:forEach items="${games}" var="game">
                    <tr>
                      <td>
                        #<c:out value="${game.id}"/>
                      </td>
                      <td>
                        <virus:localDateTime date="${game.initialHour}" pattern="dd/MM/yyyy HH:mm:ss"/>
                      </td>
                      <td>
                        <c:out value="${game.humanReadableDuration()}"/>
                      </td>
                      <td>
                        <c:choose>
                          <c:when test="${game.gamePlayer.get(game.gamePlayer.indexOf(gameplayer)).isWinner()}">
                            WIN
                          </c:when>
                          <c:otherwise>
                            LOST
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
            </c:when>
            <c:otherwise>
                Play some games to have games history
            </c:otherwise>
          </c:choose> 
          
        </table>
      </td>
    </tr>
  </table>
</virus:layout>
