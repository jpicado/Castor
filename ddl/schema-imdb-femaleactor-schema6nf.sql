
CREATE TABLE femaleactor_test_neg (
actor VARCHAR(8)
);
CREATE TABLE femaleactor_test_pos (
actor VARCHAR(8)
);
CREATE TABLE femaleactor_train_neg (
actor VARCHAR(8)
);
CREATE TABLE femaleactor_train_pos (
actor VARCHAR(8)
);
CREATE TABLE actors_gender (
actid VARCHAR(8),
name VARCHAR(250)
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
CREATE TABLE directors (
directorid VARCHAR(8),
name VARCHAR(250)
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
year VARCHAR(100)
);
CREATE TABLE movies2actors (
movieid VARCHAR(8),
actorid VARCHAR(8),
as_character VARCHAR(1000)
);
CREATE TABLE movies2directors (
movieid VARCHAR(8),
directorid VARCHAR(8)
);
CREATE TABLE movies2producers (
movieid VARCHAR(8),
producerid VARCHAR(8)
);
CREATE TABLE movies2writers (
movieid VARCHAR(8),
writerid VARCHAR(8)
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
CREATE TABLE ratings (
movieid VARCHAR(8),
rank VARCHAR(4),
votes VARCHAR(8)
);
CREATE TABLE runningtimes (
movieid VARCHAR(8),
time VARCHAR(50)
);
CREATE TABLE writers (
writerid VARCHAR(8),
name VARCHAR(250)
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