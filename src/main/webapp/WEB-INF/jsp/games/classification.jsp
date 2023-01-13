<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="classification">
    <table align="center">
        <tbody>
            <h1>Classification of the game:</h1>
            <c:forEach items="${classification}" var="player">
                <tr>
                    <td>
                        <h2><c:out value="${classification.indexOf(player) + 1} place:"/></h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:out value="${player.player.user.username}"/>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</virus:layout>