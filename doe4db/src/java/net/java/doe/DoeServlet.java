package net.java.doe;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet(name = "DoeServlet", urlPatterns = {"/table/*"})
public class DoeServlet extends HttpServlet 
{
    @Resource(lookup = "jdbc/doe4db")
    private DataSource ds;
    
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        System.out.println("DoeServlet.init: ENTER");
        super.init(config);
        try { 
            //Class.forName("org.apache.derby.jdbc.ClientDataSource"); 
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:comp/env");
            ds = (DataSource)envContext.lookup("jdbc/doe4db");
           
        } catch (Exception ex) {
            System.out.println("DoeServlet.init: Error: " + ex.getMessage());
            Logger.getLogger(DoeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        System.out.println("DoeServlet.service: ENTER");
        String buttonAction = request.getParameter("_action");        
        if(buttonAction == null) buttonAction = "";
        
        String tableName = request.getRequestURI();
        int tableNameStart = tableName.indexOf("/table/");
        tableName = (tableNameStart == -1) ? "" : tableName.substring(tableNameStart + 7);
        int tableNameEnd = tableName.indexOf(";");
        if(tableNameEnd != -1) tableName = tableName.substring(0, tableNameEnd);
        
        try {
            if(tableName == null || tableName.length() == 0) {
                index(request, response);
            } else {
                HashMap<String, String> pk = new HashMap<String,String>();
                String urlQueryString = request.getQueryString();
                if(urlQueryString != null) {
                    String encoding = request.getCharacterEncoding();
                    if(encoding == null) encoding = "ISO-8859-1";
                    StringTokenizer paramsParser = new StringTokenizer(urlQueryString, "&");
                    while(paramsParser.hasMoreTokens()) {
                        StringTokenizer valuesParser = new StringTokenizer(paramsParser.nextToken(), "=");
                        while(valuesParser.hasMoreTokens()) {
                            String key = valuesParser.nextToken();
                            if(!key.startsWith("_") && valuesParser.hasMoreTokens()) {
                                String value = valuesParser.nextToken();
                                pk.put(key, URLDecoder.decode(value, encoding));
                            }
                        }
                    }
                }
                
                if(pk.size() == 0 && "GET".equals(request.getMethod())) {
                    buttonAction = "SEARCH";
                }
                

                switch(buttonAction) {
                    case "":
                    case "NEW":
                        edit(request, response, DbInfo.getTableInfo(tableName), pk);
                        break;
                    case "INSERT":
                        insert(request, response, DbInfo.getTableInfo(tableName), null);
                        break;
                    case "UPDATE":
                        update(request, response, DbInfo.getTableInfo(tableName), pk);   
                        break;
                    case "DELETE":
                        delete(request, response, DbInfo.getTableInfo(tableName), pk);
                        break;
                    case "SEARCH":
                        search(request, response, DbInfo.getTableInfo(tableName));
                        break;                                                
                        
                }
            }
            
        } catch (Exception ex) {
            System.out.println("DoeServlet.service: Error: " + ex.getMessage());
            Logger.getLogger(DoeServlet.class.getName()).log(Level.SEVERE, null, ex);

            request.setAttribute("message", ex.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/error.jsp");
            rd.forward(request, response);
        }        
    }
    
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        String refreshParam = request.getParameter("refresh");
        boolean refresh = (refreshParam != null && refreshParam.equalsIgnoreCase("true"));
        request.setAttribute("tables", DbInfo.initTables(ds, refresh).values());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
        rd.forward(request, response);
    }
    
    
    
    public void search(HttpServletRequest request, HttpServletResponse response, TableInfo tableInfo) throws Exception 
    {
        String page = request.getParameter("_page");
        if(page == null) page = "0";
        
        request.setAttribute("tableInfo", tableInfo);
        request.setAttribute("page", page);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/search.jsp");

        if("POST".equals(request.getMethod())) {
            String encoding = request.getCharacterEncoding();
            if(encoding == null) encoding = "ISO-8859-1";

            String query = request.getParameter("_query");
            if(query == null || query.length() == 0) query = "SELECT * FROM " + tableInfo.getName();
            
            request.setAttribute("query", URLEncoder.encode(query, encoding));
            
            Connection con = ds.getConnection();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(query);
            request.setAttribute("rs", rs);
            rd.forward(request, response);
            
            rs.close();
            stat.close();
            con.close();
            
        } else {
            
            rd.forward(request, response);
            
        }
            
    } 
    
    
    public void delete(HttpServletRequest request, HttpServletResponse response, TableInfo tableInfo, HashMap<String,String> pk) throws Exception 
    {
        StringBuilder query = new StringBuilder("DELETE FROM " + tableInfo.getName());
        StringBuilder where = new StringBuilder();
        for(String key : pk.keySet()) {
            if(where.length() != 0) where.append(" AND ");
            where.append(key);
            where.append(" = ?");
        }
        if(where.length() > 0) {
            query.append(" WHERE ");
            query.append(where.toString());
        }
        
        int paramCount = 0;
        Connection con = ds.getConnection();
        PreparedStatement pstat = con.prepareStatement(query.toString());
        for(String key : pk.keySet()) {
            paramCount++;
            String value = pk.get(key);
            setPstatValue(pstat, tableInfo, key, paramCount, value);
        }
        
        int rows = pstat.executeUpdate();
        if(rows > 0) {
            request.setAttribute("msg", "Deleted " + rows + " row/s.");
        } else {
            request.setAttribute("msg", "No rows deleted.");
        }
        request.setAttribute("con", con);
        request.setAttribute("rows", rows);

        //RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/search.jsp");
        search(request, response, tableInfo);  // update entity list
        
        pstat.close();
        con.close();        

    }
    

    public void edit(HttpServletRequest request, HttpServletResponse response, TableInfo tableInfo, HashMap<String,String> pk) throws Exception 
    {
        request.setAttribute("tableInfo", tableInfo);
        request.setAttribute("pk", pk);
        request.setAttribute("page", request.getParameter("_page"));
        request.setAttribute("query", request.getParameter("_query"));
        
        if(pk == null || pk.size() == 0) {  // new entity to INSERT
            request.setAttribute("action", "INSERT");

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/edit.jsp");
            rd.forward(request, response);
            
        } else {        // existing entity to UPDATE
            request.setAttribute("action", "UPDATE");

            StringBuilder query = new StringBuilder("SELECT * FROM " + tableInfo.getName());
            StringBuilder where = new StringBuilder();
            for(String key : pk.keySet()) {
                if(where.length() != 0) where.append(" AND ");
                where.append(key);
                where.append(" = ?");
            }
            if(where.length() > 0) {
                query.append(" WHERE ");
                query.append(where.toString());
            }

            int paramCount = 0;
            Connection con = ds.getConnection();
            PreparedStatement pstat = con.prepareStatement(query.toString());
            for(String key : pk.keySet()) {
                paramCount++;
                String value = pk.get(key);
                setPstatValue(pstat, tableInfo, key, paramCount, value);
            }

            ResultSet rs = pstat.executeQuery();
            if(rs != null && rs.next()) request.setAttribute("rs", rs);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/edit.jsp");
            rd.forward(request, response);
            
            rs.close();
            pstat.close();
            con.close();
        }
    }
    
    
    public void insert(HttpServletRequest request, HttpServletResponse response, TableInfo tableInfo, HashMap<String,String> pk) throws Exception 
    {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for(ColumnInfo column : tableInfo.getColumns()) {
            if(columns.length() > 0) {
                columns.append(", ");
                values.append(",");
            }
            columns.append(column.getName());
            values.append("?");
        }

        StringBuilder query = new StringBuilder("INSERT INTO " + tableInfo.getName());
        query.append("("); query.append(columns.toString()); query.append(") ");
        query.append("VALUES("); query.append(values.toString()); query.append(")");

        int paramCount = 0;
        Connection con = ds.getConnection();
        PreparedStatement pstat = con.prepareStatement(query.toString());

        for(ColumnInfo column : tableInfo.getColumns()) {
            paramCount++;
            String value = request.getParameter(column.getName());
            setPstatValue(pstat, tableInfo, column.getName(), paramCount, value);
        }

        int rows = pstat.executeUpdate();
        request.setAttribute("msg", "Inserted " + rows + " row.");

        search(request, response, tableInfo);  // update entity list

        pstat.close();
        con.close();

    }        
    
    public void update(HttpServletRequest request, HttpServletResponse response, TableInfo tableInfo, HashMap<String,String> pk) throws Exception 
    {
        StringBuilder query = new StringBuilder("UPDATE " + tableInfo.getName());
        StringBuilder set = new StringBuilder();
        
        for(ColumnInfo column : tableInfo.getColumns()) {
            if(set.length() > 0) set.append(", ");
            String value = request.getParameter(column.getName());
            if(pk.get(column.getName()) == null || !pk.get(column.getName()).equals(value)) {
                set.append(column.getName());
                set.append(" = ? ");
            }
        }
        
        if(set.length() > 0) {
            query.append(" SET ");
            query.append(set.toString());
            
            StringBuilder where = new StringBuilder();
            for(String key : pk.keySet()) {
                if(where.length() != 0) where.append(" AND ");
                where.append(key);
                where.append(" = ?");
            }
            
            if(where.length() > 0) {
                query.append(" WHERE ");
                query.append(where.toString());
            }

            int paramCount = 0;
            Connection con = ds.getConnection();
            PreparedStatement pstat = con.prepareStatement(query.toString());
            
            for(ColumnInfo column : tableInfo.getColumns()) {
                if(set.length() > 0) set.append(", ");
                String value = request.getParameter(column.getName());
                if(pk.get(column.getName()) == null || !pk.get(column.getName()).equals(value)) {
                    paramCount++;
                    setPstatValue(pstat, tableInfo, column.getName(), paramCount, value);
                }
            }

            for(String key : pk.keySet()) {
                paramCount++;
                String value = pk.get(key);
                setPstatValue(pstat, tableInfo, key, paramCount, value);
            }        
        
        
            int rows = pstat.executeUpdate();
            request.setAttribute("msg", "Updated " + rows + " row/s.");
           
            search(request, response, tableInfo);  // update entity list
            
            pstat.close();
            con.close();
        
        }
        
    }    
    
    
    private void setPstatValue(PreparedStatement pstat, TableInfo tableInfo, String columnName, int paramCount, String value) throws Exception {
        ColumnInfo columnInfo = tableInfo.getColumnInfo(columnName);
        switch(columnInfo.getType()) {
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
                pstat.setString(paramCount, value);
                break;
            case java.sql.Types.INTEGER:
                pstat.setInt(paramCount, Integer.parseInt(value));
                break;
            case java.sql.Types.DECIMAL:
            case java.sql.Types.NUMERIC:
                pstat.setDouble(paramCount, Double.parseDouble(value));
                break;
            case java.sql.Types.TIME:
                pstat.setTime(paramCount, java.sql.Time.valueOf(value));
                break;
            case java.sql.Types.DATE:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat.parse(value);
                pstat.setDate(paramCount, new java.sql.Date(parsedDate.getTime()));
                break;
            case java.sql.Types.TIMESTAMP:
                SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                java.util.Date parsedTimestamp = timestampFormat.parse(value);
                pstat.setTimestamp(paramCount, new java.sql.Timestamp(parsedTimestamp.getTime()));
                break;
        }        
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "DOE servlet";
    }// </editor-fold>

}
