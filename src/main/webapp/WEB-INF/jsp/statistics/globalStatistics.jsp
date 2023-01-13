<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="mvc"%>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<virus:layout pageName="globalStatistics">
  <table width="100%">
    <tr>
      <td>
        <table width="50%" class="table table-striped" title="Ranking">
          <h1>Ranking</h1>
          <thead>
            <th>Top</th>
            <th></th>
            <th>Username</th>
            <th>Num Won Games</th>
            <c:if test="${isPlayer}">
              <th></th>
            </c:if>
          </thead>
          <tbody>
            <c:forEach items="${top3}" var="top">
              <tr>
                <td><c:out value="${top3.indexOf(top) + 1}"/></td>
                <td>
                  <c:if test="${top.player.profileImage == ''}"></c:if>
                  <c:if test="${top.player.profileImage != ''}">
                    <spring:url value="${top.player.profileImage}" var="profileImage"/>
                    <img class="img-responsive" src="${profileImage}" width="50"/>
                  </c:if>
                </td>
                <td>
                  <h1><c:out value="${top.player.user.username}"/></h1>
                </td>
                <td>
                  <c:out value="${top.numWonGames}"/>
                </td>
                <c:if test="${isPlayer}">
                  <td>
                    <a href="/request/${top.player.id}">Add Friend</a>
                  </td>
                </c:if>
              </tr>
            </c:forEach>
            
            <c:forEach items="${rops}" var="rop">
              <tr>
                <td>
                  <c:out value="${rops.indexOf(rop) + 1}"/>
                </td>
                <td>
                  <c:if test="${rop.player.profileImage == ''}"></c:if>
                  <c:if test="${rop.player.profileImage != ''}">
                    <spring:url value="${rop.player.profileImage}" var="profileImage"/>
                    <img class="img-responsive" src="${profileImage}" width="50"/>
                  </c:if>
                </td>
                <td>
                  <c:out value="${rop.player.user.username}"/>
                </td>
                <td>
                  <c:out value="${rop.numWonGames}"/>
                </td>
                <c:if test="${isPlayer}">
                  <td>
                    <a href="/request/${top.player.id}">Add Friend</a>
                  </td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </td>
    <c:if test="${isAdmin}">
      <td rowspan="2">
        <table>
          <tr>
            <td>
              <table>
                <tr>
                  <td>
                    <h2>Total games played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${games}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Average games played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${avgGames}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Max games played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <virus:localDateTime date="${maxGames.getKey()}" pattern="dd/MM/yyyy"/>
                    <c:out value="${maxGames.getValue()}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Min games played</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <virus:localDateTime date="${minGames.getKey()}" pattern="dd/MM/yyyy"/>
                    <c:out value="${minGames.getValue()}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Total duration of games</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${duration}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Average duration of games</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${avgDuration}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Max duration of games</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${maxDuration}"/>
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2>Min duration of games</h2>
                  </td>
                </tr>
                <tr>
                  <td>
                    <c:out value="${minDuration}"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </c:if> 
    </tr>
  </table>
</virus:layout>
