package net.java.doe;

import java.sql.Connection;


public interface Editor 
{
    String edit(ColumnInfo info, String value, boolean disabled);
    
}
