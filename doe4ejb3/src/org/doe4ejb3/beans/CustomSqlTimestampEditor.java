/*
 * CustomSqlTimestampEditor.java
 *
 * Created on 28 October 2006, 19:18
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.beans;


public class CustomSqlTimestampEditor extends CustomDateEditor
{
    public CustomSqlTimestampEditor() 
    {
        super(javax.persistence.TemporalType.TIMESTAMP);
    }
    
}