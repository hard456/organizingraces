<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<t:template>
    <jsp:body>
        <div style="max-width: 550px; margin: 0 auto;">
            <div class="card-log" style="margin-top: 25px;">
                <c:if test="${not empty message}">
                    <div class="alert alert-danger" style="margin-bottom: 15px;">
                            ${message}
                    </div>
                </c:if>
                <form:form action="${pageContext.request.contextPath}/addUser" modelAttribute="userRegistrationForm"
                           method="POST">
                    Username<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                    <form:input type="text" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Username" maxlength="32" path="user.login"/>
                    Firstname<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                    <form:input type="text" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Firstname" maxlength="32" path="user.firstname"/>
                    Surname<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                    <form:input type="text" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Surname" maxlength="32" path="user.surname"/>
                    Phone <span style="color: darkgrey">(123456789, +420123456789)</span>:
                    <form:input type="text" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Phone" maxlength="16" path="user.phone"/>
                    Email<span style="color: red;">*</span> <span style="color: darkgrey">(6 - 32 length)</span>:
                    <form:input type="email" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Email" maxlength="32" path="user.email"/>
                    Password<span style="color: red;">*</span> <span style="color: darkgrey">(8 - 256 length)</span>:
                    <form:input type="password" style="margin-botoom: 7px; max-width: 100%;" class="form-control"
                                placeholder="Password" maxlength="256" path="user.password"/>
                    Password again<span style="color: red;">*</span> <span
                        style="color: darkgrey">(8 - 256 length)</span>:
                    <form:input type="password" style="margin-bottom: 20px; max-width: 100%;" class="form-control"
                                placeholder="Password again" path="passwordAgain"/>
                    <div style="text-align: center;">
                        <button class="btn btn-primary" type="submit" name="submit"><span
                                style="color: white;">Registration</span></button>
                    </div>
                    <input type="hidden" path="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:template>