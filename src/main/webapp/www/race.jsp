<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <sec:authorize access="isAuthenticated()">
            <nav class="navbar-default" data-offset-top="140" style="z-index:1000; margin: 0 auto; max-width: 1000px">
                <div class="container-fluid" style="margin: 0 auto; background-color: white;">
                    <ul class="nav navbar-nav">
                        <li><a href="#">Page 2</a></li>
                        <li><a href="#">Page 3</a></li>
                    </ul>
                </div>
            </nav>
        </sec:authorize>

        ${id}

    </jsp:body>
</t:template>