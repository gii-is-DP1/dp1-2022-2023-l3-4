<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="game">
    <table align="center">
        <tbody>
            <h1><c:out value="${currentTurnGamePlayer.player.user.username}'s turn"/></h1>
            <c:forEach items="${bodies}" var="body">
                <tr>
                    <td>
                        <h2><c:out value="${body.key.player.firstName}'s body"/></h2>
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
                    <h2>Your hand</h2>
                </td>
            </tr>
            <tr>
                <td>
                    <c:forEach items="${hand}" var="card">
                        <c:if test="${isYourTurn}"><a href="/games/${gameId}/play/${card.id}/"></c:if>
                            <img src="<spring:url value='/resources/images/cards/${card.type.image}.png'/>" alt="" width="65" height="95"/>
                        <c:if test="${isYourTurn}"></a></c:if>

                    </c:forEach>
                </td>
            </tr>
        </tbody>
    </table>
    <c:if test="${isYourTurn}">
        <div class="container" style="padding-left: 480px; padding-top: 20px;"><a href="/games/${gameId}/discard" class="btn btn-primary">Discard</a>  </div>
    </c:if>
    <c:if test="${not isYourTurn}">
        <script type="text/javascript">
        function actualizar(){location.reload(true);}
        //Función para actualizar cada 5 segundos(5000 milisegundos)
        setInterval("actualizar()",4000);
        </script>
    </c:if>

</virus:layout>