<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="request">
    <h2>Invitations Request</h2>

    <div class="col-md-12">
    <table id="invitationTable" class="table table-striped">
        <thead>
        <tr>
            <th>Invitation:</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${myInvitation}" var="invitation">
            <tr>
                <td>
                    <c:out value="${invitation.playerInvitationSend.user.username}"/>
                </td>
                <td>
                    <a href="/room/${invitation.room.id}">
                        <input type="button" value="Join Room" class="btn btn-success" />
                    </a>
                    <a href="myInvitationRequest/${invitation.id}/denied">
                        <input type="button" value="Denied" class="btn btn-danger" />
                    </a>
                    
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</petclinic:layout>