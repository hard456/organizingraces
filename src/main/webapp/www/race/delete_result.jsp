<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <c:if test="${invalid eq true}">
            <t:race_menu/>
        </c:if>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 850px; margin: 0 auto;">
                <c:if test="${invalid eq true}">
                    <div class="alert alert-danger">
                            ${result}
                    </div>
                </c:if>
                <c:if test="${invalid eq false}">
                    <div class="alert alert-success">
                            ${result}
                    </div>
                </c:if>
            </div>
        </div>

    </jsp:body>
</t:template>
