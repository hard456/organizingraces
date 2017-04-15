<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

<script src="/js/race_cooperation.js" language="Javascript" type="text/javascript"></script>
<script src="/js/race_settings.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin: 30px 0 30px 0;">
            <div style="margin: 0 auto; max-width: 650px;">

                <div class="well">
                    <div style="text-align: center;">
                        ABOUT THE RACE
                    </div>
                </div>

                <div class="row" style="margin-bottom: 5px;">
                    <div class="col-sm-12">
                        Race name:

                        <input type="text" class="form-control" style="width: 100%"
                               value="${race.name}" disabled>
                    </div>

                </div>
                <div class="row">
                    <div class="col-sm-12">
                        Size of team:
                        <input type="text" class="form-control" style="width: 100%"
                               value="${race.teamSize}" disabled>
                    </div>
                </div>

                <c:if test="${race_cooperator eq true}">
                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            RACE SETTINGS
                        </div>
                    </div>

                    <form:form onsubmit="return false;">
                        <div class="row">
                            <div class="col-sm-9" style="margin-top: 7px;">
                                Registration
                            </div>
                            <div class="col-sm-3">
                                <c:choose>
                                    <c:when test="${race.registration eq true}">
                                        <div>
                                            <input type="button" class="btn btn-success"
                                                   onclick="changeRegistration(${race.id});"
                                                   value="Enabled" style="width: 100%">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="text-align: right;">
                                            <input type="button" class="btn btn-danger"
                                                   onclick="changeRegistration(${race.id});"
                                                   value="Disabled" style="width: 100%">
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-9" style="margin-top: 7px;">
                                Results
                            </div>
                            <div class="col-sm-3">
                                <c:choose>
                                    <c:when test="${race.evaluation eq true}">
                                        <div>
                                            <input type="button" class="btn btn-success"
                                                   value="Evaluated" style="width: 100%">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div>
                                            <input type="button" class="btn btn-danger"
                                                   value="Not evaluated" style="width: 100%">
                                        </div>
                                    </c:otherwise>
                                </c:choose>


                            </div>
                        </div>
                    </form:form>
                </c:if>

                <c:if test="${race.user.id eq user.id}">

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            RACE SETTINGS
                        </div>
                    </div>

                    <form:form onsubmit="return false;">
                        <div class="row">
                            <div class="col-sm-9" style="margin-top: 7px;">
                                Registration
                            </div>
                            <div class="col-sm-3">
                                <c:choose>
                                    <c:when test="${race.registration eq true}">
                                        <div>
                                            <input id="registration_b" type="button" class="btn btn-success"
                                                   onclick="changeRegistration(${race.id});"
                                                   value="Enabled" style="width: 100%">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="text-align: right;">
                                            <input id="registration_b" type="button" class="btn btn-danger"
                                                   onclick="changeRegistration(${race.id});"
                                                   value="Disabled" style="width: 100%">
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-sm-9" style="margin-top: 7px;">
                                Results
                            </div>
                            <div class="col-sm-3">
                                <c:choose>
                                    <c:when test="${race.evaluation eq true}">
                                        <div>
                                            <input id="evaluation_b" type="button" class="btn btn-success"
                                                   onclick="changeEvalution(${race.id});"
                                                   value="Evaluated" style="width: 100%">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div>
                                            <input id="evaluation_b" type="button" class="btn btn-danger"
                                                   onclick="changeEvalution(${race.id});"
                                                   value="Not evaluated" style="width: 100%">
                                        </div>
                                    </c:otherwise>
                                </c:choose>


                            </div>
                        </div>
                    </form:form>

                    <div id="setting_result"></div>

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            ADD RACE COOPERATOR
                        </div>
                    </div>

                    <form:form id="addCooperatorForm" onsubmit="return false;">
                        <div class="row">
                            <div class="col-sm-6">Cooperator login:</div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <input class="form-control"
                                       type="text"
                                       name="login" maxlength="32">
                            </div>
                            <div class="col-sm-3">
                                <div class="hidden-xs" style="text-align: right;">
                                    <input type="button" class="btn btn-primary" onclick="addCooperator(${race.id});"
                                           value="Add cooperator">
                                </div>
                                <div class="visible-xs">
                                    <input type="button" class="btn btn-primary" onclick="addCooperator(${race.id});"
                                           value="Add cooperator" style="margin-top: 5px; width: 100%">
                                </div>
                            </div>
                        </div>
                    </form:form>

                    <div id="add_cooperator_result"></div>

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            LIST OF COOPERATORS
                        </div>
                    </div>

                    <form:form id="deleteCooperatorForm" method="POST">
                        <c:forEach items="${cooperators}" varStatus="i">
                            <div id="C${cooperators.get(i.index).user.id}">
                                <div class="row">
                                    <div class="col-sm-9" name="login" style="margin-top: 7px;">
                                            ${cooperators.get(i.index).user.login}
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="hidden-xs" style="text-align: right;">
                                            <input type="button" class="btn btn-danger"
                                                   onclick="deleteCooperator(${race.id},'${cooperators.get(i.index).user.login}');"
                                                   value="Delete cooperator">
                                        </div>
                                        <div class="visible-xs">
                                            <input type="button" class="btn btn-danger"
                                                   onclick="deleteCooperator(${race.id},'${cooperators.get(i.index).user.login}');"
                                                   value="Delete cooperator" style="margin-top: 5px; width: 100%">
                                        </div>
                                    </div>

                                </div>
                                <c:if test="${not i.last}">
                                    <hr>
                                </c:if>
                            </div>
                        </c:forEach>
                    </form:form>

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            DELETE THE RACE
                        </div>
                    </div>


                    <form:form action="${pageContext.request.contextPath}/race/${race.id}/deleteRace" method="POST">
                        <div class="row">
                            <div class="col-sm-6">Password:</div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <input class="form-control"
                                       type="password"
                                       name="password">
                            </div>
                            <div class="col-sm-3">
                                <div class="hidden-xs" style="text-align: right;">
                                    <button class="btn btn-danger" type="submit" name="submit"><span
                                            style="color: white;">Delete race</span></button>
                                </div>
                                <div class="visible-xs">
                                    <button class="btn btn-danger" type="submit" name="submit"
                                            style="margin-top: 5px; width: 100%">
                                        <span style="color: white;">Delete race</span></button>
                                </div>
                            </div>
                        </div>
                        <br><br><br><br>
                    </form:form>

                </c:if>
            </div>
        </div>
        </div>
    </jsp:body>
</t:template>