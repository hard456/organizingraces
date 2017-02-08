<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <c:forEach var="listValue" items="${list}">
                    <div style="text-align: center"><a href="${pageContext.request.contextPath}/race/${listValue.id}"> ${listValue.name}</a></div><br>
        </c:forEach>

    </jsp:body>
</t:template>