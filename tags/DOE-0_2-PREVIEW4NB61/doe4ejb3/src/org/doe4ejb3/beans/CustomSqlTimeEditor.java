/*
 * CustomSqlTimeEditor.java
 *
 * Created on 28 October 2006, 19:18
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.beans;


public class CustomSqlTimeEditor extends CustomDateEditor
{
    public CustomSqlTimeEditor() 
    {
        super(javax.persistence.TemporalType.TIME);
    }
    
}