<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="virus" tagdir="/WEB-INF/tags" %>

<virus:layout pageName="games">
    <h2>Running Games</h2>

    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
            <th>Room name</th>
            <th>Host</th>
            <th>Players</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${games}" var="game">
            <tr>
                <td>
                    <c:out value="${game.room.roomName}"/>
                </td>
                <td>
                    <c:out value="${game.room.host.user.username}"/>
                </td>
                <td>
                    <li class="dropdown"><a  class="dropdown-toggle"
						data-toggle="dropdown"> 
						<span class="glyphicon glyphicon-user"></span>
							<strong>Players</strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
                            <c:forEach items="${game.room.players}" var="player">
							<li>
								<div class="navbar-login">
									<div class="row">
										
											<p class="text-center">
												<c:out value="${player.user.username}"/>
											</p>
										
	
									</div>
								</div>
							</li>
							<li class="divider"></li>
                        </c:forEach>

						</ul></li>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</virus:layout>
