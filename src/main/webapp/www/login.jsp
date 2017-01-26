<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:template>
    <jsp:body>

    <div class="card-log" style="margin-top: 25px;">
        <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" />
        <form name="loginForm" action="<c:url value="/j_spring_security_check" />" method="POST">
            <input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Username" maxlength="32" name="username">
            <input type="password" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Password" maxlength="256" name="password">
            <div style="text-align: center;"><button class="btn btn-primary" type="submit" name="submit"><span style="color: white;">Sign in</span></button></div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </div>

    </jsp:body>
</t:template>