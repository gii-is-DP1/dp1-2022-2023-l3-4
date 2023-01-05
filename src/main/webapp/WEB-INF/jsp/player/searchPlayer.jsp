<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="player">
    <h2>
        Search Player:
    </h2>
    <form:form modelAttribute="player" class="form-horizontal">
            <div class="form-group">
                <div class="control-group" id="username">
                    <label class="col-sm-2 control-label">username </label>
                    <div class="col-sm-10">
                        <form:input class="form-control" path="User.username" size="20" maxlength="20" />
                        <span class="help-inline">
                            <form:errors path="*" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary">Find Room</button>
                </div>
            </div>
        </form:form>


    </petclinic:layout>