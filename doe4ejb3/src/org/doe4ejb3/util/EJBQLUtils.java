/*
 * EJBQLUtils.java
 *
 * Created on 5 August 2006, 11:43
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.io.StringReader;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

import org.doe4ejb3.ejbql.*;
import org.doe4ejb3.gui.I18n;

/**
 *
 * @author jordi
 */
public class EJBQLUtils {
    
    /** Creates a new instance of EJBQLUtils */
    public static HashMap parseEJBQLParameterTypes(final String puName, String ejbql) throws Exception
    {
        StringReader reader = new StringReader(ejbql);
        EJBQLParameterTypesLexer lexer = new EJBQLParameterTypesLexer(reader);
        EJBQLParameterTypesParser parser = new EJBQLParameterTypesParser(lexer) {
            protected Class getClassFromEntityName(String entityName)
            {
                try { 
                   return JPAUtils.getClassFromEntityName(puName, entityName); 
                } catch(Exception ex) { 
                   System.out.println("Error while searching class for entity name '" + entityName + "'.");
                   ex.printStackTrace();
                   return null;
                }
            }
        };
        
        try {
            System.out.println("EJBUtils.parseEJBQLParameterTypes: PUName=" + puName + ", Query: " + ejbql);
            
            HashMap paramTypesByName = parser.parseEJBQLParameterTypes();
            HashMap entityClassesByAliasName = parser.getEntityClassesByAliasName();

            // generate traces:
            System.out.println("EJBUtils.parseEJBQLParameterTypes: Parameter types: " + paramTypesByName);
            System.out.println("EJBUtils.parseEJBQLParameterTypes: Entity classes: " + entityClassesByAliasName);

            return paramTypesByName;

        } catch(Exception ex) {
            System.out.println("EJBUtils.parseEJBQLParameterTypes: " + I18n.getLiteral("msg.error") + ex.getMessage());
        } finally {
            System.out.println("");
        }
        
        return null;
    }

    
}
