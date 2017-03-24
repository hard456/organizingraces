<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<hr style="margin: 0; padding: 0;">

    <nav class="navbar-default" data-offset-top="140" style="z-index:1000; margin: 0 auto; max-width: 1000px">
        <div class="container-fluid" style="margin: 0 auto; background-color: white;">
            <ul class="nav navbar-nav">
                <li><a href="/race/${race.id}">Event</a></li>
                <sec:authorize access="isAuthenticated()"><li><a href="${pageContext.request.contextPath}/race/${race.id}/registration">Race registration</a></li></sec:authorize>
                <c:if test="${race.teamSize gt 1}"><li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants/solo">Solo contestants</a></li></c:if>
                <li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants/teams">Teams</a></li>
                <li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants/full_list">List of contestants</a></li>
                <c:if test="${race.evaluation == true}"> <li><a href="${pageContext.request.contextPath}/race/${race.id}/results">Results</a></li></c:if>
            </ul>
        </div>
    </nav>