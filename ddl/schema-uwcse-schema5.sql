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

CREATE TABLE course (
   course VARCHAR(32),
   professor VARCHAR(32),
   ta VARCHAR(32),
   term VARCHAR(32),
   level VARCHAR(32)
);

CREATE TABLE professor (
   professor VARCHAR(32),
   pos VARCHAR(32)
);

CREATE TABLE student (
   student VARCHAR(32),
   phase VARCHAR(32),
   years VARCHAR(32)
);

CREATE TABLE coauthorprofstud (
   publication VARCHAR(32),
   professor VARCHAR(32),
   student VARCHAR(32)
);

