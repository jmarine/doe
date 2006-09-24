#!/bin/bash
export CLASSPATH=:/opt/antlr276/antlr.jar:.
export CLASSPATH=$CLASSPATH:/opt/glassfish/lib/toplink-essentials.jar  

#Generate EJBQL parser to get parameter types:
cd src/org/doe4ejb3/ejbql
/opt/java5/bin/java -classpath /opt/antlr276/antlr/antlr.jar antlr.Tool EJBQLParameterTypes.g
cd ../../..

#Compile:
#/opt/java5/bin/javac org/doe4ejb3/ejbql/*.java
#/opt/java5/bin/javac *.java

#Test:
#/opt/antlr276/scripts/java.sh TestEJBQLParameterTypes < query.in
