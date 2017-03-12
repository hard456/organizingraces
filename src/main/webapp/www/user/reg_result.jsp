<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:template>
    <jsp:body>

        <div style="margin: 35px auto 35px;">
        <div style="max-width: 850px; margin: 0 auto;">

            <c:if test="${invalid eq true}">
                <div class="alert alert-danger">${message}</div>
            </c:if>
            <c:if test="${invalid eq false}">
                <div class="alert alert-success">${message}</div>
            </c:if>
        </div>
        </div>

    </jsp:body>
</t:template>