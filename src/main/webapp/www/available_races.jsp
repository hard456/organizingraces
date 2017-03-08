<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>
        <div class="card-log" style="margin: 30px auto;">
            <c:choose>
                <c:when test="${empty races}">
                    <div style="max-width: 850px; margin: 0 auto;">
                        <div class="alert alert-warning">
                            No races available.
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="margin: 0 auto; max-width: 400px;">
                        <c:forEach var="listValue" varStatus="i" items="${races}">

                            <div class="row">
                                <div class="col-sm-8" style="margin-top: 6px;">
                                    <a href="${pageContext.request.contextPath}/race/${listValue.id}"
                                       style="text-decoration: none; color: grey;"
                                       class="link_hover"> ${listValue.name}</a>
                                </div>
                                <div class="col-sm-4" style="text-align: right">
                                    <div class="hidden-xs">
                                        <a href="${pageContext.request.contextPath}/race/${listValue.id}/registration">
                                            <button type="button" class="btn btn-success btn-sm">Registration</button>
                                        </a>
                                    </div>
                                    <div class="visible-xs">
                                        <a href="${pageContext.request.contextPath}/race/${listValue.id}/registration">
                                            <button type="button" class="btn btn-success btn-sm" style="margin-top: 5px; width: 100%">
                                                Registration
                                            </button>
                                        </a>
                                    </div>

                                </div>
                            </div>
                            <c:if test="${not i.last}"><hr></c:if>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:template>
