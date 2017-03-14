<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<script src="/js/add_contestant_inputs.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 800px; margin: 0 auto;">
                <c:if test="${race_cooperator eq true || race.user.id eq user.id}">

                    <div class="well well-lg" style="background: lightgoldenrodyellow">
                        <div style="text-align: center;">ADMIN TEAM REGISTRATION</div>
                    </div>

                    <form:form action="/race/${race.id}/addTeamByAdmin" modelAttribute="contestantList" method="POST">

                        <div class="row">
                            <div class="col-sm-6">
                                Team name:<input class="form-control" name="name"/>
                            </div>
                            <c:if test="${not empty team_categories}">
                                <div class="col-sm-6">
                                    Category:
                                    <select class="form-control" name="category">
                                        <c:forEach items="${team_categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </c:if>
                        </div>

                        <br>

                        <c:forEach items="${contestantList.contestants}" varStatus="i">

                            <div class="row">
                                <div class="col-sm-4">
                                    Teammate ${i.index+1}:
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-sm-4">
                                    Firstname:<input class="form-control" name="contestants[${i.index}].firstname"/>
                                </div>
                                <div class="col-sm-4">
                                    Lastname:<input class="form-control"
                                                    type="text"
                                                    name="contestants[${i.index}].lastname">

                                </div>
                                <div class="col-sm-4">
                                    Phone:<input class="form-control"
                                                 type="text"
                                                 path="contestants[${i.index}].firstname">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4">
                                    Email:<input class="form-control"
                                                 type="text"
                                                 name="contestants[${i.index}].email">

                                </div>
                                <c:if test="${not empty con_categories}">
                                    <div class="col-sm-4">
                                        Category:
                                        <select class="form-control" name="category">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>

                                    </div>
                                </c:if>
                                <br>
                                <div class="col-sm-4">
                                    <div class="checkbox">
                                        <label><input type="checkbox" name="contestants[${i.index}].paid" value="true">Paid</label>
                                    </div>
                                </div>
                            </div>
                            <hr>
                        </c:forEach>

                        <div id="dynamicInput">

                        </div>
                        <div class="row">
                            <div class="col-sm-12" style="text-align: right">
                                <input type="button" class="btn btn-danger" value="-"
                                       onClick="removeContestant(${race.teamSize});"
                                       id="removeContestantButton">

                                <input type="button" class="btn btn-success" value="+"
                                       onClick="addContestant('dynamicInput',${race.teamSize});"
                                       id="addContestantButton">
                                <button class="btn btn-primary" type="submit" name="submit"><span
                                        style="color: white;">Team registration</span></button>
                            </div>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    </form:form>
                    <br><br><br><br>

                    <div class="well well-lg" style="background: lightgoldenrodyellow">
                        <div style="text-align: center;">ADMIN SOLO REGISTRATION</div>
                    </div>

                    <form:form action="/race/${race.id}/addTeamByAdmin" method="POST">

                        <div class="row">
                            <div class="col-sm-4">
                                Firstname:<input class="form-control" name="firstname"/>
                            </div>
                            <div class="col-sm-4">
                                Lastname:<input class="form-control"
                                                type="text"
                                                name="lastname">

                            </div>
                            <div class="col-sm-4">
                                Phone:<input class="form-control"
                                             type="text"
                                             path="firstname">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Email:<input class="form-control"
                                             type="text"
                                             name="email">

                            </div>
                            <c:if test="${not empty team_categories}">
                                <div class="col-sm-4">
                                    Category:
                                    <select class="form-control" name="category">
                                        <c:forEach items="${team_categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </c:if>
                            <br>
                            <div class="col-sm-4">
                                <div class="checkbox">
                                    <label><input type="checkbox" name="paid" value="true">Paid</label>
                                </div>
                            </div>
                        </div>
                        <hr>

                        <div class="row">
                            <div class="col-sm-12" style="text-align: right">
                                <button class="btn btn-primary" type="submit" name="submit"><span
                                        style="color: white;">Solo registration</span></button>
                            </div>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    </form:form>
                    <br><br><br><br>
                </c:if>


                <div class="well well-lg">
                    <div style="text-align: center;">SOLO REGISTRATION</div>
                </div>

                <form name="addSoloContestant" action="<c:url value="/race/${race.id}/addSoloContestant" />"
                      method="POST">

                    <c:if test="${not empty con_categories}">
                        Category:
                        <select class="form-control" name="category">
                            <c:forEach items="${con_categories}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>

                    </c:if>
                    <br>
                    <div class="alert alert-warning">Other informations about you will be take from your account.</div>

                    <hr>

                    <div class="row">
                        <div class="col-sm-12" style="text-align: right">
                            <button class="btn btn-primary" type="submit" name="submit"><span
                                    style="color: white;">Solo registration</span></button>
                        </div>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                </form>

                <br><br><br><br>

                <c:if test="${race.teamSize > 1}">

                    <div class="well well-lg">
                        <div style="text-align: center;">TEAM REGISTRATION</div>
                    </div>

                    <form:form action="/race/${race.id}/addTeamByAdmin" method="POST">

                        <div class="row">
                            <div class="col-sm-6">
                                Team name:<input class="form-control" name="name"/>
                            </div>
                            <c:if test="${not empty team_categories}">
                                <div class="col-sm-6">
                                    Category:
                                    <select class="form-control" name="category">
                                        <c:forEach items="${team_categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </c:if>
                        </div>

                        <br>
                        You:
                        <br><br>


                        <c:if test="${not empty con_categories}">
                            <div class="row">
                                <div class="col-sm-12">
                                    Category:
                                    <select class="form-control" name="category">
                                        <c:forEach items="${con_categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>

                                </div>
                            </div>
                        </c:if>
                        <br>

                        <div class="alert alert-warning">Other informations about you will be take from your account.
                        </div>


                        <c:forEach varStatus="i" begin="0" end="${race.teamSize-2}">

                            <div class="row">
                                <div class="col-sm-4">
                                    Teammate ${i.index+1}:
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-sm-4">
                                    Firstname:<input class="form-control" name="contestants[${i.index}].firstname"/>
                                </div>
                                <div class="col-sm-4">
                                    Lastname:<input class="form-control"
                                                    type="text"
                                                    name="contestants[${i.index}].lastname">

                                </div>
                                <div class="col-sm-4">
                                    Phone:<input class="form-control"
                                                 type="text"
                                                 path="contestants[${i.index}].firstname">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4">
                                    Email:<input class="form-control"
                                                 type="text"
                                                 name="contestants[${i.index}].email">

                                </div>

                                <c:if test="${not empty con_categories}">
                                    <div class="col-sm-6">
                                        Category:
                                        <select class="form-control" name="category">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>

                                    </div>
                                </c:if>
                            </div>
                            <hr>
                        </c:forEach>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="row">
                            <div class="col-sm-12" style="text-align: right">
                                <button class="btn btn-primary" type="submit" name="submit"><span
                                        style="color: white;">Team registration</span></button>
                            </div>
                        </div>
                    </form:form>

                </c:if>

            </div>
        </div>

        ${result}

    </jsp:body>
</t:template>
