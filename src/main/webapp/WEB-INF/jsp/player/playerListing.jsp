<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Players</h2>
    

    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
            <th>User Name</th>
            <th></th>

        </tr>
 
        </thead>
        <tbody>
        <c:forEach items="${players}" var="player">
            <tr>
                <td>
                    <c:out value="${room.roomName} "/>
                </td>
                <td>
                    
                </td>
                
            </tr>

        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>