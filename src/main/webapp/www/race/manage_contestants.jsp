<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<script src="${pageContext.request.contextPath}/js/contestant_list.js" language="Javascript"
        type="text/javascript"></script>

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

<t:template>
    <jsp:body>

        <t:race_menu/>
        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 1300px; margin: 0 auto;">


                <c:choose>
                    <c:when test="${race.teamSize eq 1}">
                        <div style="max-width: 850px; margin: 0 auto;">
                            <div class="alert alert-warning">
                                This section is disabled for this race.
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>


                        <c:choose>
                            <c:when test="${race_cooperator eq true || race.user.id eq user.id || race.teamSize eq 1}">
                                <c:choose>
                                    <c:when test="${not empty contestants}">
                                        <div class="table-responsive">
                                            <table class="table table-striped">
                                                <thead>
                                                <tr>
                                                    <th>Team</th>
                                                    <th>Firstname</th>
                                                    <th>Lastname</th>
                                                    <th>Email</th>
                                                    <th>Phone</th>
                                                    <th>Category</th>
                                                    <th>Paid</th>
                                                    <th>Save</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${contestants}" var="c">
                                                    <form:form id="C${c.id}">
                                                        <tr id="C${c.id}">
                                                            <input type="text" name="conId" value="${c.id}"
                                                                   hidden="true">
                                                            <td style="vertical-align: middle;">${c.team.id}</td>
                                                            <td style="vertical-align: middle;">
                                                                <input class="form-control" type="text"
                                                                       value="${c.firstname}" style="width: auto"
                                                                       name="contestant.firstname" maxlength="32">
                                                            </td>
                                                            <td style="vertical-align: middle;">
                                                                <input class="form-control" type="text"
                                                                       value="${c.lastname}" style="width: auto"
                                                                       name="contestant.lastname" maxlength="32">
                                                            </td>
                                                            <td style="vertical-align: middle;">
                                                                <input class="form-control" type="text"
                                                                       value="${c.email}" style="width: auto"
                                                                       name="contestant.email" maxlength="32">
                                                            </td>
                                                            <td style="vertical-align: middle;">
                                                                <input class="form-control" type="text"
                                                                       value="${c.phone}" style="width: auto"
                                                                       name="contestant.phone">
                                                            </td>
                                                            <td style="vertical-align: middle;">
                                                                <c:if test="${race.contestantCategory ne null}">
                                                                    <select name="conCategory" class="form-control" style="width: auto">
                                                                        <c:forEach items="${categories}" var="category">
                                                                            <c:choose>
                                                                                <c:when test="${category.id eq c.category.id}">
                                                                                    <option selected
                                                                                            value="${category.id}">${category.name}</option>
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
                                                                        <input id="P${c.id}"
                                                                               class="btn btn-success btn-sm"
                                                                               type="button"
                                                                               style="margin-bottom: 5px; width: 100%; color: white;"
                                                                               onclick="changePaidValue(${race.id},${c.id})"
                                                                               value="YES">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <input id="P${c.id}"
                                                                               class="btn btn-danger btn-sm"
                                                                               type="button"
                                                                               style="margin-bottom: 5px; width: 100%; color: white;"
                                                                               onclick="changePaidValue(${race.id},${c.id})"
                                                                               value="NO">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <input id="S${c.id}" class="btn btn-primary btn-sm"
                                                                       type="button"
                                                                       style="margin-bottom: 5px; width: 100%; color: white;"
                                                                       onclick="updateContestant(${race.id},${c.id})"
                                                                       value="Save">
                                                            </td>
                                                        </tr>
                                                    </form:form>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <%--<form:form id="C${c.id}">--%>
                                        <%--<div class="row">--%>

                                        <%--<input type="text" name="conId" value="${c.id}" hidden="true">--%>

                                        <%--<div class="col-sm-2"><input type="text" value="${c.firstname}"--%>
                                        <%--class="form-control" maxlength="32"--%>
                                        <%--name="contestant.firstname"--%>
                                        <%--style="margin-bottom: 5px;"></div>--%>
                                        <%--<div class="col-sm-2"><input type="text" value="${c.lastname}" maxlength="32"--%>
                                        <%--class="form-control" name="contestant.lastname"--%>
                                        <%--style="margin-bottom: 5px;"></div>--%>
                                        <%--<div class="col-sm-2"><input type="text" value="${c.email}" maxlength="32"--%>
                                        <%--class="form-control" name="contestant.email"--%>
                                        <%--style="margin-bottom: 5px;"></div>--%>
                                        <%--<div class="col-sm-2"><input type="text" value="${c.phone}" maxlength="16"--%>
                                        <%--class="form-control" name="contestant.phone"--%>
                                        <%--style="margin-bottom: 5px;"></div>--%>
                                        <%--<div class="col-sm-2">--%>
                                        <%--<c:if test="${race.contestantCategory ne null}">--%>
                                        <%--<select name="conCategory" class="form-control">--%>
                                        <%--<c:forEach items="${categories}" var="category">--%>
                                        <%--<c:choose>--%>
                                        <%--<c:when test="${category.id eq c.category.id}">--%>
                                        <%--<option selected--%>
                                        <%--value="${category.id}">${category.name}</option>--%>
                                        <%--</c:when>--%>
                                        <%--<c:otherwise>--%>
                                        <%--<option value="${category.id}">${category.name}</option>--%>
                                        <%--</c:otherwise>--%>
                                        <%--</c:choose>--%>

                                        <%--</c:forEach>--%>
                                        <%--</select>--%>
                                        <%--</c:if>--%>
                                        <%--</div>--%>
                                        <%--<div class="col-sm-1">--%>
                                        <%--<c:choose>--%>
                                        <%--<c:when test="${c.paid eq true}">--%>
                                        <%--<input id="P${c.id}" class="btn btn-success btn-sm"--%>
                                        <%--type="button"--%>
                                        <%--style="margin-bottom: 5px; width: 100%; color: white;"--%>
                                        <%--onclick="changePaidValue(${race.id},${c.id})"--%>
                                        <%--value="YES">--%>
                                        <%--</c:when>--%>
                                        <%--<c:otherwise>--%>
                                        <%--<input id="P${c.id}" class="btn btn-danger btn-sm" type="button"--%>
                                        <%--style="margin-bottom: 5px; width: 100%; color: white;"--%>
                                        <%--onclick="changePaidValue(${race.id},${c.id})"--%>
                                        <%--value="NO">--%>
                                        <%--</c:otherwise>--%>
                                        <%--</c:choose>--%>
                                        <%--</div>--%>

                                        <%--<div class="col-sm-1">--%>
                                        <%--<input id="S${c.id}" class="btn btn-primary btn-sm" type="button"--%>
                                        <%--style="margin-bottom: 5px; width: 100%; color: white;"--%>
                                        <%--onclick="updateContestant(${race.id},${c.id})" value="Save">--%>
                                        <%--</div>--%>
                                        <%--</div>--%>
                                        <%--<c:if test="${not i.last}">--%>
                                        <%--<hr>--%>
                                        <%--</c:if>--%>
                                        <%--</form:form>--%>

                                    </c:when>
                                    <c:otherwise>
                                        <div style="max-width: 850px; margin: 0 auto;">
                                            <div class="alert alert-warning">
                                                List of contestants is empty!
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <div style="max-width: 850px; margin: 0 auto;">
                                    <div class="alert alert-danger">
                                        You are not allowed to see this section.
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </jsp:body>
</t:template>

