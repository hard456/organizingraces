<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>HelloWorld page</title>
</head>
<body>
Greeting : ${greeting}
<br/>
<h3>This is a welcome page!!!</h3>

Click here for <a href="<c:url value="/login" />">Login</a>.
</body>
</html>