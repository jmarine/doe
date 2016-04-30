<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>DOE tables</title>
    </head>
    <body>
        Error: <%= request.getAttribute("message") %><br>
        <button name="_action" onclick="javascript:history.back()">Back</button>
    </body>
</html>
