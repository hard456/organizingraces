<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.css">
        <script type="text/javascript" charset="utf8"
                src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.js"></script>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 1000px; margin: 0 auto;">
                <c:choose>
                    <c:when test="${race.evaluation eq true || race_cooperator eq true || race.user.id eq user.id}">

                        <%-- Import results modal --%>

                        <div class="modal fade" id="importTeams" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog line_white" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <div style="margin-bottom: 15px;">
                                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                                            <h4 class="modal-title">Import results from EXCEL</h4>
                                        </div>
                                    </div>
                                    <div class="modal-body">
                                        <label class="btn btn-primary" for="my-file-selector">
                                            <input id="my-file-selector" type="file" style="display:none;"
                                                   onchange="$('#upload-file-info').html(this.files[0].name);"
                                                   accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                                            Import XLSX file
                                        </label>
                                        <span id="upload-file-info"></span>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="button" class="btn btn-primary" value="Import">
                                        <input type="button" class="btn btn-secondary" data-dismiss="modal"
                                               value="Close">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <c:if test="${race_cooperator eq true || race.user.id eq user.id}">
                            <div class="hidden-xs" style="margin-bottom: 30px;">
                                <c:if test="${teams.size() > 0}">
                                    <input type="button" class="btn btn-success" value="Export results">
                                </c:if>
                            </div>
                            <div class="visible-xs" style="margin-bottom: 20px;">
                                <c:if test="${teams.size() > 0}">
                                    <input type="button" class="btn btn-success" value="Export results"
                                           style="width: 100%">
                                </c:if>
                            </div>
                        </c:if>

                        <table id="myTable" class="display table table-bordered" cellspacing="0" width="100%">
                            <thead class="table table-bordered">
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
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${teams}" var="team" varStatus="i">
                                <tr style="text-align: center;">
                                    <td>${i.index+1}</td>
                                    <td>${team.name}</td>
                                    <td>${team.category.name}</td>
                                    <td>555</td>
                                    <td>${team.bonus}</td>
                                    <td>${team.penalization}</td>
                                    <td>777</td>
                                    <td>${team.startTime}</td>
                                    <td>${team.finishTime}</td>
                                    <td style="width: auto;"><input type="button" value="Action"
                                                                    class="btn btn-primary"></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                        <script>
                            $(document).ready(function () {
                                $('#myTable').DataTable(
                                        {
                                            "pagingType": "first_last_numbers",
                                            "sScrollX": "100%",

                                        }
                                );
                            });
                        </script>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">
                            The race is not evaluated.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:template>
