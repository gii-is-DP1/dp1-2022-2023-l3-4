<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="statistics">

    <table id="statisticsTable" class="table table-striped">
        <thead>
        <tr>
          <img class="img-responsive" src="resources/images/user.png"/>
          <td>
            <c:out value="${statistics}"/>
          </td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${achievements}" var="ach">
            <tr>
                <td>
                    <c:out value="${ach.name}"/>
                </td>
                <td>
                    <c:out value="${ach.actualDescription}"/>
                </td>
                <td>
                    <c:if test="${ach.badgeImage == ''}">none</c:if>
                    <c:if test="${ach.badgeImage != ''}">
                        <spring:url value="${ach.badgeImage}" var="badgeImage"/>
                        <img class="img-responsive" src="${badgeImage}" width="80" height="80"/>
                    </c:if>
                </td>
                <td>
                    <c:out value="${ach.threshold}"/>
                </td>
                <td>
                    <a href="/statistics/achievements/${ach.id}/edit"><span class="glyphicon glyphicon-pencil"></span></a>
                    <a href="/statistics/achievements/${ach.id}/delete"><span class="glyphicon glyphicon-trash"></span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a class="btn btn-default" href="/statistics/achievements/new">Create new achievement</a>

</petclinic:layout>
