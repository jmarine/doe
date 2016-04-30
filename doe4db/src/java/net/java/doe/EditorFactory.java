
package net.java.doe;

public class EditorFactory 
{
    public static Editor getEditor(ColumnInfo info)
    {
        try {
            switch(info.getType())
            {
                case java.sql.Types.BINARY:
                case java.sql.Types.CHAR:
                case java.sql.Types.NCHAR:
                case java.sql.Types.NVARCHAR:
                case java.sql.Types.INTEGER:
                case java.sql.Types.SMALLINT:
                case java.sql.Types.BIGINT:
                case java.sql.Types.NUMERIC:                
                case java.sql.Types.DECIMAL:
                case java.sql.Types.FLOAT:
                case java.sql.Types.VARBINARY:
                case java.sql.Types.VARCHAR:
                case java.sql.Types.TIME:
                case java.sql.Types.TIMESTAMP:
                case java.sql.Types.DATE:
                    return new TextEditor();
                default:
                    return (Editor)Class.forName("net.java.doe." + info.getTypeName() + "Editor.class").newInstance();
            }
            
        } catch(Exception ex) {
            return new ErrorEditor(ex);
        }
    }
    
}
