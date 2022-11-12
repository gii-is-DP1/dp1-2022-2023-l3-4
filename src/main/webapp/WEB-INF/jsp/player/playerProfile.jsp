<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="players">

    <table>
        <tr>
            <td>
                <h2>
                    <img src="/resources/images/user.png" width="50"><strong style="padding-left: 20px; "><c:out value="${player.user.username}"/></strong>                
                </h2>
            </td>
        </tr>
        <tr>
            <td><strong>Description: </strong><c:out value="${player.description}"/></td>
        </tr>
        <tr>
            <th>
                Historial de partidas
            </th>
        </tr>
        <tr>
            <td>

            </td>
        </tr>
    </table>
</virus:layout>