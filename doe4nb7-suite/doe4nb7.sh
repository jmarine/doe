export JAVA_HOME=/usr/lib/jvm/java-6-sun
export NETBEANS_HOME=/opt/netbeans70b2

java  -Duser.language=ca -Duser.country=ES  -Djdk.home=$JAVA_HOME -classpath $NETBEANS_HOME/platform/lib/boot.jar:$NETBEANS_HOME/platform/lib/org-openide-modules.jar:$NETBEANS_HOME/platform/lib/org-openide-util.jar:$NETBEANS_HOME/platform/lib/org-openide-util-lookup.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar -Dnetbeans.dirs=/home/jordi/Desktop/projects/doe/doe4nb7-suite/build/cluster:$NETBEANS_HOME/ide -Dnetbeans.home=$NETBEANS_HOME/platform -Dnetbeans.logger.console=true -ea org.netbeans.Main --userdir /home/jordi/Desktop/projects/doe/doe4nb7-suite/build/testuserdir --branding doe4nb7_suite
