<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script src="/js/race_cooperation.js" language="Javascript" type="text/javascript"></script>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div style="margin: 30px 0 30px 0;">
            <div style="margin: 0 auto; max-width: 650px;">

                <div class="well">
                    <div style="text-align: center;">
                        ABOUT THE RACE
                    </div>
                </div>

                <div class="row" style="margin-bottom: 5px;">
                    <div class="col-sm-6">
                        Race name:
                    </div>
                    <div class="col-sm-6">
                            ${race.name}
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        Default size of team:
                    </div>
                    <div class="col-sm-6">
                            ${race.teamSize}
                    </div>
                </div>

                <c:if test="${race.user.id eq user.id}">

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            ADD RACE COOPERATOR
                        </div>
                    </div>

                    <form:form id="addCooperatorForm" method="POST">
                        <div class="row">
                            <div class="col-sm-6">Cooperator login:</div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <input class="form-control"
                                       type="text"
                                       name="login" maxlength="32">
                            </div>
                            <div class="col-sm-3" style="text-align: right;">
                                <input type="button" class="btn btn-primary" onclick="addCooperator(${race.id});" value="Add cooperator">
                            </div>
                        </div>
                    </form:form>

                    <div id="add_cooperator_result"></div>

                    <br><br><br><br>

                    <div class="well">
                        <div style="text-align: center;">
                            DELETE THE RACE
                        </div>
                    </div>

                    <form:form action="/race/${race.id}/deleteRace" method="POST">
                        <div class="row">
                            <div class="col-sm-6">Password:</div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <input class="form-control"
                                       type="password"
                                       name="password">
                            </div>
                            <div class="col-sm-3" style="text-align: right;">
                                <button class="btn btn-danger" type="submit" name="submit"><span
                                        style="color: white;">Delete race</span></button>
                            </div>
                        </div>
                    </form:form>

                </c:if>
            </div>
        </div>

    </jsp:body>
</t:template>