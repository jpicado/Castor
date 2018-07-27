# Castor: A Relational Learning System
**_v0.1_**

The Castor relational learning system is described in the paper [Schema Independent Relational Learning](http://josepicado.com/papers/Castor_SIGMOD2017.pdf) (SIGMOD 2017). 


## Installation

### Install VoltDB
Currently, Castor only works on top of the in-memory RDBMS VoltDB.

1. Download and install [VoltDB Community Edition](https://github.com/VoltDB/voltdb). Instructions available [here](https://github.com/VoltDB/voltdb/wiki/Building-VoltDB).

### Set environment variables
1. Set `VOLTDB_HOME` environment variable to installation directory of VoltDB.
2. Add `$VOLTDB_HOME/bin` to `PATH` environment variable.

### Compile Castor
1. Compile Castor by running:
```
ant
```
It will generate a `dist` folder, containing Castor.jar file and the dependencies in the `lib` folder.


## Start database
1. Create a VoltDB database and insert data. For an example, see `examples` folder.


## Run Castor
1. Run Castor's JAR file. There are two options to set training examples. 
- Option 1: Training examples are stored in CSV files. These files are specified by arguments: `posTrainExamplesFile, negTrainExamplesFile, posTestExamplesFile, negTestExamplesFile`.
```
java -jar Castor.jar -dataModel <data_model_file> -parameters <parameters_file> -posTrainExamplesFile <pos_train_examples_file> -negTrainExamplesFile <neg_train_examples_file> -posTestExamplesFile <pos_test_examples_file> -negTestExamplesFile <neg_test_examples_file>
```
- Option 2: Training examples are stored in tables in the database. Castor assumes that the names of these tables are the name of the target relation followed by a suffix. The name of the target relation is extracted from the `headMode` in the dataModel file. The suffixes are specified by arguments: `trainPosSuffix, trainNegSuffix, testPosSuffix, testNegSuffix`.
```
java -jar Castor.jar -dataModel <data_model_file> -parameters <parameters_file> -trainPosSuffix <train_pos_suffix> -trainNegSuffix <train_neg_suffix> -testPosSuffix <test_pos_suffix> -testNegSuffix <test_neg_suffix>
```

### Castor command line arguments
- **dataModel** &lt;data_model_file&gt; (required): JSON file containing mode declarations (language bias). See an example [here](https://github.com/jpicado/Castor/examples/uwcse/castor-input/dataModel.json). A short explanation on mode declarations can be found in Section 3 of [this paper](https://arxiv.org/abs/1710.01420).
- **parameters** &lt;parameters_file&gt; (required): JSON file containing parameters (explained below).
- **inds** &lt;inds_file&gt;: JSON file containing inclusion dependencies. See an example [here](https://github.com/jpicado/Castor/examples/uwcse/castor-input/inds.json).
- **trainPosSuffix** &lt;train_pos_suffix&gt;: Suffix of table containing positive training examples.
- **trainNegSuffix** &lt;train_neg_suffix&gt;: Suffix of table containing negative training examples.
- **testPosSuffix** &lt;test_pos_suffix&gt;: Suffix of table containing positive testing examples.
- **testNegSuffix** &lt;test_neg_suffix&gt;: Suffix of table containing negative testing examples.
- **posTrainExamplesFile** &lt;pos_train_examples_file&gt;: CSV file containing positive training examples.
- **negTrainExamplesFile** &lt;neg_train_examples_file&gt;: CSV file containing negative training examples.
- **posTestExamplesFile** &lt;pos_test_examples_file&gt;: CSV file containing positive testing examples.
- **negTestExamplesFile** &lt;neg_test_examples_file&gt;: CSV file containing negative testing examples.
- **test**: Test learned definition using testing examples.
- **outputSQL**: Output learned definition in SQL format.
- **sat**: Only build bottom-clause for example specified by argument `e`.
- **groundSat**: Only build ground bottom-clause for example specified by argument `e`.
- **e**: Index of example to build (ground) bottom-clause when `sat` or `groundSat` arguments are specified (default: 0).

### Castor parameters
These parameters are specified inside the file pointed by the `parameters` argument.
- **dbURL**: VoltDB server URL. (default: "localhost")
- **port**: VoltDB client port. (default: 21212)
- **iterations**: Number of iterations in bottom-clause construction algorithm. Equivalent to maximum depth of variables in a bottom-clause.
- **minprec**: Minimum precision that a clause must satisfy to be included in the learned definition (computed based on uncovered positive examples). In other words, how precise each clause should be. (default: 0.5)
- **minrec**: Minimum recall that a clause must satisfy to be included in the learned definition (computed based on all positive examples). In other words, the minimum percentage of positive examples that a clause should cover. (default: 0).
- **minPos**: Minimum number of positive examples that a clause must cover to be included in the learned definition. (default: 2)
- **sample**: Number of examples to use when generalizing a clause using ARMG. (default: 1)
- **beam**: Number of candidate clauses to keep. (default: 1)
- **recall**: Maximum number of literals added to a bottom-clause for each application of a mode declaration. (default: 10)
- **groundRecall**: Maximum number of literals added to a ground bottom-clause for each application of a mode declaration. Ground bottom-clauses are used to evaluate coverage using theta-subsumption. If this parameter is restricted, result of coverage is approximate. (default: Integer.Max_VALUE)
- **threads**: Number of threads; used to parallelize coverage operations. (default: 1)
- **randomSeed**: Random seed. (default: 1)
- **createStoredProcedure**: Create stored procedures that run bottom-clause construction algorithm. (default: true)
- **useStoredProcedure**: Use stored procedures to run bottom-clause construction algorithm. (default: true)


### Castor assumptions
Castor makes the following assumptions. We may remove some of these assumptions in the future.
- The schema contains unique relation names.
- All attributes in schema are strings.
- Only one attribute is input (+) in mode declarations (language bias).



## Notes
- Castor is under development.
- Castor has only been tested in macOS and Linux (Red Hat).
- Castor is memory intensive. If you get OutOfMemoryError, increase Java heap size.


## Citation
If you use Castor, please cite the paper [Schema Independent Relational Learning](http://josepicado.com/papers/Castor_SIGMOD2017.pdf) (SIGMOD 2017) ([ACM Digital Library](https://dl.acm.org/citation.cfm?id=3035923)):
```
@inproceedings{Picado2017SchemaIR,
  title={Schema Independent Relational Learning},
  author={Jose Picado and Arash Termehchy and Alan Fern and Parisa Ataei},
  booktitle={SIGMOD Conference},
  year={2017}
}
```
