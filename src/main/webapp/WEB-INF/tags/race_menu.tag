<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<hr style="margin: 0; padding: 0;">

<nav class="navbar-default" data-offset-top="140" style="z-index:1000; margin: 0 auto; max-width: 1000px">
    <div class="container-fluid" style="margin: 0 auto; background-color: white;">
        <ul class="nav navbar-nav">
            <li><a href="${pageContext.request.contextPath}/race/${race.id}">Event</a></li>
            <sec:authorize access="isAuthenticated()">
                <li><a href="${pageContext.request.contextPath}/race/${race.id}/registration">Race registration</a></li>
            </sec:authorize>
            <c:if test="${race.teamSize gt 1}">
                <li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants/solo">Solo contestants</a>
                </li>
            </c:if>
                <c:choose>
                    <c:when test="${race.teamSize gt 1}">
                        <li><a href="${pageContext.request.contextPath}/race/${race.id}/teams">Teams</a></li>
                </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants">Contestants</a></li>
                    </c:otherwise>
                </c:choose>

            <c:if test="${race.teamSize gt 1}">
                <li><a href="${pageContext.request.contextPath}/race/${race.id}/contestants/full_list">Manage
                    contestants</a></li>
            </c:if>
            <li><a href="${pageContext.request.contextPath}/race/${race.id}/results">Results</a></li>
        </ul>
    </div>
</nav>