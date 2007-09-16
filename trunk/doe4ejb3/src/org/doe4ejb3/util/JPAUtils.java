/*
 * JPAUtils.java
 *
 * Created on 5 August 2006, 11:43
 * @author Jordi Marine Fort
 */

package org.doe4ejb3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.doe4ejb3.jaxb.persistence.Persistence;


public class JPAUtils 
{
    public static final String GENERIC_USER_PROPERTY_NAME       = "username";
    public static final String GENERIC_PASSWORD_PROPERTY_NAME   = "password";
    
    public static final String TOPLINK_PROVIDER    = "oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider";
    public static final String HIBERNATE_PROVIDER  = "org.hibernate.ejb.HibernatePersistence";
    public static final String OPENJPA_PROVIDER    = "org.apache.openjpa.persistence.PersistenceProviderImpl";
    public static final String KODO_PROVIDER       = "kodo.persistence. PersistenceProviderImpl";

    private static JAXBContext jc = null;
    private static String defaultPersistenceProvider = TOPLINK_PROVIDER;
    private static Hashtable<String, String> providerByPU = new Hashtable<String, String>();
    private static Hashtable<String, ArrayList<Class>> entityListByPU = new Hashtable<String, ArrayList<Class>>();
    private static Hashtable<String, Class> classesByPUNameAndEntityName = new Hashtable<String, Class>();
    
    private static HashMap<String,String> connectionParams = new HashMap<String,String>();

    /**
     * Creates a new instance of JPAUtils
     */
    private JPAUtils() { }


    public static void setConnectionParams(String username, String password) 
    {
        if( (username != null) && (username.length() > 0) ) {
            connectionParams.put(JPAUtils.GENERIC_USER_PROPERTY_NAME, username);
            if( (password != null) && (password.length() > 0) ) connectionParams.put(JPAUtils.GENERIC_PASSWORD_PROPERTY_NAME, password);
            else connectionParams.remove(JPAUtils.GENERIC_PASSWORD_PROPERTY_NAME);
        } else {
            connectionParams.remove(JPAUtils.GENERIC_USER_PROPERTY_NAME);
            connectionParams.remove(JPAUtils.GENERIC_PASSWORD_PROPERTY_NAME);
        }
    }

    
    /**
     * Resolve entity class names from EJBQL sentences
     * (used in EJBQLUtils.parseEJBQLParameterTypes)
     */
    public static Class getClassFromEntityName(String puName, String entityName)
    {
        String key = puName + "/" + entityName;
        return classesByPUNameAndEntityName.get(key);
    }
    
    public static String getPersistenceProvider(String puName)
    {
        String provider = providerByPU.get(puName);
        if((provider == null) || (provider.length() == 0)) provider = getDefaultPersistenceProvider();
        return provider;
    }
    
    public static String getDefaultPersistenceProvider()
    {
        return defaultPersistenceProvider;
    }
    
    public static void setDefaultPersistenceProvider(String provider)
    {
        defaultPersistenceProvider = provider;
    }    
    
    private static String getUserPropertyName(String providerClass) 
    {
        if(providerClass != null) {
            if(providerClass.equalsIgnoreCase(TOPLINK_PROVIDER)) {
                return oracle.toplink.essentials.config.TopLinkProperties.JDBC_USER;
            } else if(providerClass.equalsIgnoreCase(HIBERNATE_PROVIDER)) {
                return "hibernate.connection.username";
            } else if(providerClass.equalsIgnoreCase(OPENJPA_PROVIDER)) {
                return "openjpa.ConnectionUserName";
            } else if(providerClass.equalsIgnoreCase(KODO_PROVIDER)) {
                return "kodo.ConnectionUserName";  // FIXME? "kodo.Connection2UserName";
            }
        }
        return oracle.toplink.essentials.config.TopLinkProperties.JDBC_USER;
    }

    private static String getPasswordPropertyName(String providerClass) 
    {
        if(providerClass != null) {
            if(providerClass.equalsIgnoreCase(TOPLINK_PROVIDER)) {
                return oracle.toplink.essentials.config.TopLinkProperties.JDBC_PASSWORD;
            } else if(providerClass.equalsIgnoreCase(HIBERNATE_PROVIDER)) {
                return "hibernate.connection.password";
            } else if(providerClass.equalsIgnoreCase(OPENJPA_PROVIDER)) {
                return "openjpa.ConnectionPassword";
            } else if(providerClass.equalsIgnoreCase(KODO_PROVIDER)) {
                return "kodo.ConnectionPassword"; // FIXME? "kodo.Connection2Password";
            }
        }
        return oracle.toplink.essentials.config.TopLinkProperties.JDBC_PASSWORD;
    }    

    public static String getPersistenceUnitTitle(String puName)
    {
        if((puName == null) || (puName.length() == 0)) puName = "Default PU";
        return puName;
    }
    
    public static Collection<String> getPersistenceUnits() throws Exception
    {
        if(entityListByPU.size() == 0) loadPersistentEntities();
        return entityListByPU.keySet();
    }
    
    public static Collection<Class> getPersistentEntities(String puName) throws Exception
    {
        return (Collection<Class>)entityListByPU.get(puName).clone();
    }

    
    private static Collection<Class> loadPersistentEntities() throws Exception
    {
        try {
            // GLASSFISHv2 Beta
            // System.out.println("JPAUtils.getPersistentEntities: Setting up application's classloader for GFv2 beta2...");
            // Thread.currentThread().setContextClassLoader(JPAUtils.class.getClassLoader());  

            System.out.println("JPAUtils.getPersistentEntities: Searching persistence providers");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            java.util.Enumeration<URL> iter = classLoader.getResources("META-INF/persistence.xml");
            while(iter.hasMoreElements()) {
                URL url = (URL)iter.nextElement();
                System.out.println("Looking for entity classes in: " + url.toString());
                scanPersistentEntitiesFromPU(classLoader, url);
            }
            System.out.println("END of search");
            
        } catch(Exception ex) {
            throw new Exception("ERROR searching persistence providers", ex);
        }
        
        return classesByPUNameAndEntityName.values();
    }
    
    
    protected static void scanPersistentEntitiesFromPU(ClassLoader loader, URL puURL) throws Exception
    {
        // FIXME? only one call to JAXBContext.newInstance("org.doe4ejb3.jaxb.persistence");
        
        String  defaultPUName = "";
        boolean excludeUnlistedClasses = true;
        
        InputStream inputStream = null;
        
        try {
            
            System.out.println("Parsing : " + puURL.toString() );

            // create a JAXBContext capable of handling classes generated into "org.doe4ejb3.jaxb.persistence" package.
            if(jc == null) jc = JAXBContext.newInstance( "org.doe4ejb3.jaxb.persistence" );

            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();

            // JAXBElement<?> poElement = (JAXBElement<?>)u.unmarshal( inputStream );
            // org.doe4ejb3.jaxb.Persistence persistence = (org.doe4ejb3.jaxb.Persistence)poElement.getValue();
            inputStream = puURL.openStream();
            org.doe4ejb3.jaxb.persistence.Persistence persistence = (org.doe4ejb3.jaxb.persistence.Persistence)u.unmarshal( inputStream );


            // examine content of persistence units
            for(org.doe4ejb3.jaxb.persistence.Persistence.PersistenceUnit pu : persistence.getPersistenceUnit() ) 
            {
                String puName = pu.getName();
                if(puName == null) puName = "";

                System.out.println("Parsing PU: " + puName);

                try {
                    providerByPU.put(puName, pu.getProvider());
                } catch(Exception ex) {
                    providerByPU.put(puName, getDefaultPersistenceProvider());
                }
                
                try {
                   excludeUnlistedClasses = pu.isExcludeUnlistedClasses();
                   if(!excludeUnlistedClasses) defaultPUName = pu.getName();
                } catch(Exception ex) {
                    System.out.println("> WARN: excludeUnlistedClasses not defined: ERROR: " + ex.getMessage());
                }

                for( String className : pu.getClazz() ) {
                        Class clazz = loader.loadClass(className);
                        Entity entity = (Entity)clazz.getAnnotation(javax.persistence.Entity.class);
                        if(entity != null) {
                            System.out.println("> found entity class: " + className);
                            
                            String entityName = entity.name();
                            if( (entityName == null) || (entityName.length() == 0) ) entityName = clazz.getSimpleName();
                            classesByPUNameAndEntityName.put(puName + "/" + entityName, clazz);
                            
                            ArrayList<Class> entityList = entityListByPU.get(puName);
                            if(entityList == null) entityList = new ArrayList<Class>();
                            entityList.add(clazz);
                            entityListByPU.put(puName, entityList);
                        }
                }

                for ( String jarName : pu.getJarFile() ) {
                    // TODO: getPersistentEntitiesFromJarFile(...);
                }
            }

        } catch( Exception ex ) {
            System.out.println("Error loading persistenceUnit: " + puURL.toString() );
            ex.printStackTrace();
        } finally {
            if(inputStream != null) {
                try { inputStream.close(); inputStream = null; }
                catch(Exception ex) { }
            }
        }

        if(!excludeUnlistedClasses) {
            URI puURI = puURL.toURI();  // baseDirectory/META-INF/persistence.xml
            File puFile = new File(puURI);
            File baseDirectory = puFile.getParentFile().getParentFile();
            
            Collection<Class> entityClasses = getPersistentEntitiesFromDirectory(loader, baseDirectory, "");
            for(Class clazz : entityClasses) {
                    Entity entity = (Entity)clazz.getAnnotation(javax.persistence.Entity.class);
                    String className = clazz.getName();

                    System.out.println("> found class: " + className);
                    
                    String entityName = entity.name();
                    if( (entityName == null) || (entityName.length() == 0) ) entityName = clazz.getSimpleName();
                    classesByPUNameAndEntityName.put(defaultPUName + "/" + entityName, clazz);
                    
                    ArrayList<Class> entityList = entityListByPU.get(defaultPUName);
                    if(entityList == null) entityList = new ArrayList<Class>();
                    entityList.add(clazz);
                    entityListByPU.put(defaultPUName, entityList);

            }
        }
        
    }
    
    protected static Collection<Class> getPersistentEntitiesFromDirectory(ClassLoader loader, File directory, String prefix) throws Exception
    {
        Collection<Class> classes = new ArrayList<Class>();
        File files[] = directory.listFiles();
        for(File file : files) {
            if(file.isDirectory()) {
                String newPrefix = prefix + file.getName() + ".";
                classes.addAll(getPersistentEntitiesFromDirectory(loader, file, newPrefix));
            } else if(file.getName().endsWith(".class")) {
                String className = prefix + file.getName();
                int pos = className.lastIndexOf(".");
                className = className.substring(0, pos);
                Class clazz = loader.loadClass(className);
                if(clazz.isAnnotationPresent(Entity.class)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
    
    
    public static EntityManager getEntityManager(String puName)
    {    
        // TODO: the EntityManagerFactory should be pooled and create redefined EntityManager with specific user/password?
        String providerClass = getPersistenceProvider(puName);
        HashMap<String,String> providerConnectionParams = new HashMap<String,String>();
        String username = (String)connectionParams.get(GENERIC_USER_PROPERTY_NAME);
        if(username != null) {
            String userPropertyName = getUserPropertyName(providerClass);
            providerConnectionParams.put(userPropertyName, username);
        }
        String password = (String)connectionParams.get(GENERIC_PASSWORD_PROPERTY_NAME);
        if(password != null) {
            String passwordPropertyName = getPasswordPropertyName(providerClass);
            providerConnectionParams.put(passwordPropertyName, password);
        }
        
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory(puName, providerConnectionParams);
        EntityManager manager = emf.createEntityManager(providerConnectionParams); 
        
        return manager;
    }
    
    public static String getEntityName(Class entityClass)
    {
        String entityName = entityClass.getSimpleName();
        try {
            Entity entityAnnotation = (Entity)entityClass.getAnnotation(Entity.class);
            if( (entityAnnotation != null) && (entityAnnotation.name() != null) && (entityAnnotation.name().length() > 0) ) {
                entityName = entityAnnotation.name();
            }
        } catch(Exception ex) {
            System.out.println("PersistenceProviders: ERROR getting name attribute for Entity annotation in class: " + entityClass.getName());
        }
        return entityName;
    }
    
    public static List findAllEntities(String puName, Class entityClass)
    {
        String entityName = getEntityName(entityClass);
        EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(puName);
            
        System.out.println("Querying for all " + entityName);
        Query query = manager.createQuery("SELECT OBJECT(e) FROM " + entityName + " e");
        List entities = query.getResultList();
        
        // FIXME: this is only for debug purpose (it will only be done in FINER trace level):
        for(Object obj : entities) {
            System.out.println("> found " + entityName + ": " + obj.toString());
        }
        
        manager.close();
        System.out.println("PersistenceProviders: search done.");
        
        return entities;
    }


    public static List executeQuery(String puName, String ejbql, HashMap paramValues) 
    {
        EntityManager manager = JPAUtils.getEntityManager(puName);
        
        javax.persistence.Query query = manager.createQuery(ejbql);
        if(paramValues != null) {
            for(Object paramName : paramValues.keySet()) {
                query.setParameter((String)paramName, paramValues.get(paramName));
            }
        }
        
        List entities = query.getResultList();

        // FIXME: this is only for debug purpose (it will only be done in FINER trace level):
        for(Object obj : entities) {
            System.out.println("> found: " + obj.toString());
        }

        manager.close();
        System.out.println("PersistenceProviders: search done.");
        
        return entities;
    }   

        
    public static List executeNamedQuery(String puName, String namedQueryName, HashMap paramValues) 
    {
        EntityManager manager = JPAUtils.getEntityManager(puName);
        
        javax.persistence.Query query = manager.createNamedQuery(namedQueryName);
        if(paramValues != null) {
            for(Object paramName : paramValues.keySet()) {
                query.setParameter((String)paramName, paramValues.get(paramName));
            }
        }
        
        List entities = query.getResultList();

        // FIXME: this is only for debug purpose (it will only be done in FINER trace level):
        for(Object obj : entities) {
            System.out.println("> found: " + obj.toString());
        }

        manager.close();
        System.out.println("PersistenceProviders: search done.");
        
        return entities;
    }   
    
    
    public static Object createNewEntity(String puName, Object entity)
    {
        if(entity != null) {
            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(puName);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(entity);
            transaction.commit();            
            manager.close();
           
            System.out.println("PersistenceProviders: Entity created: " + entity.toString());
        }
        
        return entity;
    }


    public static Object saveEntity(String puName, Object entity)
    {
        if(entity != null) {
            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(puName);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            entity = manager.merge(entity);
            transaction.commit();
            manager.close();
            
            System.out.println("PersistenceProviders: Entity saved: " + entity.toString());
        }
        
        return entity;
    }

    
    public static void removeEntity(String puName, Object entity)
    {
        if(entity != null) {
            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(puName);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            entity = manager.merge(entity);
            manager.remove(entity);
            transaction.commit();
            manager.close();
            
            System.out.println("PersistenceProviders: Entity removed: " + entity.toString());
        }
    }
}
