<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu />

        <c:forEach var="c" items="${contestants}">
            ${c.id},${c.category},${c.email},${c.firstname},${c.lastname},${c.paid},${c.raceId}<br>
        </c:forEach>

    </jsp:body>
</t:template>
