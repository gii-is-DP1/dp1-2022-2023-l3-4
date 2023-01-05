<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="request">
    <h2>Friends Request</h2>

    <div class="col-md-6">
    <table id="requestTable" class="table table-striped">
        <thead>
        <tr>
            <th>Request:</th>
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
    </div>

    <div class="col-md-6">
        <table id="myRequestTable" class="table table-striped">
            <thead>
            <tr>
                <th>My Request:</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mySendRequest}" var="request">
                <tr>
                    <td>
                        <c:out value="${request.playerRec.user.username}"/>
                    </td>
                    <td>
                        <c:if test="${request.status==null}">Pending</c:if>
                        <c:if test="${request.status==true}">Aproved</c:if>
                        <c:if test="${request.status==false}">Denied</c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        </div>

</petclinic:layout>