<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>
        <div style="margin: 0 auto; max-width: 400px;">
            <div class="card-log" style="margin: 30px auto;">
                <div class="row">
                    <div class="col-sm-6"><a data-toggle="tab" href="#my_races"><input type="button" style="width: 100%"
                                                                                       class="btn btn-default"
                                                                                       value="My races"></a></div>
                    <div class="col-sm-6"><a data-toggle="tab" href="#supported_races"><input type="button"
                                                                                              style="width: 100%"
                                                                                              class="btn btn-default"
                                                                                              value="Supported races"></a>
                    </div>
                </div>

                <div class="tab-content">
                    <div id="my_races" class="tab-pane fade in active">

                        <div style="margin-top: 30px;">
                            <c:choose>
                                <c:when test="${empty myRaces}">
                                    <div class="alert alert-warning">
                                        List of your races is empty.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="m" varStatus="i" items="${myRaces}">

                                        <div class="row">
                                            <div class="col-sm-8" style="margin-top: 6px;">
                                                <a href="${pageContext.request.contextPath}/race/${m.id}"
                                                   style="text-decoration: none; color: grey;"
                                                   class="link_hover"> ${m.name}</a>
                                            </div>
                                            <div class="col-sm-4" style="text-align: right">
                                                <div class="hidden-xs">
                                                    <a href="${pageContext.request.contextPath}/race/${m.id}/delete">
                                                        <button type="button" class="btn btn-danger btn-sm">Delete
                                                        </button>
                                                    </a>
                                                </div>
                                                <div class="visible-xs">
                                                    <a href="${pageContext.request.contextPath}/race/${m.id}/delete">
                                                        <button type="button" class="btn btn-danger btn-sm"
                                                                style="margin-top: 5px; width: 100%">
                                                            Delete
                                                        </button>
                                                    </a>
                                                </div>

                                            </div>
                                        </div>
                                        <c:if test="${not i.last}">
                                            <hr>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div id="supported_races" class="tab-pane fade">
                        <div style="margin-top: 30px;">
                            <c:choose>
                                <c:when test="${empty supportedRaces}">
                                    <div class="alert alert-warning">
                                        Do not support any race.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="s" varStatus="i" items="${supportedRaces}">

                                        <div class="row">
                                            <div class="col-sm-8" style="margin-top: 6px;">
                                                <a href="${pageContext.request.contextPath}/race/${s.race.id}"
                                                   style="text-decoration: none; color: grey;"
                                                   class="link_hover"> ${s.race.name}</a>
                                            </div>
                                            <div class="col-sm-4" style="text-align: right">
                                                <div class="hidden-xs">
                                                    <a href="${pageContext.request.contextPath}/race/${s.race.id}/registration">
                                                        <button type="button" class="btn btn-success btn-sm">
                                                            Registration
                                                        </button>
                                                    </a>
                                                </div>
                                                <div class="visible-xs">
                                                    <a href="${pageContext.request.contextPath}/race/${s.race.id}/registration">
                                                        <button type="button" class="btn btn-success btn-sm"
                                                                style="margin-top: 5px; width: 100%">
                                                            Registration
                                                        </button>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <c:if test="${not i.last}">
                                            <hr>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </jsp:body>
</t:template>
