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
                <div style="text-align: center;">
                    <c:if test="${race.teamSize gt 1}">ADMIN TEAM REGISTRATION</c:if>
                    <c:if test="${race.teamSize eq 1}">ADMIN REGISTRATION</c:if>
                </div>
            </div>

            <form:form action="/race/${race.id}/addTeamByAdmin" modelAttribute="contestantList" method="POST">

            <div class="row">
                <c:if test="${race.teamSize gt 1}">
                    <div class="col-sm-6">
                        Team name:<input class="form-control" name="name"/>
                    </div>
                </c:if>
                <c:if test="${not empty team_categories}">
                    <div class="col-sm-6">
                        <c:if test="${race.teamSize gt 1}">Team category:</c:if>
                        <c:if test="${race.teamSize eq 1}">Race category:</c:if>
                        <select class="form-control" name="category">
                            <c:forEach items="${team_categories}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>

                    </div>
                </c:if>
            </div>

            <br>


            <c:forEach varStatus="i" begin="0" end="${race.teamSize+1}">

            <c:choose>
            <c:when test="${i.index gt race.teamSize-1}">
            <div id="T${i.index}" style="display: none;">
                </c:when>
                <c:otherwise>
                <div id="T${i.index}">
                    </c:otherwise>
                    </c:choose>

                    <div class="row">
                        <div class="col-sm-4">
                            Teammate ${i.index+1}:
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="col-sm-4">
                            Firstname:
                            <c:choose>
                            <c:when test="${i.index gt race.teamSize-1}">
                                <input class="form-control" id="${i.index}firstname" name=""/>
                            </c:when>
                            <c:otherwise>
                                <input class="form-control" name="contestants[${i.index}].firstname"/>
                            </c:otherwise>
                            </c:choose>

                        </div>
                        <div class="col-sm-4">
                            Lastname:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}lastname" name="">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].lastname">
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <div class="col-sm-4">
                            Phone:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}phone" name="">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].phone">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4">
                            Email:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}email" name="">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].email">
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <c:if test="${not empty con_categories}">
                            <div class="col-sm-4">
                                Contestant category:
                                <c:choose>
                                    <c:when test="${i.index gt race.teamSize-1}">
                                        <select class="form-control" id="${i.index}category" name="">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select class="form-control" name="category">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </c:if>
                        <br>
                        <div class="col-sm-4">
                            <div class="checkbox">
                                <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <label><input type="checkbox" id="${i.index}paid" name="" value="true">Paid</label>
                                </c:when>
                                <c:otherwise>
                                <label><input type="checkbox" name="contestants[${i.index}].paid" value="true">Paid</label>
                                </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <hr>
                </div>

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
                                style="color: white;">
                                    <c:if test="${race.teamSize gt 1}">Team registration</c:if>
                                    <c:if test="${race.teamSize eq 1}">Registration</c:if>
                                </span></button>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                </form:form>
                <br><br><br><br>


                    <%----%>
                    <%----%>
                    <%----%>
                    <%----%>
                <c:if test="${race.teamSize gt 1}">
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
                            <c:if test="${not empty con_categories}">
                                <div class="col-sm-4">
                                    Contestant category:
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
                </c:if>

                    <%----%>
                    <%----%>
                    <%----%>
                    <%----%>

                <c:if test="${race.teamSize gt 1}">
                    <div class="well well-lg">
                        <div style="text-align: center;">SOLO REGISTRATION</div>
                    </div>

                    <form name="addSoloContestant" action="<c:url value="/race/${race.id}/addSoloContestant" />"
                          method="POST">

                        <c:if test="${not empty con_categories}">
                            Contestant category:
                            <select class="form-control" name="category">
                                <c:forEach items="${con_categories}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                            <br>
                        </c:if>

                        <div class="alert alert-warning">Informations about you will be take from your account.</div>

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
                </c:if>

                    <%----%>
                    <%----%>
                    <%----%>
                    <%----%>

                <div class="well well-lg">
                    <div style="text-align: center;">
                        <c:if test="${race.teamSize gt 1}">TEAM REGISTRATION</c:if>
                        <c:if test="${race.teamSize eq 1}">REGISTRATION</c:if>
                    </div>
                </div>

                <form:form action="/race/${race.id}/teamRegistration" method="POST">

                    <div class="row">
                        <c:if test="${race.teamSize gt 1}">
                            <div class="col-sm-6">
                                Team name:<input class="form-control" name="teamName"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty team_categories}">
                            <div class="col-sm-6">
                                <c:if test="${race.teamSize gt 1}">Team category:</c:if>
                                <c:if test="${race.teamSize eq 1}">Race category:</c:if>
                                <select class="form-control" name="teamCategory">
                                    <c:forEach items="${team_categories}" var="c">
                                        <option value="${c.id}">${c.name}</option>
                                    </c:forEach>
                                </select>

                            </div>
                        </c:if>
                    </div>
                    <br>
                    <c:if test="${race.teamSize gt 1}">
                        You:
                        <br><br>
                    </c:if>

                    <c:if test="${not empty con_categories}">
                        <div class="row">
                            <div class="col-sm-12">
                                Contestant category:
                                <select class="form-control" name="conCategory">
                                    <c:forEach items="${con_categories}" var="c">
                                        <option value="${c.id}">${c.name}</option>
                                    </c:forEach>
                                </select>

                            </div>
                        </div>
                        <br>
                    </c:if>

                    <div class="alert alert-warning">Informations about you will be take from your account.
                    </div>

                    <c:if test="${race.teamSize gt 1}">
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
                                                 name="contestants[${i.index}].phone">
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
                                        Contestant category:
                                        <select class="form-control" name="teammateCategory[${i.index}]">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>

                                    </div>
                                </c:if>
                            </div>
                            <hr>
                        </c:forEach>
                    </c:if>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="row">
                        <div class="col-sm-12" style="text-align: right">
                            <button class="btn btn-primary" type="submit" name="submit"><span
                                    style="color: white;">
                                    <c:if test="${race.teamSize gt 1}">Team Registration</c:if>
                                    <c:if test="${race.teamSize eq 1}">Registration</c:if>
                                </span></button>
                        </div>
                    </div>
                </form:form>

            </div>
        </div>

        ${result}

    </jsp:body>
</t:template>
