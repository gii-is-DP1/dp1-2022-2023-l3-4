<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="ranking">
  <table id="rankingTable" class="table table-sm">
      <thead>
        <th>
          Id
        </th>
        <th>
        </th>
        <th>
          Username
        </th>
        <th>
          Wins
        </th>
      </thead>
      <tbody>
      <c:forEach items="${topGamers.keySet()}" var="top">
          <tr>
            <td>
                <c:out value="${top.id}"/>
              </td>
            <td>
              <c:if test="${top.player.profileImage == ''}"></c:if>
              <c:if test="${top.player.profileImage != ''}">
                <spring:url value="${top.player.profileImage}" var="profileImage"/>
                <img class="img-responsive" src="${profileImage}" width="50"/>
              </c:if>
            </td>
            <td>
              <c:out value="${top.player.user.username}"/>
            </td>
            <td>
              <c:out value="${topGamers.get(top)}"/>
            </td>
            <td>
              <a href="/request/${top.player.id}">Anyadir amigo</a>
            </td>
          </tr>
      </c:forEach>
      </tbody>
  </table>

</virus:layout>
