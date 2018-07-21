# Castor: A Relational Learning System
**_v0.1_**

The Castor relational learning system is described in the paper [Schema Independent Relational Learning](http://josepicado.com/papers/Castor_SIGMOD2017.pdf) (SIGMOD 2017). 


## Installation

### Install VoltDB
Currently, Castor only works on top of the in-memory RDBMS VoltDB.

1. Download VoltDB: https://github.com/VoltDB/voltdb
2. Go to download directory and compile:
```
ant
```

### Set environment variables
1. Set VOLTDB_HOME environment variable to installation directory of VoltDB.
2. Add $VOLTDB_HOME/bin to PATH environment variable.

### Compile Castor
1. Compile Castor by running:
```
ant
```
It will generate a dist/ folder, containing Castor.jar file and the dependencies in the lib/ folder.


## Start database


## Run Castor


## Notes
- Castor is memory intensive.
- Castor has only been tested in macOS and Linux (Red Hat).
