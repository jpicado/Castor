CASTOR USED IN ECLIPSE

To get logging output in command line, do the following in eclipse: Click on: Run -> Run Configurations -> [Classpath tab] -> click on user Entries -> Advanced -> Select Add Folder -> select the location of your log4j.properties file (src/resources folder).

Castor needs access to installation directory of VoltDB. Castor gets this directory through the environment variable VOLTDB_HOME. In eclipse, this variable should be set in Run -> Run Configurations -> [Environment tab].




CASTOR EXPORTED AS RUNNABLE JAR

Prerequisites:
1. Castor needs the voltdb jar file to compile store procedures. File name must be "voltdb.jar". File must be placed in same folder as Castor.jar.
2. Castor needs the log4j.properties file to get logging output. File name must be "log4j.properties". File must be placed in same folder as Castor.jar.
3. Castor needs access to installation directory of VoltDB through the VOLTDB_HOME environment variable.

To run Castor:
java -jar Castor.jar <user_input_file> <schema_file> <target_relation_name>
