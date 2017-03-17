<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:template>
    <jsp:body>

        <t:race_menu />
        <div style="margin: 30px 0 30px 0;">
        <div style="margin: 0 auto; max-width: 400px;">
            <div class="row">
                <div class="col-sm-6">
                    Race name:
                </div>
                <div class="col-sm-6">
                    ${race.name}
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    Default team size:
                </div>
                <div class="col-sm-6">
                        ${race.teamSize}
                </div>
            </div>
        </div>
        </div>
    </jsp:body>
</t:template>