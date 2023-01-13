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
                <spring:url value="${player.profileImage}" var="profileImage"/>
                <img class="img-responsive" src="${profileImage}" width="50"/>
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
                    <c:out value="${numGamesPlayed}"/> (<c:out value="${numGamesWon} W - ${numGamesPlayed - numGamesWon} L"/>)
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Win ratio</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${(numGamesWon / numGamesPlayed)*100} %"/>
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
              </table>
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <h2>Achievements<a style="padding-left: 20px;" href="/statistics/achievements">View All</a></h2> 
                  </td>
                </tr>
                <c:choose>
                  <c:when test="${achievements.size()>0}">
                    <c:forEach items="${achievements}" var="achievement">
                      <tr>
                        <td>
                          <c:out value="${achievement.name}"/>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    <tr>
                      <td>
                        You got no achievements
                      </td>
                    </tr>

                  </c:otherwise>
                </c:choose>
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
                  <th>Game</th><th>Date</th><th>Duration</th><th>Players</th><th>Rounds</th><th>Cards</th><th></th>
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
                        <c:out value="${game.gamePlayer.size()}"/>
                      </td>
                      <td>
                        <c:out value="${game.round}"/>
                      </td>
                      <td>
                        Organs: <c:out value="${game.numOrgansPlayed()}"/>
                        Vaccines: <c:out value="${game.numVaccinesPlayed()}"/>
                        Virus: <c:out value="${game.numVirusPlayed()}"/>
                      </td>
                      <td>
                        <c:choose>
                          <c:when test="${game.winner.equals(gameplayer)}">
                            WIN
                          </c:when>
                          <c:otherwise>
                            LOST
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                  <tr>
                    <td>
                      <c:if test="${currentPage < totalPages - 1 }">
                        <a href='<spring:url value="/player/me?page=${currentPage + 1}" htmlEscape="true"/>'>
                          <button>
                            Next page
                          </button>
                        </a>
                      </c:if>
                    </td>
                    <td>
                      <c:if test="${currentPage > 0 }">
                        <a href='<spring:url value="/player/me?page=${currentPage - 1}" htmlEscape="true"/>'>
                          <button>
                            Previous page
                          </button>
                        </a>
                      </c:if>
                    </td>
                  </tr>
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
