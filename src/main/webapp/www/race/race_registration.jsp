<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script src="${pageContext.request.contextPath}/js/add_contestant_inputs.js" language="Javascript"
        type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/race_registration.js" language="Javascript"
        type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
        <div style="max-width: 800px; margin: 0 auto;">
            <c:if test="${race_cooperator eq true || race.user.id eq user.id}">

                <%-- ADMIN TEAM REGISTRATION --%>

            <div class="well well-lg" style="background: lightgoldenrodyellow">
                <div style="text-align: center;">
                    <c:if test="${race.teamSize gt 1}">ADMIN TEAM REGISTRATION</c:if>
                    <c:if test="${race.teamSize eq 1}">ADMIN REGISTRATION</c:if>
                </div>
            </div>

            <form:form id="adminTeamRegistrationForm" method="post">

            <div class="row">
                <c:if test="${race.teamSize gt 1}">
                    <div class="col-sm-6">
                        Team name <span style="color: darkgrey">(3 - 32 length)</span>:
                        <input class="form-control" name="teamName" maxlength="32"/>
                    </div>
                </c:if>
                <c:if test="${not empty team_categories}">
                    <div class="col-sm-6">
                        <c:if test="${race.teamSize gt 1}">Team category<span style="color: red;">*</span>:</c:if>
                        <c:if test="${race.teamSize eq 1}">Race category<span style="color: red;">*</span>:</c:if>
                        <select class="form-control" name="teamCategory">
                            <c:forEach items="${team_categories}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>

                    </div>
                </c:if>
            </div>

            <br>


            <c:forEach varStatus="i" begin="0" end="${race.teamSize+1}">

            <c:if test="${(race.teamSize eq 1 && i.index eq 0) || race.teamSize gt 1}">
            <c:choose>
            <c:when test="${i.index gt race.teamSize-1}">
            <div id="T${i.index}" style="display: none;">
                </c:when>
                <c:otherwise>
                <div id="T${i.index}">
                    </c:otherwise>
                    </c:choose>
                    <c:if test="${race.teamSize gt 1}">
                        <div class="row">
                            <div class="col-sm-4">
                                Teammate ${i.index+1}:
                            </div>
                        </div>
                        <br>
                    </c:if>
                    <div class="row">
                        <div class="col-sm-4">
                            Firstname<span style="color: red;">*</span> <span
                                style="color: darkgrey">(3 - 32 length)</span>:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" id="${i.index}firstname" name="" maxlength="32"/>
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" name="contestants[${i.index}].firstname"
                                           id="${i.index}firstname" maxlength="32"/>
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <div class="col-sm-4">
                            Lastname<span style="color: red;">*</span> <span
                                style="color: darkgrey">(3 - 32 length)</span>:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}lastname" name=""
                                           maxlength="32">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].lastname"
                                           id="${i.index}lastname" maxlength="32">
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <div class="col-sm-4">
                            Phone <span style="color: darkgrey">(123456789, +420123456789)</span>:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}phone" name="" maxlength="32">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].phone"
                                           id="${i.index}phone" maxlength="16">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4">
                            Email <span style="color: darkgrey">(6 - 32 length)</span>:
                            <c:choose>
                                <c:when test="${i.index gt race.teamSize-1}">
                                    <input class="form-control" type="text" id="${i.index}email" name="" maxlength="32">
                                </c:when>
                                <c:otherwise>
                                    <input class="form-control" type="text" name="contestants[${i.index}].email"
                                           id="${i.index}email" maxlength="32">
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <c:if test="${not empty con_categories}">
                            <div class="col-sm-4">
                                Contestant category<span style="color: red;">*</span>:
                                <c:choose>
                                    <c:when test="${i.index gt race.teamSize-1}">
                                        <select class="form-control" id="${i.index}category" name="">
                                            <c:forEach items="${con_categories}" var="c">
                                                <option value="${c.id}">${c.name}</option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select class="form-control" name="teammateCategory[${i.index}]"
                                                id="${i.index}category">
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
                                        <label><input type="checkbox" id="${i.index}paid" name=""
                                                      value="true">Paid</label>
                                    </c:when>
                                    <c:otherwise>
                                        <label><input type="checkbox" name="contestants[${i.index}].paid" value="true"
                                                      id="${i.index}paid">Paid</label>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <hr>
                </div>
                </c:if>
                </c:forEach>

                <div class="row">
                    <div class="col-sm-12" style="text-align: right">
                        <c:if test="${race.teamSize gt 1}">
                        <input type="button" class="btn btn-danger" value="-"
                               onClick="removeContestant(${race.teamSize});"
                               id="removeContestantButton">

                        <input type="button" class="btn btn-success" value="+"
                               onClick="addContestant(${race.teamSize});"
                               id="addContestantButton">
                        </c:if>
                        <input type="button" class="btn btn-primary" onclick="adminTeamRegistrationAjax(${race.id});"
                               value="Registration">
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form:form>
                <div id="admin_team_result"></div>
                <br><br><br><br>

                    <%-- ADMIN SOLO CONTESTANT REGISTRATION --%>

                <c:if test="${race.teamSize gt 1}">
                    <div class="well well-lg" style="background: lightgoldenrodyellow">
                        <div style="text-align: center;">ADMIN SOLO REGISTRATION</div>
                    </div>

                    <form:form id="adminSoloContestantForm" method="POST">

                        <div class="row">
                            <div class="col-sm-4">
                                Firstname<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                                <input class="form-control" name="contestant.firstname" id="firstname"
                                       maxlength="32"/>
                            </div>
                            <div class="col-sm-4">
                                Lastname<span style="color: red;">*</span> <span
                                    style="color: darkgrey">(3 - 32 length)</span>:
                                <input class="form-control"
                                       type="text"
                                       name="contestant.lastname" id="lastname" maxlength="32">

                            </div>
                            <div class="col-sm-4">
                                Phone <span style="color: darkgrey">(123456789, +420123456789)</span>:
                                <input class="form-control"
                                       type="text"
                                       name="contestant.phone" id="phone" maxlength="16">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-4">
                                Email <span style="color: darkgrey">(6 - 32 length)</span>:
                                <input class="form-control"
                                       type="text"
                                       name="contestant.email" id="email" maxlength="32">

                            </div>
                            <c:if test="${not empty con_categories}">
                                <div class="col-sm-4">
                                    Contestant category<span style="color: red;">*</span>:
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
                                    <label><input type="checkbox" name="contestant.paid" value="true"
                                                  id="paid">Paid</label>
                                </div>
                            </div>
                        </div>
                        <hr>

                        <div class="row">
                            <div class="col-sm-12" style="text-align: right">
                                <input type="button" class="btn btn-primary"
                                       onclick="adminSoloRegistration(${race.id});" value="Solo registration">
                            </div>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form:form>
                    <div id="admin_solo_result"></div>
                    <br><br><br><br>
                </c:if>
                </c:if>

                    <%-- USER SOLO REGISTRATION --%>

                <c:choose>
                    <c:when test="${race.registration eq true || race_cooperator eq true || race.user.id eq user.id}">
                        <c:if test="${race.teamSize gt 1}">
                            <div class="well well-lg">
                                <div style="text-align: center;">SOLO REGISTRATION</div>
                            </div>

                            <form:form id="soloContestantForm">

                                <c:if test="${not empty con_categories}">
                                    Contestant category<span style="color: red;">*</span>:
                                    <select class="form-control" name="category">
                                        <c:forEach items="${con_categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>
                                    <br>
                                </c:if>

                                <div class="alert alert-warning">Informations about you will be take from your
                                    account.
                                </div>

                                <hr>

                                <div class="row">
                                    <div class="col-sm-12" style="text-align: right">
                                        <input class="btn btn-primary" type="button"
                                               onclick="soloRegistration(${race.id})" style="color: white;"
                                               value="Solo registration">
                                    </div>
                                </div>

                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                            </form:form>

                            <div id="solo_reg_result"></div>

                            <br><br><br><br>
                        </c:if>

                        <%-- USER TEAM REGISTRATION --%>

                        <div class="well well-lg">
                            <div style="text-align: center;">
                                <c:if test="${race.teamSize gt 1}">TEAM REGISTRATION</c:if>
                                <c:if test="${race.teamSize eq 1}">REGISTRATION</c:if>
                            </div>
                        </div>

                        <form:form id="teamRegistrationForm">

                            <div class="row">
                                <c:if test="${race.teamSize gt 1}">
                                    <div class="col-sm-6">
                                        Team name <span style="color: darkgrey">(3 - 32 length)</span>:
                                        <input class="form-control" name="teamName" maxlength="32"/>
                                    </div>
                                </c:if>
                                <c:if test="${not empty team_categories}">
                                    <div class="col-sm-6">
                                        <c:if test="${race.teamSize gt 1}">Team category:<span style="color: red;">*</span></c:if>
                                        <c:if test="${race.teamSize eq 1}">Race category:<span style="color: red;">*</span></c:if>
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
                                        Contestant category<span style="color: red;">*</span>:
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
                                            Firstname<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                                            <input class="form-control"
                                                   name="contestants[${i.index}].firstname" maxlength="32"/>
                                        </div>
                                        <div class="col-sm-4">
                                            Lastname<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                                            <input class="form-control"
                                                   type="text"
                                                   name="contestants[${i.index}].lastname" maxlength="32">

                                        </div>
                                        <div class="col-sm-4">
                                            Phone <span style="color: darkgrey">(123456789, +420123456789)</span>:
                                            <input class="form-control"
                                                   type="text"
                                                   name="contestants[${i.index}].phone" maxlength="16">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            Email<span style="color: darkgrey">(6 - 32 length)</span>:
                                            <input class="form-control"
                                                   type="text"
                                                   name="contestants[${i.index}].email" maxlength="32">

                                        </div>

                                        <c:if test="${not empty con_categories}">
                                            <div class="col-sm-4">
                                                Contestant category<span style="color: red;">*</span>:
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
                                    <c:if test="${race.teamSize gt 1}">
                                        <input class="btn btn-primary" type="button"
                                               onclick="teamRegistration(${race.id})" style="color: white;"
                                               value="Team registration">
                                    </c:if>
                                    <c:if test="${race.teamSize eq 1}">
                                        <input class="btn btn-primary" type="button"
                                               onclick="teamRegistration(${race.id})" style="color: white;"
                                               value="Registration">
                                    </c:if>
                                </div>
                            </div>
                        </form:form>
                        <div id="team_reg_result" style="margin-bottom: 45px;"></div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">Registration disabled</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </jsp:body>
</t:template>
