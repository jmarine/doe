package net.java.doe;

import java.sql.Connection;


public class TextEditor implements Editor
{
    @Override
    public String edit(ColumnInfo info, String value, boolean disabled) 
    {
        if(info != null && "YES".equals(info.getGenerated())) disabled = true;
        
        StringBuffer sb = new StringBuffer(100);
        if(info.getSize() < 100) {
            if(!disabled) sb.append("<input type=\"text\" name=\"" + info.getName() + "\" value=\"");
            if(value != null) sb.append(value.replace("\"", "&#34;"));
            if(!disabled) sb.append("\"></input>");
        } else {
            if(!disabled) sb.append("<textarea name=\"" + info.getName() + "\">");
            if(value != null) sb.append(value.replace("\"", "&#34;"));            
            if(!disabled) sb.append("</textarea>");
        }
        return sb.toString();
    }
    
}
