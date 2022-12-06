<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="rooms">
    <h2>Rooms</h2>
    

    <table id="roomsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Room Name</th>
            <th>bla</th>
            <th></th>

        </tr>
 
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <tr>
                <td>
                    <c:out value="${room.roomName} "/>
                </td>
                <td>
                    <input id="passwordUserId" name="passwordUser" type="text" />
                </td>
                <td>
                    <a href="/room/${room.id}" class="btn btn-primary">Join Room</a>
                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>