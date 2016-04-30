<h1>Domain Object Explorer for JPA / EJB3 entity beans</h1>
{|- border="0"
|[[image: icon.gif|left]] 
|The '''Domain Object Explorer''' is a desktop application that auto-generates the user interface for your JPA/EJB3 entity beans at runtime, without needing of UI programming (like [http://www.nakedobjects.org Naked Objects], [http://www.jmatter.org JMatter], ...). It can also be deployed as a JavaEE application client into '''Glassfish''' [http://glassfish.java.net/downloads/v2.1.1-final.html v2]/[http://glassfish.java.net/downloads/3.1-final.html v3] application servers, to centrally manage datasource configuration, update persistence units, and deliver the application to the users via Java Web Start technology.
|}

__TOC__

==Project Info==
* '''Author:''' Jordi Mariné Fort
* '''Latest Release:''' 0.3.1
* '''Latest Release Status:''' Alpha
* '''Project Type:''' Java EE application client
* '''License:''' Common Development and Distribution License (CDDL) - Version 1.0

==Features==
<h5>Pros:</h5>
* Rich user interface to manage and test your JPA/EJB3 entity beans.
* Application and persistence units updates can be delivered to users via Java Web Start technology.
* Data sources can be centrally configured from application server administrative console.
* Doesn't need complex HTML, JavaScript, nor AJAX handlers on server-side.
*...

<h5>Cons</h5>
* Deployed aplications are really big (about 45Mb from Glassfish v2, or 53Mb from Glassfish v3).
* Rude printing support.
* ...


==Download==
There is no stable version, yet. But you can download preview releases from [http://java.net/projects/doe/downloads Downloads section] or [http://java.net/projects/doe/sources/svn/show SVN], where you will find the following projects for NetBeans:<br/>

* '''doe4ejb3:''' it's the core library.
* '''doe4ejb3-app-client:''' it's a Java EE application client (that can be included in the users's enterprise application).
* '''doe4nb6-suite:''' simple integration module for NetBeans RCP 6.0 (only in "doe 0.2 source" package).
* '''doe4nb7-suite:''' simple integration module for NetBeans RCP 7.0 beta (only in "doe 0.3.1" source package).

There are also the following example modules:
* '''doe4ejb3-test-ejb3:''' sample EJB3 entity beans (some are based on  "<a href="http://www.trailsframework.org">trails</a>" demos).
* '''doe4ejb3-test-app:''': sample enterprise application project that includes the "doe" application client to manage the previous "EJB3" entities.


==Installation guide==
#Download, install and start '''Glassfish''' [http://glassfish.java.net/downloads/v2.1.1-final.html v2]/[http://glassfish.java.net/downloads/3.0.1-final.html v3] application server.
#If your application is not assembled in EAR format, you should create a new "Enterprise Application" module to deploy the "doe" application client with your JPA/EJB3 entity beans.
#Define the "doe4ejb3-app-client.jar" application as a java module in "application.xml" deployment descriptor, and include this library at the root level of the EAR archive, and the required libraries in the "/lib" folder (appframework-1.0.3.jar, swing-worker-1.1.jar,  swing-layout-1.0.4.jar, AbsoluteLayout.jar, beansbinding-1.2.1.jar, activation.jar, eclipselink-javax.persistence-2.0.jar, eclipselink-2.2.0.jar, jaxb-api.jar, jaxb-impl.jar, jaxb-xjc.jar,jaxb1-impl.jar, jsr173_api.jar, <a target="_blank" href="http://java.net/downloads/swingx/releases/1.6.2/swingx-1.6.2-bundle.zip">swingx-core-1.6.2.jar</a>, <a target="_blank" href="http://www.antlr2.org/download.html">antlr-2.7.6.jar</a>, <a target="_blank" href="https://abeille.dev.java.net/servlets/ProjectDocumentList?folderID=7802&expandFolder=7802&folderID=7803">Abeille</a>'s formsrt-2.1.0.jar)
#Define the entity class names in the "META-INF/persistence.xml" descriptor, and assemble the library into the "/lib" folder of the EAR archive.
#Define desired database connection pools and JDBC resources in Glassfih application server
#Deploy the EAR archive to Glassfish application server (with the "Enterprise Application" type, and the "Java Web Start" option enabled). <br/>Important note: it seems that Glassfish v3 doesn't support directory deployment of application clients, so don't deploy the applications from NetBeans (issue 11071).

Note: If you want to test "doe4ejb3-test-app" sample application, you only have to open the project with NetBeans to build EAR archive, and use the Glassfish administrative console to configure database connection parameters (with the JDBC resource's JNDI name as "jdbc/doe4ejb3"), and deploy the EAR application with "Java Web Start" option enabled.
<br/><br/>

==Usage Instructions==
The application can be launched from Glassfish administrative console (selecting the deployed EAR application, and following the "launch" link action of the "doe4ejb3-app-client.jar" module), or with the command <nowiki>"javaws http://localhost:8080/doe4ejb3-test-app/doe4ejb3-app-client"</nowiki> (supposing "doe4ejb3-test-app" is the EAR application name, and "doe4ejb3-app-client" is "doe" application client name, as it is in the sample application).<br/>
After installation, you can also launch it from Java Web Start viewer (command: "javaws -viewer").
<br/><br/>
The application has the following features:

=====Create a new entity object=====
:Open the "File" menu, select "New" option, the name of the persistence unit, and the class name of the EJB3 entity to be created. Then complete the entity properties and press the "Accept" or "Save" button to persist the entity.

=====Search entity beans=====
:On the left panel, select the persistence unit to view its EJB3 entity classes, and double click with left button on the desired class to open the entity manager window on the desktop. Then select the query type (to search "All" objects, or use a custom/named query). With "custom" or "named" queries, you also have to complete the required parameters. Finally, press the "Search" button, and wait for the results (also note the "done" indicator in the status bar).

=====Browse/edit an object=====
:Select one of the objects found, and click the "Edit" button from the manager window to view the entity details on the right panel (desktop). You can navigate to related entities, and modify its properties (to persist changes, remember to press the "Save" or "Accept" button on the modified entity details).

=====Print the objects=====
:Use the "Print" button from the manager/detail window, configure the printing dialog, and click the "Print" button to confirm.

=====Delete an object=====
:Select the undesired objects, click "Delete" button from the manager/detail window, and confirm the deletion in the next window popup.


==Screenshots==
{|- border="0"
|Standalone:<br/>
<a href="http://java.net/attachments/wiki_images/doe/screenshot.png">[[image: screenshot.png|320x240px]]</a>
|Integrated with NetBeans Platform:<br/>
<a href="http://java.net/attachments/wiki_images/doe/screenshot-nb6.png">[[image: screenshot-nb6.png|320x240px]]</a>
|}


==Developer's guide==
=====Defining custom queries for an entity=====
The "doe" application can search entities using the "named queries" defined for an EJB3 entity bean.
<pre>
@Entity
@NamedQueries({
    @NamedQuery(name="searchPublished",
	query="SELECT OBJECT(r) FROM Recipe r WHERE r.published = true"),
    @NamedQuery(name="searchByCategory",
        query="SELECT OBJECT(r) FROM Recipe r WHERE r.category = :category")
})
public class Recipe implements java.io.Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;

    @Column(name="TITLE", nullable=false, unique=true, length=100)
    public String title;

    @Column(name="PUBLISHED", nullable=true)
    public boolean published;

    @ManyToOne
    public Category category;
    ...
}
</pre>

=====Defining custom actions for an entity=====
When "doe" application opens an existing entity, it will display additional buttons for each public method annotated with "@org.jdesktop.application.Action" attribute (jsr-296). The default presentation properties of actions defined this way are initialized automatically from their class's ResourceBundle. Developers can use <a href="https://doe.dev.java.net/source/browse/*checkout*/doe/doe4ejb3/src/org/doe4ejb3/util/DOEUtils.java">org.doe4ejb3.util.DOEUtils</a> and <a href="https://doe.dev.java.net/source/browse/*checkout*/doe/doe4ejb3/src/org/doe4ejb3/gui/WindowManager.java">org.doe4ejb3.gui.WindowManager</a> for user interaction. For example:
<pre>
@Entity
public class Recipe implements java.io.Serializable
{
    ....
    @Action
    public void cookIt()
    {
        DOEUtils.getWindowManager().showMessageDialog(
	    "Ready to eat.", 
	    "CookIt result", 
	    JOptionPane.INFORMATION_MESSAGE);
    }
    ....
}
</pre>

=====Customize user interface for an entity=====
The "doe" application can auto-generate the UI at runtime, but it's also possible to customize entity editors in many ways:
<ol>
<li><u>Using @org.doe4ejb3.annotation.PropertyDescriptor annotation:</u></li>
The <a href="https://doe.dev.java.net/source/browse/*checkout*/doe/doe4ejb3/src/org/doe4ejb3/annotation/PropertyDescriptor.java">@PropertyDescriptor</a> annotation allows simple customization of the UI controls for the properties (to change labels, the order of fields, UI control size, ...). It's also possible to use custom UI control types (that must implement the "<a href="https://doe.dev.java.net/source/browse/*checkout*/doe/doe4ejb3/src/org/doe4ejb3/gui/PropertyEditorInterface.java">org.doe4ejb3.gui.PropertyEditorInterface</a>"). For example:
<pre>
...
@Lob
@Column(name="PHOTO", columnDefinition="BLOB")
@PropertyDescriptor(index=7, displayName="Photo", width=300, height=175,
                    editorClassName="org.doe4ejb3.gui.ImagePropertyEditor")
public byte[] photo;
...
</pre>
<br/>

<li><u>Using Abeille forms:</u></li>
You can design the complete UI of an entity editor with <a href="http://abeille.dev.java.net">Abeille Forms Designer</a>, and copy the generated xml or jfrm file in the same directory/name of the entity class. To bind entity properties with the UI, developers have to design the forms with the apropiate control types (or JPanel container), and to name them like the respective properties names. When the control belongs to an embbedded entity property, use the dot symbol to separate the embedded entity name of the associated property name.  
<br/><br/>

<li><u>Using compiled Swing UI:</u></li>
You can also use your favourite GUI builder (like NetBeans IDE) to design the complete Swing UI layout for an entity editor. The class representing the UI should be inherited from JPanel, the UI class name must be specified in the entity class with the <a href="https://doe.dev.java.net/source/browse/*checkout*/doe/doe4ejb3/src/org/doe4ejb3/annotation/EntityDescriptor.java">@org.doe4ejb3.annotation.EntityDescriptor</a>(layoutClassName="uiClassName") annotation, and the UI class must be packaged in the same persistence unit "jar" as the entity class. To bind entity properties with the UI, developers have to design the forms with the apropiate control types (or JPanel container), and publish them as public fields with a similar name in the form class. When the control belongs to an embbedded entity property, use the "_" symbol to separate the embedded entity name of the associated property name. For example:
<br/><br/>

<u>Entity class:</u>
<pre>
...
@Entity
@EntityDescriptor(layoutClassName="myNS.PersonEditor")
public class Person implements Serializable 
{
    @Id
    @Column(name="ssn", length=20)
    public String ssn;

    @Column(name="name", length=150)
    public String name;

    @Column(name="bornDay")
    public java.sql.Date bornDay;
    ...
}
</pre>

<u>UI code:</u>
<pre>
package myNS; 
...
public class PersonEditor extends javax.swing.JPanel 
{
    public JTextField jTextField_ssn;
    public JTextField jTextField_name;
    public JPanel     jPanel_bornDay;

    /** Creates new form PersonEditor */
    public PersonEditor() {
        initComponents();
    }

    private void initComponents() {
        // generated UI code
        jTextField_ssn = new JTextField();
        jTextField_name = new JTextField();
        jPanel_bornDay = new JPanel();
        ...
    }

}
</pre>

</ol>
<br/>

==Support and participation==
I hope the "doe" project will be a community supported open-source product, and you could find support  from the project's [http://java.net/projects/doe/forums forums].

Any volunteers and suggestions?
<br/>

==Roadmap==
Future roadmap for the "doe" application ("to do" task list):
*English corrections and better i18n support (but I don't speak English ;-).
*Improve "GUI" (menus, toolbars, icons, better navigation, better custom queries support, fix some copy&paste operations, more drag&drop operations, ...).
*Allow user extensions/plugins to add new actions/menus for entity objects.
*Create custom editors for common property types (image viewers/loaders, dates, etc.)
*Better printing support.
*GUI API for customized actions in EJB3 entity beans.
*Load/unload of persistence units.
*More sample applications.
*NetBeans Platform module improvements.


==Revision History==
* Version 0.3.1 - [Development version - SVN head]
** Integration with Glassfish v3.1
** Updates for NetBeans RCP 7.0 (beta 2)
** Date picking with calendar (SwingX component)

* Version 0.3 - [2010/03/03 - Alpha version - SVN tag: DOE-0_3_PREVIEW4NB69]
**Integration with Glassfish v3
** Updates for NetBeans 6.8
** EclipseLink JPA support
** Some bug fixes.

*Version 0.2 - [2009/03/29 - Alpha version - SVN tag: DOE-0_2_PREVIEW4NB65]
**Improved UI.
**Cut, copy & paste operations.
**Drag & drop operations.
**Basic printing support.
**Custom queries in search panel.
**Custom actions in entity editor.
**Custom layout in entity editor (compiled Swing UI, or Abeille forms).
**Allows client authentication with databases.
**API migration to jsr-295 (beans binding) and jsr-296 (swing application framework).
**Updates for NetBeans 6.x and Glassfish v2.
**Suport for other JPA providers (like Hibernate and OpenJPA). 
**Many bug fixes.

*Version 0.1 - [2006/09/27 - Pre-Alpha version - SVN tag: start]
**Open source release to java.net.
**Simple search/view/edition of EJB3 entity objects.
**Integration with NetBeans 5, Glassfish v2 previews, and TopLink Essentials.

----
<small>
<a href="http://java-enterprise.dev.java.net/">[[image: foundry-small.gif]]</a>
<i>Part of the <a href="http://java-enterprise.dev.java.net/">Java Enterprise Community</a>.</i>
</small>


