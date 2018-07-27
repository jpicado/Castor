#!/bin/bash

# Run Castor
java -jar ../../dist/Castor.jar -parameters castor-input/parameters.json -inds castor-input/inds.json -dataModel castor-input/datamodel.json -trainPosSuffix "_FOLD1_TRAIN_POS" -trainNegSuffix "_FOLD1_TRAIN_NEG" -testPosSuffix "_FOLD1_TEST_POS" -testNegSuffix "_FOLD1_TEST_NEG" -test
