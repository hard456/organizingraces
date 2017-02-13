<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:template>
    <jsp:body>
        <img id="profile-img" class="profile-img-card" src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" style="margin: 35px auto 35px;"/>
        <div style="margin: 35px auto 0; text-align: center;">USER ${user.login}</div>
        <div style="margin: 5px auto 0; text-align: center;">${user.firstname} ${user.surname}</div>
        <div style="margin: 5px auto 35px; text-align: center;">${user.email}</div>
    </jsp:body>
</t:template>