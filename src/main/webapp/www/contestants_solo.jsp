<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 850px; margin: 0 auto;">

                <c:choose>
                    <c:when test="${empty contestants}">
                        <div class="alert alert-warning">
                            List of solo contestants is empty!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                        <c:when test="${race_cooperator eq true || race.userId eq user.id}">
                        <form:form action="/race/${race.id}/addTeamByAdmin" modelAttribute="contestantList"
                                   method="POST">

                            <div class="row hidden-xs">
                                <div class="col-sm-5">
                                    Team name:
                                </div>
                                <div class="col-sm-5">
                                    Team category:
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-5">
                                    <div class="visible-xs">Team name:</div>
                                    <input class="form-control" style="margin-bottom: 5px;" name="name"
                                           placeholder="Team name"/>
                                </div>
                                <div class="col-sm-5">
                                    <div class="visible-xs">Category:</div>
                                    <select class="form-control" style="margin-bottom: 5px;" name="category">
                                        <option selected value="Smrtelník">Smrtelník</option>
                                        <option value="Orienťák">Orienťák</option>
                                    </select>
                                </div>
                                <div class="col-sm-2" style="text-align: right;">
                                    <button class="btn btn-primary visible-xs" type="submit" name="submit"
                                            style="margin-bottom: 5px; width: 100%"><span
                                            style="color: white;">Team registration</span></button>
                                    <button class="btn btn-primary hidden-xs" type="submit" name="submit"
                                            style="margin-bottom: 5px;"><span
                                            style="color: white;">Registration</span></button>
                                </div>
                            </div>

                            <br><br>

                            <c:forEach var="c" varStatus="i" items="${contestants}">

                                <div class="row">

                                    <div class="col-sm-1">
                                        <button class="btn btn-success btn-sm" type="submit" name="submit"
                                                style="margin-bottom: 5px;"><span
                                                style="color: white;">+</span></button>
                                    </div>

                                    <div class="col-sm-2"><input type="text" value="${c.firstname}" class="form-control"
                                                                 style="margin-bottom: 5px;"></div>
                                    <div class="col-sm-2"><input type="text" value="${c.lastname}" class="form-control"
                                                                 style="margin-bottom: 5px;"></div>
                                    <div class="col-sm-3"><input type="text" value="${c.email}" class="form-control"
                                                                 style="margin-bottom: 5px;"></div>
                                    <div class="col-sm-2"><input type="text" value="${c.category}" class="form-control"
                                                                 style="margin-bottom: 5px;"></div>
                                    <div class="col-sm-2" style="text-align: right">
                                        <button class="btn btn-danger btn-sm" type="submit" name="submit"
                                                style="margin-bottom: 5px;"><span
                                                style="color: white;">Delete</span></button>
                                    </div>
                                </div>
                                <c:if test="${not i.last}"><hr></c:if>
                            </c:forEach>

                        </form:form>
                        </c:when>
                            <c:otherwise>
                                <div class="row hidden-xs"
                                     style="background: lightcoral; padding: 5px 0 5px 0; color: white; text-align: center;">
                                    <div class="col-sm-3">Firstname</div>
                                    <div class="col-sm-3">Lastname</div>
                                    <div class="col-sm-3">Email</div>
                                    <div class="col-sm-3">Category</div>
                                </div>
                                <br>
                                <c:forEach var="c" varStatus="i" items="${contestants}">
                                <div class="row" style="text-align: center;">
                                    <div class="col-sm-3">${c.firstname}</div>
                                    <div class="col-sm-3">${c.lastname}</div>
                                    <div class="col-sm-3">${c.email}</div>
                                    <div class="col-sm-3">${c.category}</div>
                                </div>
                                    <c:if test="${not i.last}"><hr></c:if>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </jsp:body>
</t:template>
