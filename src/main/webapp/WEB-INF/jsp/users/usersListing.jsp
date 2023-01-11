<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="mvc"%>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<virus:layout pageName="users">
  <table id="usersTable" style="width: 40%;">
    <thead>
      <th>
        Username
      </th>
    </thead>
    <tbody>
      <c:forEach items="${users}" var="user">
        <tr>
          <td>
            <c:out value="${user.username}"/>
          </td>
          <td>
            <a href="/users/${user.username}/edit"><span class="glyphicon glyphicon-pencil"></span></a>
            <a href="/users/${user.username}/delete"><span class="glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <div style="padding-top: 10px;">
    <table>
      <c:if test="${currentPage < totalPages - 1 }">
        <a href='<spring:url value="/users?page=${currentPage + 1}" htmlEscape="true"/>'>
          <button>
            Next page
          </button>
        </a>
      </c:if>
      <c:if test="${currentPage > 0 }">
        <a href='<spring:url value="/users?page=${currentPage - 1}" htmlEscape="true"/>'>
          <button>
            Previous page
          </button>
        </a>
      </c:if>
    </table>
  </div>

  <div style="padding-top: 20px;">
    <a href='<spring:url value="/users/new" htmlEscape="true"/>'>
      <button>
        Create new user
      </button>
    </a>
  </div>
</virus:layout>