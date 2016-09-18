CREATE TABLE dramadirectors_fold1_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold1_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold1_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold1_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold2_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold2_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold2_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold2_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold3_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold3_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold3_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold3_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold4_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold4_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold4_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold4_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold5_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold5_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold5_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold5_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold6_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold6_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold6_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold6_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold7_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold7_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold7_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold7_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold8_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold8_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold8_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold8_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold9_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold9_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold9_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold9_train_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold10_test_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold10_test_pos (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold10_train_neg (
directorid varchar(8)
);
CREATE TABLE dramadirectors_fold10_train_pos (
directorid varchar(8)
);
CREATE TABLE actors (
actid VARCHAR(8),
name VARCHAR(250),
sex VARCHAR(1)
);
CREATE TABLE altversions (
movieid VARCHAR(8),
version VARCHAR(64)
);
CREATE TABLE business (
movieid VARCHAR(8),
business VARCHAR(64)
);
CREATE TABLE certificates (
movieid VARCHAR(8),
country VARCHAR(50),
certification VARCHAR(255)
);
CREATE TABLE colorinfo (
colorid VARCHAR(8),
color VARCHAR(100)
);
CREATE TABLE countries (
countryid VARCHAR(8),
country VARCHAR(50)
);
CREATE TABLE directors (
directorid VARCHAR(8),
name VARCHAR(250)
);
CREATE TABLE genres (
genreid VARCHAR(8),
genre VARCHAR(50)
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
prodcompanyid VARCHAR(8),
prodcompany VARCHAR(250)
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
CREATE TABLE akanames (
name VARCHAR(250),
akaname VARCHAR(250)
);
CREATE TABLE akatitles (
movieid VARCHAR(8),
language VARCHAR(3),
title VARCHAR(400)
);
CREATE TABLE biographies (
bioid varchar(8),
name varchar(250),
biotext varchar(64)
);
CREATE TABLE cinematgrs (
cinematid varchar(8),
name varchar(250)
);
CREATE TABLE composers (
composerid varchar(8),
name varchar(250)
);
CREATE TABLE costdesigners (
costdesid varchar(8),
name varchar(250)
);
CREATE TABLE distributors (
movieid varchar(8),
name varchar(250)
);
CREATE TABLE editors (
editorid varchar(8),
name varchar(250)
);
CREATE TABLE language (
languageid varchar(8),
language varchar(100)
);
CREATE TABLE misc (
miscid varchar(8),
name varchar(250)
);
CREATE TABLE movies2cinematgrs (
movieid varchar(8),
cinematid varchar(8)
);
CREATE TABLE movies2composers (
movieid varchar(8),
composerid varchar(8)
);
CREATE TABLE movies2costdes (
movieid varchar(8),
costdesid varchar(8)
);
CREATE TABLE movies2editors (
movieid varchar(8),
editorid varchar(8)
);
CREATE TABLE movies2misc (
movieid varchar(8),
miscid varchar(8)
);
CREATE TABLE movies2proddes (
movieid varchar(8),
proddesid varchar(8)
);
CREATE TABLE mpaaratings (
movieid varchar(8),
reasontext varchar(64)
);
CREATE TABLE proddesigners (
proddesid varchar(8),
name varchar(250)
);
CREATE TABLE releasedates (
movieid varchar(8),
countryid varchar(8),
releasedates varchar(150)
);
CREATE TABLE technical (
movieid varchar(8),
name varchar(250)
);
CREATE TABLE movies2colors (
movieid varchar(8),
colorid varchar(8)
);
CREATE TABLE movies2countries (
movieid varchar(8),
countryid varchar(8)
);
CREATE TABLE movies2genres (
movieid varchar(8),
genreid varchar(8)
);
CREATE TABLE movies2languages (
movieid varchar(8),
languageid varchar(8)
);
CREATE TABLE movies2prodcompanies (
movieid varchar(8),
prodcompanyid varchar(8)
);

CREATE INDEX index_actors_actid ON actors ( actid );
CREATE INDEX index_altversions_movieid ON altversions ( movieid );
CREATE INDEX index_business_movieid ON business ( movieid );
CREATE INDEX index_certificates_movieid ON certificates ( movieid );
CREATE INDEX index_colorinfo_colorid ON colorinfo ( colorid );
CREATE INDEX index_countries_countryid ON countries ( countryid );
CREATE INDEX index_directors_directorid ON directors ( directorid );
CREATE INDEX index_genres_genreid ON genres ( genreid );
CREATE INDEX index_movies_movieid ON movies ( movieid );
CREATE INDEX index_movies2actors_movieid ON movies2actors ( movieid );
CREATE INDEX index_movies2actors_actorid ON movies2actors ( actorid );
CREATE INDEX index_movies2directors_movieid ON movies2directors ( movieid );
CREATE INDEX index_movies2directors_directorid ON movies2directors ( directorid );
CREATE INDEX index_movies2producers_movieid ON movies2producers ( movieid );
CREATE INDEX index_movies2producers_producerid ON movies2producers ( producerid );
CREATE INDEX index_movies2writers_movieid ON movies2writers ( movieid );
CREATE INDEX index_movies2writers_writerid ON movies2writers ( writerid );
CREATE INDEX index_plots_movieid ON plots ( movieid );
CREATE INDEX index_prodcompanies_prodcompanyid ON prodcompanies ( prodcompanyid );
CREATE INDEX index_producers_producerid ON producers ( producerid );
CREATE INDEX index_ratings_movieid ON ratings ( movieid );
CREATE INDEX index_runningtimes_movieid ON runningtimes ( movieid );
CREATE INDEX index_writers_writerid ON writers ( writerid );
CREATE INDEX index_akatitles_movieid ON akatitles ( movieid );
CREATE INDEX index_biographies_bioid ON biographies ( bioid );
CREATE INDEX index_cinematgrs_cinematid ON cinematgrs ( cinematid );
CREATE INDEX index_composers_composerid ON composers ( composerid );
CREATE INDEX index_costdesigners_costdesid ON costdesigners ( costdesid );
CREATE INDEX index_distributors_movieid ON distributors ( movieid );
CREATE INDEX index_editors_editorid ON editors ( editorid );
CREATE INDEX index_language_languageid ON language ( languageid );
CREATE INDEX index_misc_miscid ON misc ( miscid );
CREATE INDEX index_movies2cinematgrs_movieid ON movies2cinematgrs ( movieid );
CREATE INDEX index_movies2composers_movieid ON movies2composers ( movieid );
CREATE INDEX index_movies2costdes_movieid ON movies2costdes ( movieid );
CREATE INDEX index_movies2editors_movieid ON movies2editors ( movieid );
CREATE INDEX index_movies2misc_movieid ON movies2misc ( movieid );
CREATE INDEX index_movies2proddes_movieid ON movies2proddes ( movieid );
CREATE INDEX index_mpaaratings_movieid ON mpaaratings ( movieid );
CREATE INDEX index_proddesigners_proddesid ON proddesigners ( proddesid );
CREATE INDEX index_releasedates_movieid ON releasedates ( movieid );
CREATE INDEX index_technical_movieid ON technical ( movieid );
CREATE INDEX index_movies2colors_movieid ON movies2colors ( movieid );
CREATE INDEX index_movies2countries_movieid ON movies2countries ( movieid );
CREATE INDEX index_movies2genres_movieid ON movies2genres ( movieid );
CREATE INDEX index_movies2languages_movieid ON movies2languages ( movieid );
CREATE INDEX index_movies2prodcompanies_movieid ON movies2prodcompanies ( movieid );
