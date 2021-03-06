<%@ tag description="Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- Mobile setting-->
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Own CSS -->
    <spring:url value="/css/style.css" var="style"/>
    <link rel="stylesheet" href="${style}">

    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script language="JavaScript" type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <title>Orienteering Races</title>
</head>
<body>

<script>var BASE_URL = "${pageContext.request.contextPath}";</script>

<!-- HEADER -->

<div id="header">
    <div style="max-width: 100%; height: auto; background-color: #336666">
        <div style="margin: 0 auto; padding: 14px; max-width: 1000px; height: auto;">
            <%--<span class="glyphicon glyphicon-floppy-save" aria-hidden="true"--%>
                  <%--style="font-size:2em; color: white;"></span>--%>
            <%--<a href="${pageContext.request.contextPath}/" style="text-decoration: none;"><span style="color: white; font-size: 35px">Orienteering Races</span></a>--%>
                <img style="width: 228px; height: 84px;" src="${pageContext.request.contextPath}/images/logo.png">
            <br>
            <span style="color: cadetblue; font-size: 18px;">SUPPORT FOR YOUR ORIENTEERING RACES</span>
        </div>
    </div>
</div>

<!-- MENU -->

<div id="menu">
    <nav class="navbar-default" data-offset-top="140" style="z-index:1000; margin: 0 auto; max-width: 1000px">
        <div class="container-fluid" style="background-color: white">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                    <span class="glyphicon glyphicon-menu-hamburger"></span>

                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/"><span
                        class="glyphicon glyphicon-home"></span></a>
            </div>
            <div class="collapse navbar-collapse" id="myNavbar">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="${pageContext.request.contextPath}/avaible_races">
                            <span>Available races</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/evaluated_races">
                            <span>Evaluated races</span>
                        </a>
                    </li>

                    <sec:authorize access="isAuthenticated()">
                    <li><a href="${pageContext.request.contextPath}/create_race">Create race</a></li>
                    <li><a href="${pageContext.request.contextPath}/my_races">My races</a></li>
                    </sec:authorize>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <sec:authorize access="!isAuthenticated()">
                    <li><a href="${pageContext.request.contextPath}/registration">Registration</a></li>
                    </sec:authorize>

                    <sec:authorize access="isAuthenticated()">
                    <li><a href="${pageContext.request.contextPath}/userpage"><span
                            class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.username"/></a></li>
                    </sec:authorize>

                    <sec:authorize access="isAuthenticated()">
                        <li><a href="${pageContext.request.contextPath}/logout"><span
                            class="glyphicon glyphicon-log-out"></span> Logout</a></li>

                    </sec:authorize>
                    <sec:authorize access="!isAuthenticated()">
                        <li><a href="${pageContext.request.contextPath}/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
                    </sec:authorize>
                </ul>
            </div>
        </div>
    </nav>
</div>

<!-- BODY -->

<div id="body">
    <jsp:doBody/>
</div>

<!-- FOOTER -->

<%--<div id="pagefooter">--%>
    <%--<div class="container-fluid" style="background-color: cornflowerblue;">--%>
        <%--<div class="container" style="margin: 30px auto 30px auto; max-width: 970px;">--%>
            <%--<div class="row">--%>
                <%--<div class="col-xs-10 col-md-11">--%>
                    <%--<span style="color: white; font-size: 15px">© 2016 Jan Palcút. All Rights Reserved.</span>--%>
                <%--</div>--%>
                <%--<div class="col-xs-2 col-md-1">--%>
                    <%--<a href="#header" style="float: right; margin-right: 15px;">--%>
                        <%--<span style="color:white" class="glyphicon glyphicon-chevron-up"></span>--%>
                    <%--</a>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
</body>
</html>