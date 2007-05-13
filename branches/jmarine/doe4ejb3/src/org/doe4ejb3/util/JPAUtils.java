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

    private static JAXBContext jc = null;
    private static Hashtable puNameByEntity = new Hashtable();
    private static Hashtable<String, Class> classesByPUNameAndEntityName = new Hashtable<String, Class>();
    private static Hashtable entityManagerFactoryByPUName = new Hashtable();
    
    /**
     * Creates a new instance of JPAUtils
     */
    private JPAUtils() { }

    
    public static Class getClassFromEntityName(String puName, String entityName)
    {
        String key = puName + "/" + entityName;
        return classesByPUNameAndEntityName.get(key);
    }
    
    public static Collection<Class> getPersistentEntities() throws Exception
    {
        try {
            System.out.println("Searching persistence providers");
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
                System.out.println("Parsing PU: " + pu.getName());
                
                try {
                   excludeUnlistedClasses = pu.isExcludeUnlistedClasses();
                   if(!excludeUnlistedClasses) defaultPUName = pu.getName();
                } catch(Exception ex) {
                    System.out.println("> WARN: excludeUnlistedClasses not defined: ERROR: " + ex.getMessage());
                }

                for( String className : pu.getClazz() ) {
                    if(!puNameByEntity.containsKey(className)) {
                        System.out.println("> found class: " + className);
                        Class clazz = loader.loadClass(className);
                        Entity entity = (Entity)clazz.getAnnotation(javax.persistence.Entity.class);
                        if(entity != null) {
                            puNameByEntity.put(className, pu.getName());
                            
                            String entityName = entity.name();
                            if( (entityName == null) || (entityName.length() == 0) ) entityName = clazz.getSimpleName();
                            classesByPUNameAndEntityName.put(pu.getName() + "/" + entityName, clazz);
                        }
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
                if(!puNameByEntity.containsKey(className)) {
                    System.out.println("> found class: " + className);
                    puNameByEntity.put(className, defaultPUName);
                    
                    String entityName = entity.name();
                    if( (entityName == null) || (entityName.length() == 0) ) entityName = clazz.getSimpleName();
                    classesByPUNameAndEntityName.put(defaultPUName + "/" + entityName, clazz);
                }
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

    
    public static String getPersistentUnitNameForEntity(Class entityClass)
    {
        return (String)puNameByEntity.get(entityClass.getName());
    }

    public static EntityManager getEntityManager(Class entityClass)
    {
        String puName = getPersistentUnitNameForEntity(entityClass);
        return getEntityManager(puName);
    }
    
    public static EntityManager getEntityManager(String puName)
    {    
        EntityManagerFactory emf = (EntityManagerFactory)entityManagerFactoryByPUName.get(puName);
        if(emf == null) {
            emf = javax.persistence.Persistence.createEntityManagerFactory(puName);
            entityManagerFactoryByPUName.put(puName, emf);
        }
        
        // FIXME: the EntityManager should also be pooled? (thread safe & high creation time)
        EntityManager manager = emf.createEntityManager(); // Retrieve an application managed entity manager
        
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
    
    public static List findAllEntities(Class entityClass)
    {
        String entityName = getEntityName(entityClass);
        EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(entityClass);
            
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

    
    public static List executeNamedQuery(Class entityClass, String queryName, HashMap paramValues) 
    {
        String puName = JPAUtils.getPersistentUnitNameForEntity(entityClass);
        EntityManager manager = JPAUtils.getEntityManager(puName);
        
        javax.persistence.Query query = manager.createNamedQuery(queryName);
        for(Object paramName : paramValues.keySet()) {
           query.setParameter((String)paramName, paramValues.get(paramName));
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
    
    
    public static Object createNewEntity(Object entity)
    {
        if(entity != null) {
            Class entityClass = entity.getClass();
            String entityName = getEntityName(entityClass);

            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(entityClass);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(entity);
            transaction.commit();            
            manager.close();
           
            System.out.println("PersistenceProviders: Entity created: " + entity.toString());
        }
        
        return entity;
    }


    public static Object saveEntity(Object entity)
    {
        if(entity != null) {
            Class entityClass = entity.getClass();
            String entityName = getEntityName(entityClass);

            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(entityClass);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.merge(entity);
            transaction.commit();
            manager.close();
            
            System.out.println("PersistenceProviders: Entity saved: " + entity.toString());
        }
        
        return entity;
    }

    
    public static void removeEntity(Object entity)
    {
        if(entity != null) {
            Class entityClass = entity.getClass();
            String entityName = getEntityName(entityClass);

            EntityManager manager = org.doe4ejb3.util.JPAUtils.getEntityManager(entityClass);
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