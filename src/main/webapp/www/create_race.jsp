<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<t:template>
    <jsp:body>

        <div class="card-log" style="margin-top: 25px;">
            <form name="createEventForm" action="<c:url value="/create_event" />" method="POST">
                <div style="margin: 0 auto; max-width: 250px;">Race name:</div><input type="text" style="margin: 0 auto 7px; max-width: 250px;" class="form-control" placeholder="Race name" maxlength="50" name="name">
                <div style="text-align: center;"><button class="btn btn-primary" type="submit" name="submit"><span style="color: white;">Create race</span></button></div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </div>

    </jsp:body>
</t:template>
