<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

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


        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 1300px; margin: 0 auto;">
                <c:choose>
                    <c:when test="${race.evaluation eq true || race_cooperator eq true || race.user.id eq user.id}">
                        <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                            <div class="row">
                                <div class="col-sm-6">
                                    <a href="${pageContext.request.contextPath}/race/${race.id}/results"><input
                                            type="button" style="width: 100%; margin-bottom: 5px;"
                                            class="btn btn-primary" value="Show results"></a>
                                </div>
                                <div class="col-sm-6">
                                    <a href="${pageContext.request.contextPath}/race/${race.id}/results/manage"><input
                                            type="button" style="width: 100%; margin-bottom: 5px;"
                                            class="btn btn-default" value="Set results"></a>
                                </div>
                            </div>
                            <hr>
                        </c:if>
                        <c:if test="${race.teamCategory ne null}">
                            <div class="row">
                                <div class="col-sm-2 col-md-offset-4" style="margin-top: 7px;">Select by category:</div>
                                <div class="col-sm-3">
                                    <select id="categorySelect" class="form-control">
                                        <option value="None"></option>
                                        <c:forEach items="${team_categories}" var="c">
                                            <option value="${c.name}">${c.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </c:if>
                        <br><br>
                        <table id="myTable" class="display" cellspacing="0" width="100%" style="font-size: 12px;">
                            <thead>
                            <tr>
                                <th>Rank</th>
                                <c:choose>
                                    <c:when test="${race.teamSize eq 1}">
                                        <th>Contestant</th>
                                    </c:when>
                                    <c:otherwise>
                                        <th>Team</th>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                    <th>Id</th>
                                </c:if>
                                <c:choose>
                                    <c:when test="${race.teamSize eq 1}">
                                        <th>Race category</th>
                                    </c:when>
                                    <c:otherwise>
                                        <th>Team category</th>
                                    </c:otherwise>
                                </c:choose>
                                <th>Points</th>
                                <th>Bonus</th>
                                <th>Penalization</th>
                                <th>Final Points</th>
                                <th>Start time</th>
                                <th>Finish time</th>
                                <th>Result time</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${teams}" var="team" varStatus="i">
                                <tr style="text-align: center;">
                                    <td>${i.index+1}</td>
                                    <td>${team.name}</td>
                                    <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                                        <td>${team.id}</td>
                                    </c:if>
                                    <td>${team.category.name}</td>
                                    <td>${team.points}</td>
                                    <td>${team.bonus}</td>
                                    <td>${team.penalization}</td>
                                    <td>${team.finalPoints}</td>
                                    <td><joda:format value="${team.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><joda:format value="${team.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>${team.resultTime}</td>
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
                        </script>

                        <c:choose>
                            <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                                <script>
                                    $(document).ready(function () {
                                        $('#myTable').DataTable(
                                                {
                                                    "pagingType": "first_last_numbers",
                                                    "sScrollX": "100%",
                                                    dom: 'Bfrtip',
                                                    lengthMenu: [
                                                        [50, 100, -1],
                                                        ['50 rows', '100 rows', 'Show all']
                                                    ],
                                                    columnDefs: [
                                                        {type: 'non-empty-string', targets: 8},
                                                        {type: 'non-empty-string', targets: 9},
                                                        {orderable: false, targets: 10}
                                                    ],
                                                    buttons: [
                                                        'pageLength',
                                                        'excelHtml5',
                                                        {
                                                            extend: 'csvHtml5',
                                                            charset: 'UTF-16LE',
                                                            fieldSeparator: ';',
                                                            bom: true
                                                        }
                                                    ]
                                                }
                                        );
                                    });
                                    $('#categorySelect').on('change', function () {
                                        if (this.value.localeCompare("None") == 0) {
                                            var table = $('#myTable').DataTable();
                                            table.columns(3).search('').draw();
                                        }
                                        else {
                                            var table = $('#myTable').DataTable();
                                            table.columns(3).search(this.value, true, false).draw();
                                        }
                                    });
                                </script>
                            </c:when>
                            <c:otherwise>
                                <script>
                                    $(document).ready(function () {
                                        $('#myTable').DataTable(
                                                {
                                                    "pagingType": "first_last_numbers",
                                                    "sScrollX": "100%",
                                                    dom: 'Bfrtip',
                                                    lengthMenu: [
                                                        [50, 100, -1],
                                                        ['50 rows', '100 rows', 'Show all']
                                                    ],
                                                    columnDefs: [
                                                        {type: 'non-empty-string', targets: 7},
                                                        {type: 'non-empty-string', targets: 8},
                                                        {orderable: false, targets: 9}
                                                    ],
                                                    buttons: [
                                                        'pageLength',
                                                    ]
                                                }
                                        );
                                    });
                                    $('#categorySelect').on('change', function () {
                                        if (this.value.localeCompare("None") == 0) {
                                            var table = $('#myTable').DataTable();
                                            table.columns(2).search('').draw();
                                        }
                                        else {
                                            var table = $('#myTable').DataTable();
                                            table.columns(2).search(this.value, true, false).draw();
                                        }
                                    });
                                </script>
                            </c:otherwise>
                        </c:choose>

                    </c:when>
                    <c:otherwise>
                        <div style="max-width: 850px; margin: 0 auto;">
                            <div class="alert alert-warning">
                                The race is not evaluated.
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:template>
