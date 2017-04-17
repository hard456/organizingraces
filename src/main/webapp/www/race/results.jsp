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
            <div style="max-width: 1000px; margin: 0 auto;">
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
                        </c:if>
                        <br><br>
                        <table id="myTable" class="display" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Rank</th>
                                <th>Team</th>
                                <th>Category</th>
                                <th>Points</th>
                                <th>Bonus</th>
                                <th>Penalization</th>
                                <th>Final Points</th>
                                <th>Start time</th>
                                <th>Finish time</th>
                                    <%--<th>Action</th>--%>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${teams}" var="team" varStatus="i">
                                <tr style="text-align: center;">
                                    <td>${i.index+1}</td>
                                    <td>${team.name}</td>
                                    <td>${team.category.name}</td>
                                    <td>${team.points}</td>
                                    <td>${team.bonus}</td>
                                    <td>${team.penalization}</td>
                                    <td>${team.finalPoints}</td>
                                    <td><joda:format value="${team.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><joda:format value="${team.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <%--<td style="width: auto;"><input type="button" value="Action"--%>
                                        <%--class="btn btn-primary"></td>--%>
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
                                                        [10, 25, 50, -1],
                                                        ['10 rows', '25 rows', '50 rows', 'Show all']
                                                    ],
                                                    columnDefs: [
                                                        {type: 'non-empty-string', targets: 7},
                                                        {type: 'non-empty-string', targets: 8}
                                                    ],
                                                    buttons: [
                                                        'pageLength',
                                                        'excelHtml5',
                                                        {
                                                            extend: 'csvHtml5',
                                                            charset: 'UTF-16LE',
                                                            fieldSeparator: '\t',
                                                            bom: true
                                                        }
                                                    ]
                                                }
                                        );
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
                                                        [10, 25, 50, -1],
                                                        ['10 rows', '25 rows', '50 rows', 'Show all']
                                                    ],
                                                    columnDefs: [
                                                        {type: 'non-empty-string', targets: 7},
                                                        {type: 'non-empty-string', targets: 8}
                                                    ],
                                                    buttons: [
                                                        'pageLength',
                                                    ]
                                                }
                                        );
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
