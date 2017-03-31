<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>

        <div class="card-log" style="margin-top: 25px;">
            <div style="max-width: 650px; margin: 0 auto;">

                <div class="row">
                    <div class="col-sm-4">
                        <input type="button" value="Data export" class="btn btn-primary">
                    </div>
                </div>

            </div>
        </div>
    </jsp:body>
</t:template>
