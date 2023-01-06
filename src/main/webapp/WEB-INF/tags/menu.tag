<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="https://tranjisgames.com/wp-content/uploads/2022/02/RULES_v1.10-0122-Cas-WAH_web.pdf" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'room'}" url="/room/myRoom"
					title="this is my room">
					<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
					<span>My room</span>
				</petclinic:menuItem>

				<sec:authorize access="isAuthenticated()">
				<petclinic:menuItem active="${name eq 'friend'}" url="/friend/myFriends"
					title="this is my friends">
					<span class="glyphicon glyphicon-heart-empty" aria-hidden="true"></span>
					<span>Friends</span>
				</petclinic:menuItem>
				</sec:authorize>

				<sec:authorize access="hasAnyAuthority('admin')">
				<petclinic:menuItem active="${name eq 'achievement'}" url="/statistics/achievements/"
					title="Achievements">
					<span class="glyphicon glyphicon-tasks" aria-hidden="true"></span>
					<span>Achievements</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<petclinic:menuItem active="${name eq 'ranking'}" url="/ranking/global"
					title="Top players">
					<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
					<span>Ranking</span>
				</petclinic:menuItem>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="hasAnyAuthority('admin')">
				<li class="dropdown"><a  class="dropdown-toggle"
						data-toggle="dropdown"> 
						<span class="glyphicon glyphicon-user"></span>
							<strong>Game</strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										
											<p class="text-center">
												<a href="<c:url value="/runningGames" />">
												<span class="glyphicon glyphicon-user icon-size">Running Game</span>
												</a>
											</p>
										
	
									</div>
								</div>
							</li>
							<li class="divider"></li>
							<li>
								<div class="navbar-login">
									<div class="row">
										
											<p class="text-center">
												<a href="<c:url value="/terminateGames" />">
												<span class="glyphicon glyphicon-user icon-size">Terminate Game</span>
												</a>
											</p>
										
	
									</div>
								</div>
							</li>

						</ul></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a  class="dropdown-toggle"
						data-toggle="dropdown"> 
						<span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<a href="<c:url value="/player/me" />">
												<span class="glyphicon glyphicon-user icon-size"></span>
												</a>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
