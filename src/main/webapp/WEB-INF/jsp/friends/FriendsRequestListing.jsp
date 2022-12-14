<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="request">
    <h2>Friends</h2>


    <table id="requestTable" class="table table-striped">
        <thead>
        <tr>
            <th>Request:</th>
            <th>Estado</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${myRequest}" var="request">
            <tr>
                <td>
                    <c:out value="${request.playerSend.user.username}"/>
                </td>
                <td>
                    <c:if test="${request.status==null}">Amigos</c:if>
                </td>
                <td>
                    <a href="myFriendsRequest/${request.id}/accept">
                        <input type="button" value="Accept" class="btn btn-success" />
                    </a>
                    <a href="myFriendsRequest/${request.id}/denied">
                        <input type="button" value="Denied" class="btn btn-danger" />
                    </a>
                    
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>