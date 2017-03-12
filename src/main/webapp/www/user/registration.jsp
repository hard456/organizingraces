<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<t:template>
    <jsp:body>
        <div class="card-log" style="margin-top: 25px;">
            <form name="registrationForm" action="<c:url value="/addUser" />" method="POST">
                <div style="margin: 0 auto; max-width: 250px;">Username:</div><input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Username" maxlength="32" name="login">
                <div style="margin: 0 auto; max-width: 250px;">Firstname:</div><input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Firstname" maxlength="32" name="firstname">
                <div style="margin: 0 auto; max-width: 250px;">Surname:</div><input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Surname" maxlength="32" name="surname">
                <div style="margin: 0 auto; max-width: 250px;">Phone:</div><input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Phone" maxlength="16" name="phone">
                <div style="margin: 0 auto; max-width: 250px;">Email:</div><input type="email" style="margin: 0 auto 20px; max-width: 250px;" class="form-control" placeholder="Email" maxlength="32" name="email">
                <div style="margin: 0 auto; max-width: 250px;">Password:</div><input type="password" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Password" maxlength="256" name="password">
                <div style="margin: 0 auto; max-width: 250px;">Password again:</div><input type="password" style="margin: 0 auto 20px; max-width: 250px;" class="form-control" placeholder="Password again" name="passwordAgain">
                <div style="text-align: center;"><button class="btn btn-primary" type="submit" name="submit"><span style="color: white;">Registration</span></button></div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>
    </jsp:body>
</t:template>