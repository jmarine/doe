package net.java.doe;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;


public class DbInfo 
{
    private static HashMap<String, TableInfo> infoTableCache;
    
    public static synchronized HashMap<String, TableInfo> initTables(DataSource ds, boolean refresh) throws Exception
    {
        if(refresh || infoTableCache == null) {
            infoTableCache = new HashMap<String, TableInfo>();
            Connection con = ds.getConnection();
            DatabaseMetaData md = con.getMetaData();
            String user_schema = md.getUserName().toUpperCase();
            ResultSet tables = md.getTables(null, user_schema, "%", new String[] { "TABLE" } );
            while(tables.next()) {
                String catalog = tables.getString(1);
                String schema = tables.getString(2);
                String tableName = tables.getString(3);
                TableInfo tableInfo = initTableInfo(md, catalog, schema, tableName);
                infoTableCache.put(tableInfo.getName(), tableInfo);
            }
            tables.close();
        }
        return infoTableCache;
    }
    
    
    private static TableInfo initTableInfo(DatabaseMetaData md, String catalog, String schema, String tableName) throws SQLException
    {
        TableInfo tableInfo = new TableInfo(catalog, schema, tableName);
        ResultSet column = md.getColumns(null, schema, tableName, null);
        while(column.next()) {
            ColumnInfo columnInfo = new ColumnInfo(column);
            tableInfo.addColumnInfo(columnInfo);
        }
        column.close();
        
        ResultSet primaryKey = md.getPrimaryKeys(catalog, schema, tableName);
        while(primaryKey.next()) {
            String columnName = primaryKey.getString(4);
            int keySeq = primaryKey.getInt(5);
            ColumnInfo columnInfo = tableInfo.getColumnInfo(columnName);
            columnInfo.setPrimaryKeySeq(keySeq);
        }
        primaryKey.close();

        ResultSet foreignKey = md.getImportedKeys(catalog, schema, tableName);
        while(foreignKey.next()) {
            ForeignKeyInfo foreignKeyInfo = new ForeignKeyInfo(foreignKey);
            tableInfo.addForeignKeyInfo(foreignKeyInfo);
        }        
        foreignKey.close();
        
        return tableInfo;
    }    
    
    
    public static TableInfo getTableInfo(String tableName)
    {
        return infoTableCache.get(tableName);
    }
    
    
}
