<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="players">

    <table>
        <tr>
            <td>
                <h2><img src="/resources/images/user.png" width="50"><c:out value="${player.user.username}"/></h2>
            </td>
        </tr>
    </table>
</virus:layout>