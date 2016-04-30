<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*, java.sql.*, net.java.doe.*" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<html>
    <head>
        <title>DOE tables</title>
        <script language="javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
    </head>
    <body>

        <c:out value="${msg}" />

        <h1>
        Edit <c:out value="${tableInfo.name}" /> entry:
        </h1>
        
        <form id="data" method="POST">
            
            <input type="hidden" name="_page" value="<c:out value="${page}"/>"/>
            <input type="hidden" name="_query" value="<c:out value="${query}"/>"/>
            
        <%
            TableInfo tableInfo = (TableInfo)request.getAttribute("tableInfo");
            ResultSet rs = (ResultSet)request.getAttribute("rs");
            if("UPDATE".equals(request.getAttribute("action"))) {
                Map<String,String> pks  = (Map<String,String>)request.getAttribute("pk");

                for(Map.Entry<String,String> entry : pks.entrySet()) {
                    ColumnInfo column = tableInfo.getColumnInfo(entry.getKey());
                    if(column.isPrimaryKey()) { 
                %>
                        <input type="hidden" name="pk.<%= entry.getKey() %>" value="<%= entry.getValue() %>">
                <% 
                    }
                }
            } 

            if(rs == null && "UPDATE".equals(request.getAttribute("action"))) {
%>
                <h1>No rows found to edit.</h1>
        <%
            } else {
        %>                
        
            <table> 
                <tbody>
            <% 

                    for(int i = 0; i < tableInfo.getColumns().size(); i++) {
                        ColumnInfo column = tableInfo.getColumnInfo(i+1);
                        String value = tableInfo.getValue(rs, column);
            %>
                        <tr>
                            <td><%= column.getName() %></td>
                            <td><%= EditorFactory.getEditor(column).edit(column, value, "YES".equals(column.getGenerated())) %></td>
                        </tr>
            <%      
                    }

            %>
                    </tbody>
            </table>
            
<%                
                if("UPDATE".equals(request.getAttribute("action"))) {                
        %>
                    <button name="_action" value="UPDATE">Update</button>
                    <button name="_action" value="DELETE">Delete</button>
        <%
                } else {
        %>              

                    <button name="_action" value="INSERT">Create</button>
<%                    
                } 
            }
%>        
            
        </form>
                

        <form id="back" method="POST" action="<c:url value="/table/${tableInfo.name}"/>">
            <input type="hidden" name="_page" value="<c:out value="${page}"/>"/>
            <input type="hidden" name="_query" value="<c:out value="${query}"/>"/>
            <button name="_action" value="SEARCH">Back</button>
        </form>                
        
    </body>
</html>
