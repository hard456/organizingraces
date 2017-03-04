<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>
        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 900px; margin: 0 auto;">

                <c:choose>
                    <c:when test="${race.userId eq user.id}">

                        <div class="row hidden-xs" style="background: lightcoral; padding: 5px 0 5px 0; color: white;">
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

                            <div class="row">
                                <div class="col-sm-2"><input type="text" value="${c.firstname}" class="form-control"
                                                             style="margin-bottom: 5px;"></div>
                                <div class="col-sm-2"><input type="text" value="${c.lastname}" class="form-control"
                                                             style="margin-bottom: 5px;"></div>
                                <div class="col-sm-2"><input type="text" value="${c.email}" class="form-control"
                                                             style="margin-bottom: 5px;"></div>
                                <div class="col-sm-2"><input type="text" value="${c.firstname}" class="form-control"
                                                             style="margin-bottom: 5px;"></div>
                                <div class="col-sm-2"><input type="text" value="${c.category}" class="form-control"
                                                             style="margin-bottom: 5px;"></div>
                                <div class="col-sm-1">
                                    <c:choose>
                                        <c:when test="${c.paid eq true}">
                                            <button class="btn btn-success btn-sm" type="submit" name="submit"
                                                    style="margin-bottom: 5px; width: 100%"><span
                                                    style="color: white;">YES</span></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-danger btn-sm" type="submit" name="submit"
                                                    style="margin-bottom: 5px; width: 100%"><span
                                                    style="color: white;">NO</span></button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="col-sm-1">
                                    <button class="btn btn-success btn-sm" type="submit" name="submit"
                                            style="margin-bottom: 5px; width: 100%"><span
                                            style="color: white;">Save</span></button>
                                </div>
                            </div>
                            <hr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger">
                            You are not allowed to see this section.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </jsp:body>
</t:template>
