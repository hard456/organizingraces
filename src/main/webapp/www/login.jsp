<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:template>
    <jsp:body>
        <form:form method="POST" action="/login">
            <table>
                <tr>
                    <td><form:label path="">Name</form:label></td>
                    <td><form:input path=""/></td>
                </tr>
                <tr>
                    <td><form:label path="">Age</form:label></td>
                    <td><form:input path=""/></td>
                </tr>
                <tr>
                    <td><form:label path="id">id</form:label></td>
                    <td><form:input path="id" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Login"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </jsp:body>
</t:template>