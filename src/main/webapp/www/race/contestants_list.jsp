<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<script src="/js/contestant_list.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>
        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 900px; margin: 0 auto;">

                <c:choose>
                    <c:when test="${race_cooperator eq true || race.user.id eq user.id}">
                        <c:choose>
                            <c:when test="${not empty contestants}">
                                <div class="row hidden-xs"
                                     style="background: lightcoral; padding: 5px 0 5px 0; color: white;">
                                    <div class="col-sm-2">Firstname</div>
                                    <div class="col-sm-2">Lastname</div>
                                    <div class="col-sm-2">Email</div>
                                    <div class="col-sm-2">Phone</div>
                                    <div class="col-sm-2">Category</div>
                                    <div class="col-sm-1">Paid</div>
                                    <div class="col-sm-1">Save</div>
                                </div>
                                <br>
                                <c:forEach var="c" varStatus="i" items="${contestants}">
                                    <form:form id="C${c.id}">
                                        <div class="row">

                                            <input type="text" name="conId" value="${c.id}" hidden="true">

                                            <div class="col-sm-2"><input type="text" value="${c.firstname}"
                                                                         class="form-control" maxlength="32"
                                                                         name="contestant.firstname"
                                                                         style="margin-bottom: 5px;"></div>
                                            <div class="col-sm-2"><input type="text" value="${c.lastname}" maxlength="32"
                                                                         class="form-control" name="contestant.lastname"
                                                                         style="margin-bottom: 5px;"></div>
                                            <div class="col-sm-2"><input type="text" value="${c.email}" maxlength="32"
                                                                         class="form-control" name="contestant.email"
                                                                         style="margin-bottom: 5px;"></div>
                                            <div class="col-sm-2"><input type="text" value="${c.phone}" maxlength="16"
                                                                         class="form-control" name="contestant.phone"
                                                                         style="margin-bottom: 5px;"></div>
                                            <div class="col-sm-2">
                                                <c:if test="${race.contestantCategory ne null}">
                                                    <select name="conCategory" class="form-control">
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
                                            </div>
                                            <div class="col-sm-1">
                                                <c:choose>
                                                    <c:when test="${c.paid eq true}">
                                                        <input id="P${c.id}" class="btn btn-success btn-sm"
                                                               type="button"
                                                               style="margin-bottom: 5px; width: 100%; color: white;"
                                                               onclick="changePaidValue(${race.id},${c.id})"
                                                               value="YES">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="P${c.id}" class="btn btn-danger btn-sm" type="button"
                                                               style="margin-bottom: 5px; width: 100%; color: white;"
                                                               onclick="changePaidValue(${race.id},${c.id})"
                                                               value="NO">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <div class="col-sm-1">
                                                <input id="S${c.id}" class="btn btn-primary btn-sm" type="button"
                                                       style="margin-bottom: 5px; width: 100%; color: white;"
                                                       onclick="updateContestant(${race.id},${c.id})" value="Save">
                                            </div>
                                        </div>
                                        <c:if test="${not i.last}">
                                            <hr>
                                        </c:if>
                                    </form:form>
                                </c:forEach>
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
            </div>
        </div>

    </jsp:body>
</t:template>

