<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="users">
    <h2>
        <c:if test="${player['new']}">New </c:if> Player
    </h2>
    <form:form modelAttribute="player" class="form-horizontal" id="add-player-form">
        <div class="form-group has-feedback">
            <virus:inputField label="First Name" name="firstName"/>
            <virus:inputField label="Last Name" name="lastName"/>
            <virus:inputField label="Username" name="user.username"/>
            <virus:inputField label="Password" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${player['new']}">
                        <button class="btn btn-default" type="submit">Add player</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update player</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</virus:layout>
