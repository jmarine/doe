/*
 * EJBQLParameterTypesTest.java
 * JUnit based test
 *
 * Created on 7 September 2006, 19:40
 */

package org.doe4ejb3.ejbql;

import java.io.StringReader;
import java.util.HashMap;

import junit.framework.*;


/**
 *
 * @author jordi
 */
public class EJBQLParameterTypesTest extends TestCase {
    
    public EJBQLParameterTypesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }


    /**
     * Test of getParamTypesByName method, of class org.doe4ejb3.ejbql.EJBQLParameterTypes.
     */
    public void testParseSomeEJBQLParameterTypes() {
        try {
            parseEJBQLParameterTypes("SELECT DISTINCT OBJECT(c) FROM Ciudad c WHERE c.provincia = :provincia");
            parseEJBQLParameterTypes("SELECT DISTINCT OBJECT(c) FROM Ciudad c JOIN c.provincia p WHERE p = :provincia");
            parseEJBQLParameterTypes("SELECT DISTINCT OBJECT(c) FROM Ciudad c WHERE c.provincia.id = :provinciaId");
        } catch(Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }
   
    protected HashMap parseEJBQLParameterTypes(String ejbql) throws Exception
    {
        System.out.println("parseEJBQLParameterTypes: " + ejbql);

        StringReader reader = new StringReader(ejbql);
        EJBQLParameterTypesLexer lexer = new EJBQLParameterTypesLexer(reader);
        EJBQLParameterTypesParser parser = new EJBQLParameterTypesParser(lexer) {
            protected Class getClassFromEntityName(String entityName)
            {
                try { 
                    return Class.forName(entityName);
                } catch(Exception ex) { 
                   System.out.println("Error while searching class for entity name '" + entityName + "'.");
                   ex.printStackTrace();
                   return null;
                }
            }
            
        };

        HashMap paramTypesByName = parser.parseEJBQLParameterTypes();
        HashMap entityClassesByAliasName = parser.getEntityClassesByAliasName();

        // Print the results
        System.out.println("Parameter types: " + paramTypesByName);
        System.out.println("Entity classes: " + entityClassesByAliasName);
        System.out.println("");
        
        return paramTypesByName;
    }
    
}
