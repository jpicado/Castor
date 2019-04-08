#!/bin/bash

PORT=10101

# create schema
sqlcmd --port=$PORT < ddl-schema.sql
sqlcmd --port=$PORT < ddl-indexes.sql

# load database

csvloader --port $PORT --separator "," --skip 1 --file large_data_set/source.csv source -r ./log
csvloader --port $PORT --separator "," --skip 1 --file large_data_set/cot_event.csv cot_event -r ./log
csvloader --port $PORT --separator "," --skip 1 --file large_data_set/cot_event_position.csv cot_event_position -r ./log
