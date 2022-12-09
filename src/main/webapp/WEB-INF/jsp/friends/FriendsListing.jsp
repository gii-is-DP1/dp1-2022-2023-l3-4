<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="friends">
    <div class="row">
        <div class="col-md-8">
            <h2>Friends</h2>
        </div>
        <div class="col-md-2">
            <a href="/friend/myFriendsRequest">My Friends Request</a>
        </div>
        <div class="col-md-2">
            <a href="/player/createSearch">Search Player</a>
        </div>
    </div>


    <table id="friendsTable" class="table table-striped">
        <thead>
        <tr>
            <th>User Name:</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${myFriends}" var="friend">
            <tr>
                <td>
                    <c:out value="${friend.user.username}"/>
                </td>
                <td>
                    <a href="${playerAuth.id}/delete/${friend.id}">
                        <input type="button" value="Delete" class="btn btn-danger" />
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>