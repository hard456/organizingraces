<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:template>
    <jsp:body>
        ${er}
        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 850px; margin: 0 auto;">
                <c:if test="${invalid eq true}">
                    <div class="alert alert-danger">
                        Data are invalid:<br>
                        Team name (3 - 32 length)<br>
                        If you use custom categories (1 - 20 length)
                    </div>
                </c:if>
                <c:if test="${result eq false}">
                    <div class="alert alert-danger">
                        The race with this name already exists.
                    </div>
                </c:if>
                <c:if test="${result eq true}">
                    <div class="alert alert-success">
                        The race was created.
                    </div>
                </c:if>
            </div>
        </div>

    </jsp:body>
</t:template>
