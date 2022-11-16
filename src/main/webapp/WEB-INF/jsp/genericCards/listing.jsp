<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="genericCards">
    <h2>genericCards/h2>

    <table id="genericCardsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Id</th>
            <th>Colour</th>
            <th>Type</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${genericCards}" var="genericCard">
            <tr>
                <td>
                    <c:out value="${genericCards.id}"/>
                </td>
                <td>
                    <c:out value="${genericCards.colour}"/>
                </td>
                <td>
                    <c:out value="${genericCards.type}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
<!-- 
    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/games.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table> -->
</petclinic:layout>
