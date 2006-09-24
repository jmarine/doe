/*
 * ApplicationException.java
 *
 * Created on July 24, 2005, 7:49 PM
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.exception;

/**
 *
 * @author Jordi Marine Fort
 */
public class ApplicationException extends Exception {
    
    /** Creates a new instance of ApplicationException */
    public ApplicationException(String msg) 
    {
        super(msg);
    }

    public ApplicationException(String msg, Throwable cause) 
    {
        super(msg, cause);
    }

}
