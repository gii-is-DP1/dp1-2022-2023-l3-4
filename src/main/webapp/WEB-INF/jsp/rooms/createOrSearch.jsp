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

                        
                        <button class="btn btn-primary btn-lg btn-block" >Search Room</button>
            </div>
</virus:layout>