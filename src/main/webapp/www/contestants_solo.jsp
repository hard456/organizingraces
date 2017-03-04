<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 850px; margin: 0 auto;">

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
                        <div class="col-sm-2"><input type="text" value="${c.email}" class="form-control"
                                                     style="margin-bottom: 5px;"></div>
                        <div class="col-sm-3"><input type="text" value="${c.category}" class="form-control"
                                                     style="margin-bottom: 5px;"></div>
                        <div class="col-sm-2" style="text-align: right">
                            <button class="btn btn-danger btn-sm" type="submit" name="submit"
                                    style="margin-bottom: 5px;"><span
                                    style="color: white;">Delete</span></button>
                        </div>
                    </div>
                    <hr>
                </c:forEach>
            </div>
        </div>
    </jsp:body>
</t:template>
