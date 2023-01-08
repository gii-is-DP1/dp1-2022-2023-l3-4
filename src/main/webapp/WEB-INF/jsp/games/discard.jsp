<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<virus:layout pageName="game">
    <table align="center">
        <form:form modelAttribute="cardsForm">
            <tr>
                <td>
                    <h1>Select the cards you want to discard</h1>
                </td>
            </tr>
            <tr>
                <td>
                    <c:forEach items="${hand}" var="card">
                        <img src="<spring:url value='/resources/images/cards/${card.type.image}.png'/>" alt="" width="65" height="95"/>
                        <form:checkbox value="${card.id}" path="cards"/>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>
                  <button class="btn btn-default" type="submit">Discard</button>
                </td>
            </tr>
        </form:form>
    </table>

</virus:layout>