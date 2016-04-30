package net.java.doe;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ForeignKeyInfo 
{
    private String keyName;
    private int    keySeq;
    private String sourceTable;
    private String sourceColumn;
    private String targetTable;
    private String targetColumn;
    
    public ForeignKeyInfo(ResultSet rs) throws SQLException
    {
        sourceTable = rs.getString(7);
        sourceColumn = rs.getString(8);
        
        targetTable = rs.getString(3);
        targetColumn = rs.getString(4);

        keySeq = rs.getInt(9);
        
        keyName = rs.getString(12);
        if(keyName == null) keyName = "FK_" + sourceTable + "_" + targetTable;
    }    
    
    public String getKeyInfo()
    {
        return getKeyName() + "(" + getKeySeq() + "): " + getSourceTable() + "." + getSourceColumn() + "-->" + getTargetTable() + "." + getTargetColumn();
    }

    /**
     * @return the keyName
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * @param keyName the keyName to set
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    /**
     * @return the keySeq
     */
    public int getKeySeq() {
        return keySeq;
    }

    /**
     * @param keySeq the keySeq to set
     */
    public void setKeySeq(int keySeq) {
        this.keySeq = keySeq;
    }

    /**
     * @return the sourceTable
     */
    public String getSourceTable() {
        return sourceTable;
    }

    /**
     * @param sourceTable the sourceTable to set
     */
    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    /**
     * @return the sourceColumn
     */
    public String getSourceColumn() {
        return sourceColumn;
    }

    /**
     * @param sourceColumn the sourceColumn to set
     */
    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    /**
     * @return the targetTable
     */
    public String getTargetTable() {
        return targetTable;
    }

    /**
     * @param targetTable the targetTable to set
     */
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    /**
     * @return the targetColumn
     */
    public String getTargetColumn() {
        return targetColumn;
    }

    /**
     * @param targetColumn the targetColumn to set
     */
    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }
    
}
