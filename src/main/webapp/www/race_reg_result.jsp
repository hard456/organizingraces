<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

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

        <c:forEach var="c" items="${request}">
            ${c.key},
        </c:forEach>

    </jsp:body>
</t:template>
