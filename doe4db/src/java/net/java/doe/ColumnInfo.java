package net.java.doe;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class ColumnInfo
{
    private String name;
    private int type;           // java.sql.Types
    private String typeName;  
    private int size;
    private int decimals;
    private int nullable;
    private String defaultValue;
    private int pos;
    private String autoincrement;
    private String generated;
    private int primaryKeySeq;
    

    public ColumnInfo(ResultSet rs) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        name = rs.getString(4);
        type = rs.getInt(5);
        typeName = rs.getString(6);
        size = rs.getInt(7);
        decimals = rs.getInt(9);
        nullable = rs.getInt(11);
        defaultValue = rs.getString(13);
        pos = rs.getInt(17);
        autoincrement = (rsmd.getColumnCount() >= 23) ? rs.getString(23) : "";
        generated = (rsmd.getColumnCount() >= 24) ? rs.getString(24) : "";
        primaryKeySeq = 0;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    
    public boolean isPrimaryKey()
    {
        return (primaryKeySeq > 0);
    }
    
    
    /**
     * @return the primaryKeySeq
     */
    public int getPrimaryKeySeq() {
        return primaryKeySeq;
    }

    /**
     * @param primaryKeySeq the primaryKeySeq to set
     */
    public void setPrimaryKeySeq(int primaryKeySeq) {
        this.primaryKeySeq = primaryKeySeq;
    }
    
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
    public String getDDL()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(typeName);
        switch(type) {
            case java.sql.Types.BINARY:
            case java.sql.Types.CHAR:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
            case java.sql.Types.INTEGER:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.FLOAT:
            case java.sql.Types.NUMERIC:                
            case java.sql.Types.VARBINARY:
            case java.sql.Types.VARCHAR:
                sb.append('(');
                sb.append(size);
                if(decimals > 0) {
                    sb.append(',');
                    sb.append(decimals);
                }
                sb.append(')');
        }
        
        if(nullable == DatabaseMetaData.columnNoNulls) {
            sb.append(" NOT NULL");
        }
        
        if(defaultValue != null) {
            sb.append(" DEFAULT ");
            sb.append(defaultValue);
        }
        
        return sb.toString();
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the decimals
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * @param decimals the decimals to set
     */
    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    /**
     * @return the nullable
     */
    public int getNullable() {
        return nullable;
    }

    /**
     * @param nullable the nullable to set
     */
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * @return the generated
     */
    public String getGenerated() {
        return generated;
    }

    /**
     * @param generated the generated to set
     */
    public void setGenerated(String generated) {
        this.generated = generated;
    }

    /**
     * @return the autoincrement
     */
    public String getAutoincrement() {
        return autoincrement;
    }

    /**
     * @param autoincrement the autoincrement to set
     */
    public void setAutoincrement(String autoincrement) {
        this.autoincrement = autoincrement;
    }
}