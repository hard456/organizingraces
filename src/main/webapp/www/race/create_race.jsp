<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script src="${pageContext.request.contextPath}/js/create_race.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 800px; margin: 0 auto;">

                <form:form id="createEventForm">
                    Race name<span style="color: red;">*</span> <span style="color: darkgrey">(3 - 32 length)</span>:
                    <input type="text" maxlength="32" style="margin-bottom: 7px;" class="form-control"
                           placeholder="Race name" name="race.name">
                    Size of team<span style="color: red;">*</span>:
                    <select name="race.teamSize" class="form-control">
                        <option value="1">1</option>
                        <option selected value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>


                        <br><br><br><br>

                        <div class="well well-lg">
                            <div style="text-align: center;">Define contestant categories</div>
                        </div>

                        <div class="radio row">
                            <div class="col-sm-4">
                                <label><input type="radio" name="conRadio" value="none" checked="checked">None
                                </label>
                            </div>
                        </div>
                    <c:if test="${not empty con_categories}">
                        <hr>
                        <div class="row">
                            <div class="radio col-sm-4">
                                <label><input type="radio" name="conRadio" value="defaultValue">Use default
                                    category</label>
                            </div>
                            <div class="col-sm-8">
                                <select class="form-control" name="defConCategoryId">
                                    <c:forEach var="c" items="${con_categories}">
                                        <option value="${c.id}">${c.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                    <hr>
                    <div class="row">
                        <div class="radio col-sm-4">
                            <label><input type="radio" name="conRadio" value="own">Create own categories</label>
                        </div>
                    </div>
                    Category 1 <span style="color: darkgrey">(1 - 20 length)</span>:
                    <input type="text" maxlength="20" class="form-control" style="margin-bottom: 7px;" name="contestantSubCategories[0].name"/>
                    Category 2 <span style="color: darkgrey">(1 - 20 length)</span>:
                    <input type="text" maxlength="20" class="form-control" name="contestantSubCategories[1].name" style="margin-bottom: 7px;">

                    <div id="con">

                    </div>

                    <div class="row">
                        <div class="col-sm-12" style="text-align: right">

                            <input type="button" class="btn btn-danger" value="-"
                                   onClick="removeCategory('con');">

                            <input type="button" class="btn btn-success" value="+"
                                   onClick="addCategory('con');">

                        </div>
                    </div>

                        <br><br><br><br>

                        <div class="well well-lg">
                            <div style="text-align: center;">Define team categories</div>
                        </div>

                        <div class="row">
                            <div class="radio col-sm-4">
                                <label><input type="radio" name="teamRadio" checked="checked" value="none">None
                                </label>
                            </div>
                        </div>
                    <c:if test="${not empty team_categories}">
                        <hr>
                        <div class="row">
                            <div class="radio col-sm-4">
                                <label><input type="radio" name="teamRadio" value="defaultValue">Use default
                                    category</label>
                            </div>
                            <div class="col-sm-8">
                                <select class="form-control" name="defTeamCategoryId">
                                    <c:forEach var="c" items="${team_categories}">
                                        <option value="${c.id}">${c.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                    <hr>
                    <div class="row">
                        <div class="radio col-sm-4">
                            <label><input type="radio" name="teamRadio" value="own">Create own categories</label>
                        </div>
                    </div>

                    Category 1 <span style="color: darkgrey">(1 - 20 length)</span>:
                    <input type="text" maxlength="20" class="form-control" name="teamSubCategories[0].name" style="margin-bottom: 7px;">
                    Category 2 <span style="color: darkgrey">(1 - 20 length)</span>:
                    <input type="text" maxlength="20" class="form-control" name="teamSubCategories[1].name" style="margin-bottom: 7px;">

                    <div id="team">

                    </div>

                    <div class="row">
                        <div class="col-sm-12" style="text-align: right">

                            <input type="button" class="btn btn-danger" value="-"
                                   onClick="removeCategory('team');">

                            <input type="button" class="btn btn-success" value="+"
                                   onClick="addCategory('team');">

                        </div>
                    </div>

                    <br><br>
                    <div style="text-align: center;">
                        <input class="btn btn-primary" type="button" name="submit" onclick="createRace()" value="Create race" style="color: white;">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form:form>
                <div id="create_race_result" style="margin: 10px 0 80px 0;"></div>
            </div>
        </div>

    </jsp:body>
</t:template>
