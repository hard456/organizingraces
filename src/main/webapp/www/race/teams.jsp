<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

        <script src="${pageContext.request.contextPath}/js/teams.js" language="Javascript"
                type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/js/excel_team.js" language="Javascript"
                type="text/javascript"></script>

        <%-- Import teams modal --%>

        <div class="modal fade" id="importTeams" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Import teams from EXCEL</h4>
                        </div>
                    </div>
                    <div class="modal-body">
                        <label class="btn btn-primary" for="my-file-selector">
                            <input name="file" id="my-file-selector" type="file" style="display:none;"
                                   onchange="$('#upload-file-info').html(this.files[0].name);"
                                   accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                            Import XLSX file
                        </label>
                        <span id="upload-file-info"></span>

                        <div class="loader" id="loader" style="margin: 20px auto 20px auto; display: none"></div>

                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-primary" value="Import"
                               onclick="importTeams(${race.id},${race.teamSize})">
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                    </div>
                </div>
            </div>
        </div>

        <!-- Result delete modal -->
        <div class="modal" id="deleteResultModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete result</h4>
                        </div>
                        <div id="delete_result" style="margin-bottom: 10px;"></div>
                        <div style="text-align: right">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                                   style="margin-bottom: 5px;">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Save danger result -->
        <div class="modal" id="updatetModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete result</h4>
                        </div>
                        <div id="save_result" style="margin-bottom: 10px;"></div>
                        <div style="text-align: right">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                                   style="margin-bottom: 5px;">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete team modal -->
        <div class="modal" id="deleteTeamModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Delete team</h4>
                    </div>
                    <div class="modal-body">
                        <p>Would you like to delete the team?</p>
                    </div>
                    <div class="modal-footer">
                        <input type="button" id="delete_team" class="btn btn-danger" data-dismiss="modal"
                               value="DELETE TEAM" style="margin: 0 0 5px 0; width: 100%">
                        <input type="button" id="delete_with_contestants" class="btn btn-danger"
                               data-dismiss="modal" value="DELETE WITH CONTESTANTS"
                               style="margin: 0 0 5px 0; width: 100%">
                        <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                               style="margin: 0; width: 100%">
                    </div>
                </div>
            </div>
        </div>

        <div class="card-log" style="margin-top: 25px;">
            <c:choose>
                <c:when test="${teams.size() eq 0 || race.teamSize eq 1}">
                    <div style="max-width: 850px; margin: 0 auto;">
                        <div class="alert alert-warning">
                            <c:choose>
                                <c:when test="${race.teamSize eq 1}">
                                    This section is disabled for this race.
                                </c:when>
                                <c:otherwise>
                                    List of teams is empty!
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                            <div style="max-width: 1100px; margin: 0 auto;">
                                <div id="importResult"></div>
                                <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                    <div style="margin-bottom: 20px;">
                                        <form:form
                                                action="${pageContext.request.contextPath}/race/${race.id}/exportTeams"
                                                method="post">
                                            <input type="button" class="btn btn-primary" data-toggle="modal"
                                                   data-target="#importTeams"
                                                   value="Import teams">
                                            <input type="submit" class="btn btn-success" value="Export teams">
                                        </form:form>
                                    </div>
                                </c:if>


                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Team</th>
                                            <th>Members</th>
                                            <th>Team&nbsp;category</th>
                                            <th>Save</th>
                                            <th>Delete</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${teams}" var="team" varStatus="i">
                                            <tr id="T${team.id}">
                                                <td style="vertical-align: middle;">${team.id}</td>
                                                <td style="vertical-align: middle;"><input class="form-control" id="teamName${team.id}"
                                                                                           type="text" maxlength="32"
                                                                                           value="${team.name}" style="width: auto;"></td>
                                                <td style="vertical-align: middle;">${contestants.get(i.index)}</td>
                                                <td style="vertical-align: middle;">
                                                    <c:if test="${team.category ne null}">
                                                        <select class="form-control" id="teamCategory${team.id}" style="width: auto;">
                                                            <c:forEach items="${team_categories}" var="category">
                                                                <c:choose>
                                                                    <c:when test="${team.category.id eq category.id}">
                                                                        <option value="${category.id}"
                                                                                selected>${category.name}</option>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <option value="${category.id}">${category.name}</option>
                                                                    </c:otherwise>
                                                                </c:choose>

                                                            </c:forEach>
                                                        </select>
                                                    </c:if>
                                                </td>
                                                <td style="vertical-align: middle;"><input type="button" value="Save"
                                                           class="btn btn-primary btn-sm"
                                                           onclick="updateTeam(${race.id}, ${team.id})">
                                                </td>
                                                <td style="vertical-align: middle;"><input type="button" value="Delete"
                                                           class="btn btn-danger btn-sm"
                                                           data-toggle="modal" data-target="#deleteTeamModal"
                                                           onclick="tagDeleteButtons(${race.id}, ${team.id})"></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="max-width: 950px; margin: 0 auto;">
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>Team</th>
                                            <th>Members</th>
                                            <th>Team&nbsp;category</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${teams}" var="team" varStatus="i">
                                            <tr id="T${team.id}">
                                                <td style="vertical-align: middle;">${team.name}</td>
                                                <td style="vertical-align: middle;">${contestants.get(i.index)}</td>
                                                <td style="vertical-align: middle;">${team.category.name}</td>
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
        </div>

    </jsp:body>
</t:template>



















        <%--<div class="card-log" style="margin-top: 25px;">--%>
        <%--<c:choose>--%>
            <%--<c:when test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                <%--<div style="max-width: 650px; margin: 0 auto;">--%>
                <%--<div id="importResult"></div>--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                <%--<c:choose>--%>
                    <%--<c:when test="${teams.size() > 0}">--%>
                        <%--<div style="max-width: 500px; margin: 0 auto;">--%>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>
                        <%--<div style="max-width: 850px; margin: 0 auto;">--%>
                    <%--</c:otherwise>--%>
                <%--</c:choose>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <%--<c:if test="${race_cooperator eq true || race.user.id eq user.id}">--%>
            <%--<div class="hidden-xs" style="margin-bottom: 20px;">--%>
                <%--<form:form action="${pageContext.request.contextPath}/race/${race.id}/exportTeams"--%>
                           <%--method="post">--%>
                    <%--<input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"--%>
                           <%--value="Import teams">--%>
                        <%--<input type="submit" class="btn btn-success" value="Export teams">--%>
                <%--</form:form>--%>
            <%--</div>--%>
            <%--<div class="visible-xs" style="margin-bottom: 20px;">--%>
                <%--<form:form action="${pageContext.request.contextPath}/race/${race.id}/exportTeams"--%>
                           <%--method="post">--%>
                    <%--<input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"--%>
                           <%--value="Import teams" style="width: 100%; margin-bottom: 5px;">--%>
                    <%--<input type="submit" class="btn btn-success" value="Export teams" style="width: 100%">--%>
                <%--</form:form>--%>
            <%--</div>--%>
        <%--</c:if>--%>

        <%--<c:choose>--%>
            <%--<c:when test="${teams.size() > 0}">--%>
                <%--<c:forEach items="${teams}" var="team">--%>
                    <%--<div class="well" id="T${team.id}">--%>
                        <%--<div class="row" style="margin-bottom: 20px;">--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                    <%--<div class="col-sm-3">--%>
                                        <%--Team name:--%>
                                        <%--<input class="form-control" type="text" value="${team.name}"--%>
                                               <%--id="teamName${team.id}">--%>
                                    <%--</div>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--<div class="col-sm-6">--%>
                                        <%--Team name:--%>
                                        <%--<input class="form-control" type="text" value="${team.name}">--%>
                                    <%--</div>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                            <%--<c:if test="${race.teamCategory ne null}">--%>
                                <%--<c:choose>--%>
                                    <%--<c:when test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                        <%--<div class="col-sm-3">--%>
                                            <%--Team category:--%>
                                            <%--<select class="form-control" id="teamCategory${team.id}">--%>
                                                <%--<c:forEach items="${team_categories}" var="category">--%>
                                                    <%--<c:choose>--%>
                                                        <%--<c:when test="${team.category.id eq category.id}">--%>
                                                            <%--<option value="${category.id}"--%>
                                                                    <%--selected>${category.name}</option>--%>
                                                        <%--</c:when>--%>
                                                        <%--<c:otherwise>--%>
                                                            <%--<option value="${category.id}">${category.name}</option>--%>
                                                        <%--</c:otherwise>--%>
                                                    <%--</c:choose>--%>

                                                <%--</c:forEach>--%>
                                            <%--</select>--%>
                                        <%--</div>--%>
                                    <%--</c:when>--%>
                                    <%--<c:otherwise>--%>
                                        <%--<div class="col-sm-6">--%>
                                            <%--Team category:--%>
                                            <%--<input class="form-control" type="text" value="${team.category.name}">--%>
                                        <%--</div>--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>

                            <%--</c:if>--%>
                            <%--<c:if test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                <%--<div class="col-sm-3 hidden-xs"--%>
                                     <%--style="text-align: right; margin-top: 20px; float: right;">--%>
                                    <%--<input type="button" value="Delete" style="width: 100%;" class="btn btn-danger"--%>
                                           <%--data-toggle="modal" data-target="#deleteTeamModal"--%>
                                           <%--onclick="tagDeleteButtons(${race.id}, ${team.id})">--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-3 hidden-xs"--%>
                                     <%--style="text-align: right; margin-top: 20px; float: right;">--%>
                                    <%--<input type="button" value="Save" style="width: 100%;" class="btn btn-success"--%>
                                           <%--onclick="updateTeam(${race.id}, ${team.id})">--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-3 visible-xs" style="margin-top: 5px;">--%>
                                    <%--<input type="button" value="Save" class="btn btn-success"--%>
                                           <%--style="width: 100%" onclick="updateTeam(${race.id}, ${team.id})">--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-3 visible-xs" style="margin-top: 5px;">--%>
                                    <%--<input type="button" value="Delete" class="btn btn-danger"--%>
                                           <%--style="width: 100%"--%>
                                           <%--data-toggle="modal" data-target="#deleteTeamModal"--%>
                                           <%--onclick="tagDeleteButtons(${race.id}, ${team.id})">--%>
                                <%--</div>--%>
                            <%--</c:if>--%>
                        <%--</div>--%>
                        <%--<hr>--%>
                        <%--<div class="row">--%>
                            <%--<div class="col-sm-4">--%>
                                <%--Members:--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<c:forEach items="${contestants}" var="c">--%>
                            <%--<c:if test="${c.team.id eq team.id}">--%>
                                <%--<div class="row" style="margin-bottom: 5px;">--%>
                                    <%--<c:choose>--%>
                                        <%--<c:when test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                            <%--<div class="col-sm-3">--%>
                                                <%--<input class="form-control" type="text"--%>
                                                       <%--value="${c.firstname} ${c.lastname}">--%>
                                            <%--</div>--%>
                                        <%--</c:when>--%>
                                        <%--<c:otherwise>--%>
                                            <%--<div class="col-sm-6">--%>
                                                <%--<input class="form-control" type="text"--%>
                                                       <%--value="${c.firstname} ${c.lastname}">--%>
                                            <%--</div>--%>
                                        <%--</c:otherwise>--%>
                                    <%--</c:choose>--%>

                                    <%--<c:if test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                        <%--<c:if test="${c.email ne null && c.email.length() > 0}">--%>
                                            <%--<div class="col-sm-3">--%>
                                                <%--<input class="form-control" type="text" value="${c.email}">--%>
                                            <%--</div>--%>
                                        <%--</c:if>--%>
                                        <%--<c:if test="${c.phone ne null && c.phone.length() > 0}">--%>
                                            <%--<div class="col-sm-3">--%>
                                                <%--<input class="form-control" type="text" value="${c.phone}">--%>
                                            <%--</div>--%>
                                        <%--</c:if>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${race.contestantCategory ne null}">--%>
                                        <%--<c:choose>--%>
                                            <%--<c:when test="${race_cooperator eq true || race.user.id eq user.id}">--%>
                                                <%--<div class="col-sm-3">--%>
                                                    <%--<input class="form-control" type="text" value="${c.category.name}">--%>
                                                <%--</div>--%>
                                            <%--</c:when>--%>
                                            <%--<c:otherwise>--%>
                                                <%--<div class="col-sm-6">--%>
                                                    <%--<input class="form-control" type="text" value="${c.category.name}">--%>
                                                <%--</div>--%>
                                            <%--</c:otherwise>--%>
                                        <%--</c:choose>--%>

                                    <%--</c:if>--%>

                                <%--</div>--%>
                            <%--</c:if>--%>
                        <%--</c:forEach>--%>
                    <%--</div>--%>
                <%--</c:forEach>--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                <%--<div class="alert alert-warning">--%>
                    <%--List of teams is empty!--%>
                <%--</div>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
        <%--</div>--%>
        <%--</div>--%>
    <%--</jsp:body>--%>
<%--</t:template>--%>