<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<t:template>
    <jsp:body>
        <div class="card-log" style="margin-top: 25px;">
            <form:form action="/addUser" modelAttribute="userRegistrationForm" method="POST">
                <div style="margin: 0 auto; max-width: 250px;">Username:</div>
                <form:input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control"
                       placeholder="Username" maxlength="32" path="user.login"/>
                <div style="margin: 0 auto; max-width: 250px;">Firstname:</div>
                <form:input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control"
                       placeholder="Firstname" maxlength="32" path="user.firstname"/>
                <div style="margin: 0 auto; max-width: 250px;">Surname:</div>
                <form:input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control"
                       placeholder="Surname" maxlength="32" path="user.surname"/>
                <div style="margin: 0 auto; max-width: 250px;">Phone:</div>
                <form:input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control"
                       placeholder="Phone" maxlength="16" path="user.phone"/>
                <div style="margin: 0 auto; max-width: 250px;">Email:</div>
                <form:input type="email" style="margin: 0 auto 20px; max-width: 250px;" class="form-control"
                       placeholder="Email" maxlength="32" path="user.email"/>
                <div style="margin: 0 auto; max-width: 250px;">Password:</div>
                <form:input type="password" style="margin: 0 auto 7px; max-width: 250px;" class="form-control"
                       placeholder="Password" maxlength="256" path="user.password"/>
                <div style="margin: 0 auto; max-width: 250px;">Password again:</div>
                <form:input type="password" style="margin: 0 auto 20px; max-width: 250px;" class="form-control"
                       placeholder="Password again" path="passwordAgain"/>
                <div style="text-align: center;">
                    <button class="btn btn-primary" type="submit" name="submit"><span
                            style="color: white;">Registration</span></button>
                </div>
                <input type="hidden" path="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form:form>
        </div>
    </jsp:body>
</t:template>