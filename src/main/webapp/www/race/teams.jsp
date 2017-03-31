<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<script src="/js/delete_team.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

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
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Close" style="margin-bottom: 5px;">
                        </div>
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
                        <input type="button" id="delete_team" class="btn btn-danger" data-dismiss="modal" value="DELETE TEAM" style="margin: 0 0 5px 0; width: 100%">
                        <input type="button" id="delete_with_contestants" class="btn btn-danger" data-dismiss="modal" value="DELETE WITH CONTESTANTS" style="margin: 0 0 5px 0; width: 100%">
                        <input type="button" class="btn btn-default" data-dismiss="modal" value="Close" style="margin: 0; width: 100%">
                    </div>
                </div>
            </div>
        </div>
        </div>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 650px; margin: 0 auto;">

                <div class="hidden-xs">
                        <input type="button" value="Data import" class="btn btn-primary">
                    <hr>
                </div>

                <div class="visible-xs">
                    <input type="button" value="Import" class="btn btn-primary" style="width: 100%">
                    <hr>
                </div>

                <c:forEach items="${teams}" var="team">
                    <div class="well" id="T${team.id}">
                    <div class="row" style="margin-bottom: 20px;">
                        <div class="col-sm-4" style="margin-bottom: 10px;">
                            Team name:
                            <input class="form-control" type="text" value="${team.name}" disabled>
                        </div>
                        <div class="col-sm-4" style="margin-bottom: 10px;">
                            Team category:
                            <input class="form-control" type="text" value="${team.category.name}" disabled>
                        </div>
                        <div class="col-sm-4 hidden-xs" style="text-align: right; margin-top: 20px;">
                            <input type="button" value="Delete" class="btn btn-danger"
                                   data-toggle="modal" data-target="#deleteTeamModal" onclick="tagDeleteButtons(${race.id}, ${team.id})">
                        </div>
                        <div class="col-sm-4 visible-xs" style="text-align: right;">
                            <input type="button" value="Delete" class="btn btn-danger" style="width: 100%"
                                   data-toggle="modal" data-target="#deleteTeamModal" onclick="tagDeleteButtons(${race.id}, ${team.id})">
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-sm-4">
                            Members:
                        </div>
                    </div>
                        <c:forEach items="${contestants}" var="c">
                            <c:if test="${c.team.id eq team.id}">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <input class="form-control" type="text" value="${c.firstname} ${c.lastname}" disabled>
                                    </div>
                                    <div class="col-sm-4">
                                        <input class="form-control" type="text" value="${c.email}" disabled>
                                    </div>
                                    <div class="col-sm-4">
                                        <input class="form-control" type="text" value="${c.category.name}" disabled>
                                    </div>
                                </div>
                                <hr style="margin: 10px 0 10px 0;">
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>

            </div>
        </div>
    </jsp:body>
</t:template>