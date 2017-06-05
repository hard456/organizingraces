<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script src="${pageContext.request.contextPath}/js/create_team.js" language="Javascript"
        type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 1100px; margin: 0 auto;">

                <c:choose>
                    <c:when test="${race.teamSize gt 1}">
                        <c:choose>
                            <c:when test="${empty contestants}">
                                <div style="max-width: 850px; margin: 0 auto;">
                                    <div class="alert alert-warning">
                                        List of solo contestants is empty!
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${race_cooperator eq true || race.user.id eq user.id}">

                                        <div id="create_team_result"></div>
                                        <form:form id="createTeamForm" method="POST">
                                            <div class="row hidden-xs">
                                                <div class="col-sm-5">
                                                    Team name:
                                                </div>
                                                <c:if test="${race.teamCategory ne null}">
                                                    <div class="col-sm-5">
                                                        Team category:
                                                    </div>
                                                </c:if>
                                            </div>

                                            <div class="row">
                                                <div class="col-sm-5">
                                                    <div class="visible-xs">Team name:</div>
                                                    <input class="form-control" style="margin-bottom: 5px;"
                                                           id="teamName"
                                                           placeholder="Team name" maxlength="32"/>
                                                </div>
                                                <c:if test="${race.teamCategory ne null}">
                                                    <div class="col-sm-5">
                                                        <div class="visible-xs">Category:</div>
                                                        <select class="form-control" style="margin-bottom: 5px;"
                                                                id="teamCategory">
                                                            <c:forEach items="${team_categories}" var="c">
                                                                <option selected value="${c.id}">${c.name}</option>
                                                            </c:forEach>

                                                        </select>
                                                    </div>
                                                </c:if>
                                                <div class="col-sm-2" style="text-align: right;">
                                                    <input type="button" class="btn btn-primary visible-xs"
                                                           onclick="createTeamAjax(${race.id});" value="Registration"
                                                           style="margin-bottom: 5px; width: 100%">
                                                    <input type="button" class="btn btn-primary hidden-xs"
                                                           onclick="createTeamAjax(${race.id});" value="Registration"
                                                           style="margin-bottom: 5px;">
                                                </div>
                                            </div>

                                            <br><br>

                                            <div class="table-responsive">
                                                <table class="table table-striped">
                                                    <thead>
                                                    <tr>
                                                        <th></th>
                                                        <th>Firstname</th>
                                                        <th>Lastname</th>
                                                        <th>Email</th>
                                                        <th>Phone</th>
                                                        <th>Category</th>
                                                        <th>Delete</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var="c" varStatus="i" items="${contestants}">
                                                        <tr id="C${c.id}">

                                                            <td style="vertical-align: middle">
                                                                <input type="button" id="B${c.id}"
                                                                       class="btn btn-success btn-sm"
                                                                       onclick="addToTeamList(${c.id});" value="+"
                                                                       style="margin-bottom: 5px;  width: 28px; text-align: center; vertical-align: center;">
                                                            </td>

                                                            <td style="vertical-align: middle">${c.firstname}</td>
                                                            <td style="vertical-align: middle">${c.lastname}</td>
                                                            <td style="vertical-align: middle">${c.email}</td>
                                                            <td style="vertical-align: middle">${c.phone}</td>
                                                            <td style="vertical-align: middle">${c.category.name}</td>
                                                            <td style="vertical-align: middle">
                                                                <input type="button" class="btn btn-danger btn-md"
                                                                       value="Delete"
                                                                       style="margin-bottom: 5px;"
                                                                       onclick="deleteSoloContestant(${c.id},${race.id});">
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <%--<div id="C${c.id}">--%>
                                            <%--<div class="row">--%>

                                            <%--<div class="col-sm-1">--%>
                                            <%--<input type="button" id="B${c.id}"--%>
                                            <%--class="btn btn-success btn-sm"--%>
                                            <%--onclick="addToTeamList(${c.id});" value="+"--%>
                                            <%--style="margin-bottom: 5px;  width: 28px; text-align: center; vertical-align: center;">--%>
                                            <%--</div>--%>

                                            <%--<div class="col-sm-2"><input type="text" value="${c.firstname}"--%>
                                            <%--class="form-control"--%>
                                            <%--style="margin-bottom: 5px;" disabled></div>--%>
                                            <%--<div class="col-sm-2"><input type="text" value="${c.lastname}"--%>
                                            <%--class="form-control"--%>
                                            <%--style="margin-bottom: 5px;" disabled></div>--%>
                                            <%--<div class="col-sm-3">--%>
                                            <%--<c:if test="${not empty c.email && not empty c.phone}">--%>
                                            <%--<input type="text" value="${c.email}, ${c.phone}" class="form-control" style="margin-bottom: 5px;" disabled>--%>
                                            <%--</c:if>--%>
                                            <%--<c:if test="${not empty c.email && empty c.phone}">--%>
                                            <%--<input type="text" value="${c.email}" class="form-control" style="margin-bottom: 5px;" disabled>--%>
                                            <%--</c:if>--%>
                                            <%--<c:if test="${empty c.email && not empty c.phone}">--%>
                                            <%--<input type="text" value="${c.phone}" class="form-control" style="margin-bottom: 5px;" disabled>--%>
                                            <%--</c:if>--%>
                                            <%--<c:if test="${empty c.email && empty c.phone}">--%>
                                            <%--<input type="text" value="" class="form-control" style="margin-bottom: 5px;" disabled>--%>
                                            <%--</c:if>--%>
                                            <%--</div>--%>

                                            <%--<div class="col-sm-2"><input type="text"--%>
                                            <%--value="${c.category.name}"--%>
                                            <%--class="form-control"--%>
                                            <%--style="margin-bottom: 5px;" disabled></div>--%>
                                            <%--<div class="col-sm-2" style="text-align: right">--%>
                                            <%--<input type="button" class="btn btn-danger btn-md"--%>
                                            <%--value="Delete"--%>
                                            <%--style="margin-bottom: 5px;" onclick="deleteSoloContestant(${c.id},${race.id});">--%>
                                            <%--</div>--%>
                                            <%--</div>--%>
                                            <%--<c:if test="${not i.last}">--%>
                                            <%--<hr>--%>
                                            <%--</c:if>--%>
                                            <%--</div>--%>
                                        </form:form>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="max-width: 950px; margin: 0 auto;">
                                            <div class="table-responsive">
                                                <table class="table table-striped">
                                                    <thead>
                                                    <tr>
                                                        <th>Firstname</th>
                                                        <th>Lastname</th>
                                                        <th>Conestant&nbsp;category</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var="c" varStatus="i" items="${contestants}">
                                                        <tr>
                                                            <td style="vertical-align: middle">${c.firstname}</td>
                                                            <td style="vertical-align: middle">${c.lastname}</td>
                                                            <td style="vertical-align: middle">${c.category.name}</td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger">
                            This section is not supported.
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </jsp:body>
</t:template>
