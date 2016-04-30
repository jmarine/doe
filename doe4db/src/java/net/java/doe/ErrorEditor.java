package net.java.doe;

import java.sql.Connection;


public class ErrorEditor implements Editor
{
    private Exception ex;
    
    public ErrorEditor(Exception ex)
    {
        this.ex = ex;
    }
    
    @Override
    public String edit(ColumnInfo info, String value, boolean disabled) 
    {
        StringBuffer sb = new StringBuffer(100);
        sb.append("Error: ");
        sb.append(ex.getClass().getName());
        sb.append(": ");
        sb.append(ex.getMessage());
        return sb.toString();
    }
}
