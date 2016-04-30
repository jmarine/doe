<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List, java.util.HashMap, net.java.doe.TableInfo" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>DOE tables</title>
    </head>
    <body>
        <c:forEach items="${tables}" var="table">
            <a href="<c:url value="/table/${table.name}"/>"><c:out value="${table.name}"/></a><br>
        </c:forEach>        
    </body>
</html>
