<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="games">
    <h2>Games</h2>

    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
            <th>InitialHour</th>
            <th>IsRunning</th>
            <th>Round</th>
            <th>Turn</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${games}" var="game">
            <tr>
                <td>
                    <c:out value="${game.initialHour}"/>
                </td>
                <td>
                    <c:out value="${game.isRunning}"/>
                </td>
                <td>
                    <c:out value="${game.round}"/>
                </td>
                <td>
                    <c:out value="${game.turn}"/>
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
</virus:layout>
