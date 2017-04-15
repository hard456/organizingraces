<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<%-- CSRF --%>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<%-- CURRENT TIME --%>
<% pageContext.setAttribute("now", new org.joda.time.DateTime()); %>

<%-- CURRENT TIME WITH PATTERN --%>
<joda:format var="now_format" value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/>

<script src="/js/results_manage.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.css">
        <link rel="stylesheet" type="text/css"
              href="https://cdn.datatables.net/buttons/1.2.4/css/buttons.dataTables.min.css">
        <script type="text/javascript" charset="utf8"
                src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.js"></script>
        <script type="text/javascript" charset="utf8"
                src="https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>
        <script type="text/javascript" charset="utf8"
                src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
        <script type="text/javascript" charset="utf8"
                src="https://cdn.datatables.net/buttons/1.2.4/js/buttons.html5.min.js"></script>

        <%-- MODAL - SET DEADLINE TIME --%>

        <div class="modal" id="deadlineTimeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set deadline time</h4>
                        </div>
                    </div>
                    <div class="modal-body">
                        Would you like to set deadline time?
                        <c:if test="${not empty team_categories}">
                            <select class="form-control" id="teamCategory" style="margin-top: 5px;">
                                <c:forEach var="c" items="${team_categories}">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-danger" value="FOR ALL" onclick="setDeadlineForAll(${race.id})">
                        <c:if test="${not empty team_categories}">
                            <input type="button" class="btn btn-danger" value="TO CATEGORY" onclick="setDeadlineToCategory(${race.id})">
                        </c:if>
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                    </div>
                </div>
            </div>
        </div>

        <%-- MODAL - SET START TIME FOR MORE TEAMS --%>

        <div class="modal" id="startTimeTeamsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set start time</h4>
                        </div>
                    </div>
                    <div class="modal-body">
                        Would you like to set a time for?
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-danger" value="FOR ALL"
                               onclick="setStartTimeForAll(${race.id})" data-dismiss="modal">
                        <input type="button" class="btn btn-danger" value="Next 10"
                               onclick="setStartTimeNextTen(${race.id})" data-dismiss="modal">
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                    </div>
                </div>
            </div>
        </div>

        <%-- MODAL - SET START TIME FOR ONE TEAM --%>

        <div class="modal" id="setStartTimeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set start time</h4>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-5">
                                <input type="text" class="form-control" id="startTimeModalInput"
                                       placeholder="${now_format}"/>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="button" id="setStartTimeButton" value="Set time" class="btn btn-success" data-dismiss="modal"/>
                        <input type="button" class="btn btn-danger" value="Set global time"
                               id="setGlobalStartTimeButton" data-dismiss="modal">
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                    </div>
                </div>
            </div>
        </div>

        <%-- FINISH TIME MODAL --%>

        <div class="modal" id="finishTimeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog line_white" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set finish time</h4>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-5">
                                <input type="text" id="finishTimeModalInput" class="form-control"
                                       placeholder="${now_format}"/>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="button" value="Set" class="btn btn-success" id="finishTimeButton" data-dismiss="modal"/>
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                    </div>
                </div>
            </div>
        </div>

        <%-- POINTS MODAL --%>

        <div class="modal" id="pointsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
                   aria-hidden="true">
        <div class="modal-dialog line_white" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <div style="margin-bottom: 15px;">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Set points</h4>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-12" style="margin-bottom: 10px;">
                            Points:
                            <input type="text" id="pointsModalInput" class="form-control"/>
                        </div>
                        <div class="col-sm-12" style="margin-bottom: 10px;">
                            Bonus points:
                            <input type="text" id="bonusModalInput" class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="button" class="btn btn-primary" value="Save" id="setPointsButton" data-dismiss="modal">
                    <input type="button" class="btn btn-secondary" data-dismiss="modal" value="Close">
                </div>
            </div>
        </div>
    </div>

        <%-- RESULT DANGER MODAL --%>

        <div class="modal" id="resultDangerModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete result</h4>
                        </div>
                        <div id="resultDanger" style="margin-bottom: 10px;" class="alert alert-danger"></div>
                        <div style="text-align: right">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                                   style="margin-bottom: 5px;">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- RESULT SUCCESS MODAL --%>

        <div class="modal" id="resultSuccessModal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div style="margin-bottom: 15px;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Delete result</h4>
                        </div>
                        <div id="resultSuccess" style="margin-bottom: 10px;" class="alert alert-success"></div>
                        <div style="text-align: right">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Close"
                                   style="margin-bottom: 5px;">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 1000px; margin: 0 auto;">
                <c:choose>
                    <c:when test="${race_cooperator eq true || race.user.id eq user.id}">

                        <div class="row">
                            <div class="col-sm-6">
                                <a href="${pageContext.request.contextPath}/race/${race.id}/results"><input
                                        type="button" style="width: 100%; margin-bottom: 5px;" class="btn btn-default"
                                        value="Show results"></a>
                            </div>
                            <div class="col-sm-6">
                                <a href="${pageContext.request.contextPath}/race/${race.id}/results/manage"><input
                                        type="button" style="width: 100%; margin-bottom: 5px;" class="btn btn-primary"
                                        value="Set results"></a>
                            </div>
                        </div>
                        <hr>
                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-sm-3">
                                <input type="text" class="form-control" placeholder="In minutes"/>
                            </div>
                            <div class="col-sm-2">
                                <input type="button" value="Deadline time" class="btn btn-success" data-toggle="modal"
                                       data-target="#deadlineTimeModal"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <input type="text" id="startGlobalTime" class="form-control" value="${now_format}"
                                       placeholder="${now_format}"/>
                            </div>
                            <div class="col-sm-2">
                                <input type="button" value="Set start time" class="btn btn-success" data-toggle="modal"
                                       data-target="#startTimeTeamsModal"/>
                            </div>
                        </div>

                        <hr>
                        <table id="myTable" class="display" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Team</th>
                                <th>Category</th>
                                <th>Points</th>
                                <th>Bonus</th>
                                <th>Start time</th>
                                <th>Finish time</th>
                                <th>Deadline time</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${teams}" var="team" varStatus="i">
                                <tr style="text-align: center;">
                                    <td>${team.name}</td>
                                    <td>${team.category.name}</td>
                                    <td id="points${team.id}" onclick="setPointsToModal(${race.id},${team.id})"
                                        data-toggle="modal" data-target="#pointsModal">
                                            ${team.points}
                                    </td>
                                    <td id="bonus${team.id}" onclick="setPointsToModal(${race.id},${team.id})"
                                        data-toggle="modal" data-target="#pointsModal">
                                            ${team.bonus}
                                    </td>
                                    <td id="startTime${team.id}" onclick="setStartTimeToModal(${race.id}, ${team.id})"
                                        data-toggle="modal" data-target="#setStartTimeModal">
                                        <joda:format value="${team.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td id="finishTime${team.id}" onclick="setModalForFinishTime(${race.id},${team.id})"
                                        data-toggle="modal" data-target="#finishTimeModal">
                                        <joda:format value="${team.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td>${team.deadlineTime}</td>
                                    <td style="width: auto;">
                                        <input type="button" value="Finish" class="btn btn-primary btn-sm"
                                               onclick="teamFinished(${race.id}, ${team.id})">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                        <script>
                            jQuery.extend(jQuery.fn.dataTableExt.oSort, {
                                "non-empty-string-asc": function (str1, str2) {
                                    if (str1 == "")
                                        return 1;
                                    if (str2 == "")
                                        return -1;
                                    return ((str1 < str2) ? -1 : ((str1 > str2) ? 1 : 0));
                                },

                                "non-empty-string-desc": function (str1, str2) {
                                    if (str1 == "")
                                        return 1;
                                    if (str2 == "")
                                        return -1;
                                    return ((str1 < str2) ? 1 : ((str1 > str2) ? -1 : 0));
                                }
                            });
                            $(document).ready(function () {
                                $('#myTable').DataTable(
                                        {
                                            "pagingType": "first_last_numbers",
                                            "sScrollX": "100%",
                                            dom: 'Bfrtip',
                                            columnDefs: [
                                                {type: 'non-empty-string', targets: 4}, // define 'name' column as non-empty-string type
                                                {type: 'non-empty-string', targets: 5}
                                            ],
                                            lengthMenu: [
                                                [10, 25, 50, -1],
                                                ['10 rows', '25 rows', '50 rows', 'Show all']
                                            ],
                                            buttons: [
                                                'pageLength',
                                            ]
                                        }
                                );
                            });
                        </script>

                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger">
                            You are not allowed to see this section.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:template>
