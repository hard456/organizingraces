<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">

                <c:choose>
                <c:when test="${race.user.id eq user.id}">

                    <div style="max-width: 650px; margin: 0 auto;">

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

            </div>

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

    </jsp:body>
</t:template>