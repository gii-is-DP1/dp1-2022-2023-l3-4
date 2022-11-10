<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="room">
    <h2>
        Welcome Player:
    </h2>

            <div>
                <a href="/room/new" class="btn btn-primary btn-lg btn-block">Create Room</a>
            </div>
            <div>
                <form:form modelAttribute="room" action="/room/find" method="get" class="form-horizontal"
                id="search-owner-form">
         <div class="form-group">
             <div class="control-group" id="roomName">
                 <label class="col-sm-2 control-label">Room Name </label>
                 <div class="col-sm-10">
                     <form:input class="form-control" path="roomName" size="20" maxlength="20"/>
                     <span class="help-inline"><form:errors path="*"/></span>
                 </div>
             </div>
         </div>
         <div class="form-group">
             <div class="col-sm-offset-2 col-sm-10">
                 <button type="submit" class="btn btn-primary">Find Room</button>
             </div>
         </div>
        </form:form>
            </div>
</virus:layout>