<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="game">
    <table>
        <tbody>
            <c:forEach items="${bodies}" var="body">
                <tr>
                    <td>
                        <c:out value="${body.key}'s body"/>
                    </td>
                    
                </tr>
            </c:forEach>
            <tr>
                <td>
                    Your hand:
                </td>
                <c:forEach items="${hand}" var="card">
                    
                    <td>
                        <img src="<spring:url value='/resources/images/cards/${card.type.image}.png'/>" alt=""/>
                    </td>
                </c:forEach>
            </tr>
        </tbody>
    </table>  



</virus:layout>