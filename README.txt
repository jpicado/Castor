
COMPILE AND CREATE JAR FILE

Open Terminal to current directory, type:
ant



RUN

cd into dist
java -jar Castor.jar -parameters <parameters file> -schema <schema file> -dataModel <data model file>




CASTOR USED IN ECLIPSE

To get logging output in command line, do the following in eclipse: Click on: Run -> Run Configurations -> [Classpath tab] -> click on user Entries -> Advanced -> Select Add Folder -> select the location of your log4j.properties file (Castor folder).

Castor needs access to installation directory of VoltDB. Castor gets this directory through the environment variable VOLTDB_HOME. In eclipse, this variable should be set in Run -> Run Configurations -> [Environment tab].
