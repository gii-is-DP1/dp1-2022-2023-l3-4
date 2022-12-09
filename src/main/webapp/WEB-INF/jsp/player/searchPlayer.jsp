<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="player">
    <h2>
        Welcome Player:
    </h2>
    <div>
        <form:form modelAttribute="player" action="/player/find" method="get" class="form-horizontal"
            id="search-player-form">
            <div class="form-group">
                <div class="control-group" id="userName">
                    <label class="col-sm-2 control-label">User Name </label>
                    <div class="col-sm-10">
                        <form:input class="form-control" path="userName" size="20" maxlength="20" />
                        <span class="help-inline">
                            <form:errors path="*" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary">Find Player</button>
                </div>
            </div>
        </form:form>
    </div>

</petclinic:layout>