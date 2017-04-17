<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script src="${pageContext.request.contextPath}/js/delete_team.js" language="Javascript" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/excel_team.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <%--<script src="http://code.jquery.com/jquery-1.9.1.js"></script>--%>

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
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-primary" value="Import" onclick="importTeams(${race.id},${race.teamSize})">
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
                        <c:if test="${race.teamSize gt 1}">
                            <input type="button" id="delete_team" class="btn btn-danger" data-dismiss="modal"
                                   value="DELETE TEAM" style="margin: 0 0 5px 0; width: 100%">
                            <input type="button" id="delete_with_contestants" class="btn btn-danger"
                                   data-dismiss="modal" value="DELETE WITH CONTESTANTS"
                                   style="margin: 0 0 5px 0; width: 100%">
                        </c:if>
                        <c:if test="${race.teamSize eq 1}">
                            <input type="button" id="delete_with_contestants" class="btn btn-danger"
                                   data-dismiss="modal" value="DELETE" style="margin: 0 0 5px 0; width: 100%">
                        </c:if>
                        <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                               style="margin: 0; width: 100%">
                    </div>
                </div>
            </div>
        </div>

        <div class="card-log" style="margin-top: 25px;">
        <c:choose>
            <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                <div style="max-width: 650px; margin: 0 auto;">
                <div id="importResult"></div>
            </c:when>
            <c:otherwise>
                <div style="max-width: 500px; margin: 0 auto;">
            </c:otherwise>
        </c:choose>
        <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
            <div class="hidden-xs" style="margin-bottom: 20px;">
                <c:choose>
                    <c:when test="${race.teamSize > 1}">
                        <form:form action="${pageContext.request.contextPath}/race/${race.id}/exportTeams"
                                   method="post">
                            <input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"
                                   value="Import teams">
                            <c:if test="${teams.size() > 0}">
                                <input type="submit" class="btn btn-success" value="Export teams">
                            </c:if>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <form:form action="${pageContext.request.contextPath}/race/${race.id}/exportContestants"
                                   method="post">
                            <input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"
                                   value="Import teams">
                            <c:if test="${teams.size() > 0}">
                                <input type="submit" class="btn btn-success" value="Export teams">
                            </c:if>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="visible-xs" style="margin-bottom: 20px;">
                <c:choose>
                    <c:when test="${race.teamSize > 1}">
                        <form:form action="${pageContext.request.contextPath}/race/${race.id}/exportTeams"
                                   method="post">
                            <input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"
                                   value="Import teams" style="width: 100%">
                            <c:if test="${teams.size() > 0}">
                                <input type="submit" class="btn btn-success" value="Export teams" style="width: 100%">
                            </c:if>
                        </form:form>
                    </c:when>
                    <c:otherwise> <form:form
                            action="${pageContext.request.contextPath}/race/${race.id}/exportContestants"
                            method="post">
                        <input type="button" class="btn btn-primary" data-toggle="modal" data-target="#importTeams"
                               value="Import teams" style="width: 100%">
                        <c:if test="${teams.size() > 0}">
                            <input type="submit" class="btn btn-success" value="Export teams" style="width: 100%">
                        </c:if>
                    </form:form></c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${teams.size() > 0}">
                <c:forEach items="${teams}" var="team">
                    <div class="well" id="T${team.id}">
                        <c:if test="${race_cooperator eq true || race.user.id eq user.id || not empty race.teamCategory}">
                        <div class="row" style="margin-bottom: 20px;">
                            <c:if test="${race.teamSize gt 1}">
                                <c:choose>
                                    <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                                        <div class="col-sm-4" style="margin-bottom: 10px;">
                                            Team name:
                                            <input class="form-control" type="text" value="${team.name}" disabled>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-sm-6" style="margin-bottom: 10px;">
                                            Team name:
                                            <input class="form-control" type="text" value="${team.name}" disabled>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                            </c:if>
                            <c:if test="${race.teamCategory ne null}">
                                <c:choose>
                                    <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                                        <div class="col-sm-4" style="margin-bottom: 10px;">
                                            Team category:
                                            <input class="form-control" type="text" value="${team.category.name}"
                                                   disabled>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-sm-6" style="margin-bottom: 10px;">
                                            Team category:
                                            <input class="form-control" type="text" value="${team.category.name}"
                                                   disabled>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                            </c:if>
                            <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                <div class="col-sm-4 hidden-xs"
                                     style="text-align: right; margin-top: 20px; float: right;">
                                    <input type="button" value="Delete" class="btn btn-danger"
                                           data-toggle="modal" data-target="#deleteTeamModal"
                                           onclick="tagDeleteButtons(${race.id}, ${team.id})">
                                </div>
                                <div class="col-sm-4 visible-xs" style="text-align: right;">
                                    <input type="button" value="Delete" class="btn btn-danger"
                                           style="width: 100%"
                                           data-toggle="modal" data-target="#deleteTeamModal"
                                           onclick="tagDeleteButtons(${race.id}, ${team.id})">
                                </div>
                            </c:if>
                        </div>
                        </c:if>
                        <c:if test="${race.teamSize gt 1 || race.teamCategory ne null || race_cooperator eq true
                        || race.user.id eq user.id}">
                            <hr>
                        </c:if>
                        <c:if test="${race.teamSize gt 1 || race_cooperator eq true
                        || race.user.id eq user.id}">
                            <div class="row">
                                <div class="col-sm-4">
                                    Members:
                                </div>
                            </div>
                        </c:if>
                        <c:forEach items="${contestants}" var="c">
                            <c:if test="${c.team.id eq team.id}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <c:choose>
                                        <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                                            <div class="col-sm-3">
                                                <input class="form-control" type="text"
                                                       value="${c.firstname} ${c.lastname}"
                                                       disabled>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="col-sm-6">
                                                <input class="form-control" type="text"
                                                       value="${c.firstname} ${c.lastname}"
                                                       disabled>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                        <div class="col-sm-3">
                                            <input class="form-control" type="text" value="${c.email}" disabled>
                                        </div>
                                        <div class="col-sm-3">
                                            <input class="form-control" type="text" value="${c.phone}"
                                                   disabled>
                                        </div>
                                    </c:if>
                                    <c:if test="${race.contestantCategory ne null}">
                                        <c:choose>
                                            <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                                                <div class="col-sm-3">
                                                    <input class="form-control" type="text" value="${c.category.name}"
                                                           disabled>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="col-sm-6">
                                                    <input class="form-control" type="text" value="${c.category.name}"
                                                           disabled>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                    </c:if>

                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning">
                    List of teams / contestants is empty!
                </div>
            </c:otherwise>
        </c:choose>
        </div>
        </div>
    </jsp:body>
</t:template>