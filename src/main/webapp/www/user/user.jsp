<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:template>
    <jsp:body>
        <div class="card-log" style="margin-top: 35px;">
            <div style="max-width: 400px; margin: 0 auto;">
                <div class="well" style="text-align: center;">My profile</div>
                        Login:
                        <input type="text" class="form-control" value="${user.login}" style="margin-bottom: 10px;">
                        Firstname:
                        <input type="text" class="form-control" value="${user.firstname}" style="margin-bottom: 10px;">
                        Lastname:
                        <input type="text" class="form-control" value="${user.surname}" style="margin-bottom: 10px;">
                        Email:
                        <input type="email" class="form-control" value="${user.email}" style="margin-bottom: 10px;">
                        Phone:
                        <input type="text" class="form-control" value="${user.phone}" style="margin-bottom: 10px;">
            </div>
        </div>
    </jsp:body>
</t:template>