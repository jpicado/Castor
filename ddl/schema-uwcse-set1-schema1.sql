CREATE TABLE advisedby_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_ai_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_ai_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_ai_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_ai_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_ai_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_ai_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_ai_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_ai_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_graphics_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_graphics_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_graphics_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_graphics_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_graphics_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_graphics_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_graphics_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_graphics_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_language_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_language_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_language_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_language_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_language_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_language_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_language_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_language_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_systems_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_systems_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_systems_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_systems_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_systems_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_systems_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_systems_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_systems_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_theory_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_theory_train_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_theory_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_theory_train_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_theory_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_theory_test_pos PRIMARY KEY (student,advisor)
);

CREATE TABLE advisedby_theory_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby_theory_test_neg PRIMARY KEY (student,advisor)
);

CREATE TABLE taughtby (
   course VARCHAR(32),
   professor VARCHAR(32),
   term VARCHAR(32),
   CONSTRAINT pk_taughtby PRIMARY KEY (course,professor,term)
);

CREATE TABLE courselevel (
   course VARCHAR(32),
   level VARCHAR(32),
   CONSTRAINT pk_courselevel PRIMARY KEY (course)   
);

CREATE TABLE hasposition (
   professor VARCHAR(32),
   pos VARCHAR(32),
   CONSTRAINT pk_hasposition PRIMARY KEY (professor)
);

CREATE TABLE inphase (
   student VARCHAR(32),
   phase VARCHAR(32),
   CONSTRAINT pk_inphase PRIMARY KEY (student)
);

CREATE TABLE yearsinprogram (
   student VARCHAR(32),
   years VARCHAR(32),
   CONSTRAINT pk_yearsinprogram PRIMARY KEY (student)
);

CREATE TABLE ta (
   course VARCHAR(32),
   student VARCHAR(32),
   term VARCHAR(32),
   CONSTRAINT pk_ta PRIMARY KEY (course,student,term)
);

CREATE TABLE professor (
   professor VARCHAR(32),
   CONSTRAINT pk_professor PRIMARY KEY (professor)
);

CREATE TABLE student (
   student VARCHAR(32),
   CONSTRAINT pk_student PRIMARY KEY (student)
);

CREATE TABLE publicationprofessor (
   publication VARCHAR(32),
   author VARCHAR(32),
   CONSTRAINT pk_publicationprofessor PRIMARY KEY (publication,author)
);

CREATE TABLE publicationstudent (
   publication VARCHAR(32),
   author VARCHAR(32),
   CONSTRAINT pk_publicationstudent PRIMARY KEY (publication,author)
);

CREATE TABLE publication (
   publication VARCHAR(32),
   author VARCHAR(32),
   CONSTRAINT pk_publication PRIMARY KEY (publication,author)
);
