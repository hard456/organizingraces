<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:template>
    <jsp:body>

        <t:race_menu/>


        <div class="card-log" style="margin-top: 25px;">
            <div style="text-align: center">SOLO REGISTRATION</div>

            <hr style="margin: 0 auto 7px; max-width: 1000px;">

            <form name="addSoloContestant" action="<c:url value="/race/${race.id}/addSoloContestant" />" method="POST">

                <div style="margin: 0 auto 20px; max-width: 250px;">
                    <select name="category" style="max-width: 250px">
                        <option selected value="NOT FROM WEST BOHEMIA UNIVERSITY">NOT FROM WEST BOHEMIA UNIVERSITY</option>
                        <option value="FAV">FAV</option>
                        <option value="FEL">FEL</option>
                        <option value="OTHERS">OTHERS</option>
                    </select>
                </div>

                <div style="text-align: center;">
                    <button class="btn btn-primary" type="submit" name="submit"><span
                            style="color: white;">Solo registration</span></button>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            </form>
        </div>

    </jsp:body>
</t:template>
