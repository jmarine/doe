package net.java.doe;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;


public class TableInfo 
{
    private String catalog;
    private String schema;
    private String name;
    private String descriptionColumn;
    
    private ArrayList<ColumnInfo> columnsByPosition;
    private HashMap<String, ColumnInfo> columnsByName;
    
    private HashMap<String, ArrayList<ForeignKeyInfo>> navigationKeys;

    
    public TableInfo(String catalog, String schema, String name)
    {
        setCatalog(catalog);
        setSchema(schema);
        setName(name);
        columnsByName = new HashMap<String, ColumnInfo>();
        columnsByPosition = new ArrayList<ColumnInfo>();
        navigationKeys = new HashMap<String, ArrayList<ForeignKeyInfo>>();
    }
    
    public void addColumnInfo(ColumnInfo columnInfo)
    {
        columnsByName.put(columnInfo.getName(), columnInfo);

        columnsByPosition.ensureCapacity(columnInfo.getPos());
        while (columnsByPosition.size() < columnInfo.getPos()) {
            columnsByPosition.add(null);
        }
        columnsByPosition.set(columnInfo.getPos()-1, columnInfo);
        
        if(descriptionColumn == null) {
            switch(columnInfo.getName()) {
                case "NAME":
                case "TEXT":
                case "DESCRIPTION":
                    setDescriptionColumn(columnInfo.getName());
                    break;
            }
        }
    }
    
    public ColumnInfo getColumnInfo(String columnName)
    {
        return columnsByName.get(columnName);
    }
    
    public ColumnInfo getColumnInfo(int pos)
    {
        return columnsByPosition.get(pos-1);
    }

    public Collection<ColumnInfo> getColumns()
    {
        return columnsByPosition;
    }
    
    public void setDescriptionColumn(String descriptionColumn)
    {
        this.descriptionColumn = descriptionColumn;
    }
    
    public String getDescriptionColumnValue(ResultSet rs)
    {
        StringBuffer retval = new StringBuffer();
        StringTokenizer stk = new StringTokenizer(descriptionColumn, "+");
        while(stk.hasMoreTokens()) {
            String text = stk.nextToken();
            ColumnInfo columnInfo = getColumnInfo(text);
            if(columnInfo == null) {
                retval.append(text);
            } else {
                retval.append(getValue(rs, columnInfo));
            }
        }
        return retval.toString();
    }
    
    
    public boolean isPrimaryKey(String columnName) 
    {
        ColumnInfo columnInfo = columnsByName.get(columnName);
        return columnInfo.isPrimaryKey();
    }
    
    public Collection<ColumnInfo> getPrimaryKey()
    {
        ArrayList<ColumnInfo> cols = new ArrayList<ColumnInfo>(1);
        for(ColumnInfo columnInfo : columnsByPosition) {
            if(columnInfo.isPrimaryKey()) {
                cols.ensureCapacity(columnInfo.getPrimaryKeySeq());
                while(cols.size() < columnInfo.getPrimaryKeySeq()) cols.add(null);
                cols.set(columnInfo.getPrimaryKeySeq()-1, columnInfo);
            }
        }
        return cols;
    }    
    
    
    public void addForeignKeyInfo(ForeignKeyInfo keyInfo)
    {
        ArrayList<ForeignKeyInfo> keys = navigationKeys.get(keyInfo.getKeyName());
        if(keys == null) keys = new ArrayList<ForeignKeyInfo>(1);

        keys.ensureCapacity(keyInfo.getKeySeq());
        while(keys.size() < keyInfo.getKeySeq()) {
            keys.add(null);
        }
        keys.set(keyInfo.getKeySeq()-1, keyInfo);
        
        navigationKeys.put(keyInfo.getKeyName(), keys);
    }
    
    public Collection<ForeignKeyInfo> getForeignKeyInfo(String foreignKeyName)
    {
        return navigationKeys.get(foreignKeyName);
    }
    
    public Collection<String> getForeignKeyNames(String sourceColumnNamePattern)
    {
        if(sourceColumnNamePattern == null) {
            return navigationKeys.keySet();
        } else {
            ArrayList<String> foreignKeys = new ArrayList<String>();
            for(ArrayList<ForeignKeyInfo> fkKeys : navigationKeys.values()) {
                for(ForeignKeyInfo fkInfo : fkKeys) {
                    if(sourceColumnNamePattern.equals(fkInfo.getSourceColumn())) {
                        foreignKeys.add(fkInfo.getKeyName());
                    }
                }
            }
            return foreignKeys;
        }
    }
    
    
    public String getValue(ResultSet rs, ColumnInfo columnInfo) 
    {
        if(rs == null) return columnInfo.getDefaultValue();
        
        try {
            switch(columnInfo.getType()) {
                case java.sql.Types.SMALLINT:
                case java.sql.Types.INTEGER:
                    return String.valueOf(rs.getInt(columnInfo.getName()));
                case java.sql.Types.BIGINT:                    
                case java.sql.Types.NUMERIC:
                case java.sql.Types.DECIMAL:
                case java.sql.Types.FLOAT:                    
                    return String.valueOf(rs.getBigDecimal(columnInfo.getName()));
                case java.sql.Types.CHAR:
                case java.sql.Types.VARCHAR:
                case java.sql.Types.NCHAR:
                case java.sql.Types.NVARCHAR:
                    return rs.getString(columnInfo.getName());
                case java.sql.Types.DATE:
                    return rs.getDate(columnInfo.getName()).toString();
                case java.sql.Types.TIMESTAMP:
                    return rs.getTimestamp(columnInfo.getName()).toString();
                case java.sql.Types.TIME:
                    return rs.getTime(columnInfo.getName()).toString();
                default:
                    throw new Exception("Type " + columnInfo.getDDL() + " not supported for column " + columnInfo.getName());
            }
            
        } catch(Exception ex) {
            return "Error: " + ex.getMessage();
        }
        
    }
    

    /**
     * @return the name
     */
    public String getName() {
        String fullName = name;
        if(schema  != null) fullName = schema  + "." + fullName;
        if(catalog != null) fullName = catalog + "." + fullName;
        return fullName;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return the catalog
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * @param catalog the catalog to set
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    
}
