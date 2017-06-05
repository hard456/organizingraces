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
        <script src="${pageContext.request.contextPath}/js/contestant_list.js" language="Javascript"
                type="text/javascript"></script>

        <%-- Import contestants modal --%>
        <div class="modal fade" id="importTeams" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Import contestants from EXCEL</h4>
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

        <!-- Delete contestant modal -->
        <div class="modal" id="deleteTeamModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Delete contestant</h4>
                    </div>
                    <div class="modal-body">
                        <p>Would you like to delete the contestant?</p>
                    </div>
                    <div class="modal-footer">
                        <input type="button" id="delete_with_contestants" class="btn btn-danger"
                               data-dismiss="modal" value="DELETE" style="margin: 0 0 5px 0; width: 100%">
                        <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                               style="margin: 0; width: 100%">
                    </div>
                </div>
            </div>
        </div>
        <div class="card-log" style="margin-top: 25px;">
            <c:choose>
                <c:when test="${contestants.size() eq 0 || race.teamSize gt 1}">
                    <div style="max-width: 850px; margin: 0 auto;">
                        <div class="alert alert-warning">
                            <c:choose>
                                <c:when test="${race.teamSize gt 1}">
                                    This section is disabled for this race.
                                </c:when>
                                <c:otherwise>
                                    List of contestants is empty!
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                            <div style="width: 100%; margin: 0 auto;">
                                <div id="importResult"></div>
                                <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                    <div style="margin-bottom: 20px;">
                                        <form:form
                                                action="${pageContext.request.contextPath}/race/${race.id}/exportContestants"
                                                method="post">
                                            <input type="button" class="btn btn-primary" data-toggle="modal"
                                                   data-target="#importTeams" style="margin-top: 5px;"
                                                   value="Import contestants">
                                            <input type="submit" class="btn btn-success" value="Export contestants" style="margin-top: 5px;">
                                        </form:form>
                                    </div>
                                </c:if>


                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Firstname</th>
                                            <th>Lastname</th>
                                            <th>Email</th>
                                            <th>Phone</th>
                                            <th>Category</th>
                                            <th>Race&nbsp;category</th>
                                            <th>Paid</th>
                                            <th>Save</th>
                                            <th>Delete</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${contestants}" var="c">
                                        <form:form id="C${c.id}">
                                            <tr id="T${c.team.id}">
                                                <input type="text" name="conId" value="${c.id}" hidden="true">
                                                <td style="vertical-align: middle;">${c.team.id}</td>
                                                <td style="vertical-align: middle;"><input class="form-control" style="width: auto;"
                                                                                           type="text" name="contestant.firstname"
                                                                                           value="${c.firstname}" maxlength="32"></td>
                                                <td style="vertical-align: middle;"><input class="form-control" style="width: auto;"
                                                                                           type="text" name="contestant.lastname"
                                                                                           value="${c.lastname}" maxlength="32"></td>
                                                <td style="vertical-align: middle;"><input class="form-control" style="width: auto;"
                                                                                           type="text" name="contestant.email"
                                                                                           value="${c.email}" maxlength="32"></td>
                                                <td style="vertical-align: middle;"><input class="form-control" style="width: auto;"
                                                                                           type="text" name="contestant.phone"
                                                                                           value="${c.phone}"></td>
                                                <td style="vertical-align: middle;">
                                                    <c:if test="${c.category ne null}">
                                                        <select class="form-control" id="teamCategory${c.category.id}" name="conCategory" style="width: auto;">
                                                            <c:forEach items="${con_categories}" var="category">
                                                                <c:choose>
                                                                    <c:when test="${c.category.id eq category.id}">
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
                                                <td>
                                                    <c:if test="${c.team.category ne null}">
                                                        <select class="form-control" id="teamCategory${c.team.id}" name="teamCategory" style="width: auto;">
                                                            <c:forEach items="${team_categories}" var="category">
                                                                <c:choose>
                                                                    <c:when test="${c.team.category.id eq category.id}">
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
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${c.paid eq true}">
                                                            <input id="P${c.id}" class="btn btn-success btn-sm"
                                                                   type="button"
                                                                   style="color: white; width: 100%"
                                                                   onclick="changePaidValue(${race.id},${c.id})"
                                                                   value="YES">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input id="P${c.id}" class="btn btn-danger btn-sm"
                                                                   type="button"
                                                                   style="margin-bottom: 5px; color: white; width: 100%"
                                                                   onclick="changePaidValue(${race.id},${c.id})"
                                                                   value="NO">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td><input id="S${c.id}" class="btn btn-primary btn-sm"
                                                           type="button"
                                                           style="margin-bottom: 5px; width: 100%; color: white;"
                                                           onclick="updateContestant(${race.id},${c.id})"
                                                           value="Save">
                                                </td>
                                                <td><input type="button" value="Delete"
                                                           class="btn btn-danger btn-sm"
                                                           data-toggle="modal" data-target="#deleteTeamModal"
                                                           onclick="tagDeleteButtons(${race.id}, ${c.team.id})"></td>
                                            </tr>
                                            </form:form>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="max-width: 1000px; margin: 0 auto;">
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th>Firstname</th>
                                            <th>Lastname</th>
                                            <th>Category</th>
                                            <th>Race&nbsp;category</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${contestants}" var="c">
                                                    <tr id="T${c.team.id}">
                                                        <td style="vertical-align: middle;">${c.firstname}</td>
                                                        <td style="vertical-align: middle;">${c.lastname}</td>
                                                        <td style="vertical-align: middle;">${c.category.name}</td>
                                                        <td>${c.team.category.name}</td>
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