<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="game">
    <table align="center">
        <tbody>
            <c:forEach items="${bodies}" var="body">
                <tr>
                    <td>
                        <c:out value="${body.key.player.firstName}'s body"/>
                    </td>
                </tr>
                <tr>
                    <c:forEach items="${body.value}" var="bodycard">
                        
                        <td>
                            <table>
                                <tr>
                                    <td>
                                        <img src="<spring:url value='/resources/images/cards/${bodycard.type.image}.png'/>" alt="There is no body here"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:if test="${bodycard.virus.size() > 0}">
                                            <c:forEach items="${bodycard.virus}" var="virus">
                                                <img src="<spring:url value='/resources/images/cards/${virus.type.image}.png'/>" alt="There is no body here" height="100"/>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${bodycard.vaccines.size() > 0}">
                                            <c:forEach items="${bodycard.vaccines}" var="vaccine">
                                                <img src="<spring:url value='/resources/images/cards/${vaccine.type.image}.png'/>" alt="There is no body here" height="100"/>
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        
                    </c:forEach>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    Your hand
                </td>
            </tr>
            <tr>
                <td>
                    <c:forEach items="${hand}" var="card">
                        <a href="/games/${gameId}/gamePlayer/${gamePlayerId}/play/${card.id}/">
                            <img src="<spring:url value='/resources/images/cards/${card.type.image}.png'/>" alt="" width="65" height="95"/>
                        </a>
                    </c:forEach>
                </td>
            </tr>
        </tbody>
    </table>
    <a href="/games/{gameId}/gamePlayer/{gamePlayerId}/decision/1" class="btn btn-primary">Descartar</a>  



</virus:layout>