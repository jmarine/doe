<%@page import="java.net.URLEncoder" session="false" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Collection, java.util.List, java.sql.*, net.java.doe.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<html>
    <head>
        <title>DOE tables</title>
        <script language="javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
        <script language="javascript">
            $(document).ready(function() {
                $('#table_name').click(function() {
                   javascript:$('#table_info').toggle();
                   return false;
                });
            })
        </script>
    </head>
    <body>
        Table: <span id="table_name" style="text-decoration: underline"><c:out value="${tableInfo.name}"/></span><br>
        
        <form method="POST" action="<c:url value="/table/${tableInfo.name}"/>">
            <button name="_action" value="SEARCH">Search</button>
            <button name="_action" value="NEW">Create</button>
        </form>

        <c:out value="${msg}" />

        
        <% if("POST".equals(request.getMethod())) { %>
        <table border="1">
                <thead>
                    <tr>
                        <c:forEach items="${tableInfo.columns}" var="column">
                            <th><c:out value="${column.name}"/></th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>            
        <% 
            int count = 0;
            TableInfo tableInfo = (TableInfo)request.getAttribute("tableInfo");
            int cols = tableInfo.getColumns().size();
            
            ResultSet rs = (ResultSet)request.getAttribute("rs");
            
            while(rs != null && rs.next()) {
                count++;
        %>
        
                <tr>
        <%
                
                for(int i = 0; i < cols; i++) {
                    int col = i+1;
                    ColumnInfo columnInfo = tableInfo.getColumnInfo(col);
        %>
        <td>
            <% if(columnInfo.isPrimaryKey()) { 
                  StringBuffer queryString = new StringBuffer();
                  Collection<ColumnInfo> pk = tableInfo.getPrimaryKey();
                  for(ColumnInfo pkColumnInfo : pk) {
                    if(queryString.length() > 0) queryString.append("&");
                    queryString.append(pkColumnInfo.getName());
                    queryString.append('=');
                    queryString.append(URLEncoder.encode(tableInfo.getValue(rs, pkColumnInfo), response.getCharacterEncoding()));
                  }
                  request.setAttribute("pk", queryString);
            %>
            <a href="<c:url value="/table/${tableInfo.name}?${pk}&_page=${page}&_query=${query}"/>">
            <% } else { 
            %>
            
            <% } %>
            
            <%= tableInfo.getValue(rs, tableInfo.getColumnInfo(col)) %>
            
            <% if(tableInfo.getColumnInfo(col).isPrimaryKey()) { %>
            </a>
            <% } %>
        </td>
        
        <%      
                } 
        %>
                </tr>
        <%
            }
        %>
                </tbody>
        <table>
            
            
        Total count: <%= count %>
        <% }  // "POST" method 
        %>
        
    </body>
</html>
