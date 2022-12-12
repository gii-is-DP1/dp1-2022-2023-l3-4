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
            <th>Public/Private</th>
            <th>Password</th>

        </tr>
 
        </thead>
        <tbody>
        <c:forEach items="${rooms}" var="room">
            <tr>
                <td>
                    <c:out value="${room.roomName} "/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${room.isPrivate}">
                            Private
                        </c:when>
                        <c:otherwise>
                            Public
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <form>
                        <option id="roomPassword">${room.password}</option> 
                        <c:if test="${room.isPrivate}">
                            <label for="pswd">Enter your password: </label> 
                            <input type="password" id="pswd">
                        </c:if>
                        <input type="button" value="Join Room" class="btn btn-primary" onclick="checkPswd();" />
                    </form>
                    <script type="text/javascript">
                        function checkPswd() {
                            var confirmPassword = document.getElementById("roomPassword").value;
                            var password = document.getElementById("pswd").value;
                            console.log(password);
                            console.log(confirmPassword);
                            if (password == confirmPassword) {
                                window.location.href="/room/${room.id}";
                            }
                            else{
                                alert("Incorrect password.");
                            }
                        }
                    </script>
                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>