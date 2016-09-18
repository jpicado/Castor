#!/usr/bin/env python

from __future__ import print_function

inputFileName = "schema_webkb_cornell.sql"
outputFileName = "schema_webkb_cornell_with_PKs.sql"

inputFile = open(inputFileName, "r")
outputFile = open(outputFileName, "w")

attributes = ""
table = ""

for line in inputFile:
	
	if line.startswith("CREATE TABLE"):
		# Get table name
		tokens = line.split(" ")
		table = tokens[2]
	elif line.startswith(");"):
		# Add constraint to table definition
		constraint = ", CONSTRAINT pk_" + table + " PRIMARY KEY (" + attributes + ")\n"
		outputFile.write(constraint)
		
		# Initialize
		attributes = ""
		table = ""
	else:
		# Get attribute name and add to current attributes
		tokens = line.split(" ")
		attribute = tokens[0]
		
		if attributes:
			attributes += ","
		attributes += attribute
		
	outputFile.write(line)



