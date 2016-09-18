CREATE TABLE director2writer_test_neg (
directorname VARCHAR(250),
writername VARCHAR(250)
);
CREATE TABLE director2writer_test_pos (
directorname VARCHAR(250),
writername VARCHAR(250)
);
CREATE TABLE director2writer_train_neg (
directorname VARCHAR(250),
writername VARCHAR(250)
);
CREATE TABLE director2writer_train_pos (
directorname VARCHAR(250),
writername VARCHAR(250)
);
CREATE TABLE actors (
actid VARCHAR(8),
name VARCHAR(250),
sex VARCHAR(1)
);
CREATE TABLE altversions (
movie VARCHAR(8),
version VARCHAR(64)
);
CREATE TABLE business (
movie VARCHAR(8),
business VARCHAR(64)
);
CREATE TABLE certificates (
movieid VARCHAR(8),
country VARCHAR(50),
certification VARCHAR(255)
);
CREATE TABLE colorinfo (
movieid VARCHAR(8),
color VARCHAR(100)
);
CREATE TABLE countries (
movieid VARCHAR(8),
country VARCHAR(50)
);
CREATE TABLE genres (
movieid VARCHAR(8),
genre VARCHAR(50)
);
CREATE TABLE keywords (
movieid VARCHAR(8),
keyword VARCHAR(125)
);
CREATE TABLE movies (
movieid VARCHAR(8),
title VARCHAR(400),
year VARCHAR(100),
rank VARCHAR(4),
votes VARCHAR(8),
directorid VARCHAR(8),
directorname VARCHAR(250),
writerid VARCHAR(8),
writername VARCHAR(250)
);
CREATE TABLE movies2actors (
movieid VARCHAR(8),
actorid VARCHAR(8),
as_character VARCHAR(1000)
);
CREATE TABLE movies2producers (
movieid VARCHAR(8),
producerid VARCHAR(8)
);
CREATE TABLE plots (
movieid VARCHAR(8),
plot VARCHAR(64)
);
CREATE TABLE prodcompanies (
movieid VARCHAR(8),
name VARCHAR(250)
);
CREATE TABLE producers (
producerid VARCHAR(8),
name VARCHAR(250)
);
CREATE TABLE runningtimes (
movieid VARCHAR(8),
time VARCHAR(50)
);
CREATE TABLE akanames(
name VARCHAR(250),
akaname VARCHAR(250)
);
CREATE TABLE akatitles(
movieid VARCHAR(8),
language VARCHAR(3),
title VARCHAR(400)
);
CREATE TABLE biographies(
bioid varchar(8),
name varchar(250),
biotext varchar(64)
);
CREATE TABLE cinematgrs(
cinematid varchar(8),
name varchar(250)
);
CREATE TABLE composers(
composerid varchar(8),
name varchar(250)
);
CREATE TABLE costdesigners(
costdesid varchar(8),
name varchar(250)
);
CREATE TABLE distributors(
movieid varchar(8),
name varchar(250)
);
CREATE TABLE editors(
editorid varchar(8),
name varchar(250)
);
CREATE TABLE language(
movieid varchar(8),
language varchar(100)
);
CREATE TABLE misc(
miscid varchar(8),
name varchar(250)
);
CREATE TABLE movies2cinematgrs(
movieid varchar(8),
cinematid varchar(8)
);
CREATE TABLE movies2composers(
movieid varchar(8),
composerid varchar(8)
);
CREATE TABLE movies2costdes(
movieid varchar(8),
costdesid varchar(8)
);
CREATE TABLE movies2editors(
movieid varchar(8),
editorid varchar(8)
);
CREATE TABLE movies2misc(
movieid varchar(8),
miscid varchar(8)
);
CREATE TABLE movies2proddes(
movieid varchar(8),
proddesid varchar(8)
);
CREATE TABLE mpaaratings(
movieid varchar(8),
reasontext varchar(64)
);
CREATE TABLE proddesigners(
proddesid varchar(8),
name varchar(250)
);
CREATE TABLE releasedates(
movieid varchar(8),
country varchar(50),
releasedates varchar(150)
);
CREATE TABLE technical(
movieid varchar(8),
name varchar(250)
);
