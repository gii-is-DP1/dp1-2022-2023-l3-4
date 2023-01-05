<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="game">
    <table align="center">
        <tbody>
            <h1>Select a body or a card</h1>
            <c:forEach items="${bodies}" var="body">
                <tr>
                    <td>
                      <a href="/games/${gameId}/play/${cardId}/toPlayer/${body.key.id}">
                        <h2><c:out value="${body.key.player.firstName}'s body"/></h2>
                      </a>
                    </td>
                </tr>
                <tr>
                    <c:forEach items="${body.value}" var="bodycard">
                        <td>
                            <a href="/games/${gameId}/play/${cardId}/toCard/${bodycard.id}">
                            <img src="<spring:url value='/resources/images/cards/${bodycard.type.image}.png'/>" alt="There is no body here"/>
                            </a>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    <h2>Your hand</h2>
                </td>
            </tr>
            <tr>
                <td>
                    <c:forEach items="${hand}" var="card">
                        <img src="<spring:url value='/resources/images/cards/${card.type.image}.png'/>" alt="" width="65" height="95"/>
                    </c:forEach>
                </td>
            </tr>
        </tbody>
    </table>


</virus:layout>