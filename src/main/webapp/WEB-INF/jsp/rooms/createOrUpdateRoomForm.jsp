<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="room">
    <jsp:body>
        <h2>
            <c:if test="${room['new']}">New </c:if> Room
        </h2>
        <form:form modelAttribute="room"
                   class="form-horizontal">
            
            <div class="form-group has-feedback">
                <div class="form-group">
                    <petclinic:inputField label="Room Name" name="roomName"/>
                    <petclinic:inputField label="Num max player" name="numMaxPlayers"/>
                    <label for="isPrivate">is Private?</label>
                    <form:checkbox path="isPrivate"/>
                </div>



                
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${room['new']}">
                            <button class="btn btn-default" type="submit">Create Room</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Room</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!room['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
