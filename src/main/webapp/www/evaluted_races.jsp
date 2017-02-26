<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>


        <div style="margin: 30px 0 30px 0;">
            <c:forEach var="listValue" items="${races}">
                <div style="margin: 0 auto; max-width: 400px;">
                    <div class="row">
                        <div class="col-sm-8" style="margin-top: 6px">
                            <a href="${pageContext.request.contextPath}/race/${listValue.id}"
                               style="text-decoration: none; color: grey;"
                               class="link_hover"> ${listValue.name}</a>
                        </div>
                        <div class="col-sm-4">
                            <a href="${pageContext.request.contextPath}/race/${listValue.id}/results">
                                <button type="button" class="btn btn-success btn-sm">Results</button>
                            </a>
                        </div>
                    </div>
                </div>
                <br>
            </c:forEach>
        </div>


    </jsp:body>
</t:template>