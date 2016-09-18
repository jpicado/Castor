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

CREATE TABLE advisedby (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_advisedby PRIMARY KEY (student,advisor)
);

CREATE TABLE tempAdvisedby (
   student VARCHAR(32),
   advisor VARCHAR(32),
   CONSTRAINT pk_tempAdvisedby PRIMARY KEY (student,advisor)
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

CREATE TABLE projectmember (
   project VARCHAR(32),
   member VARCHAR(32),
   CONSTRAINT pk_projectmember PRIMARY KEY (project,member)
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

CREATE TABLE publication (
   publication VARCHAR(32),
   author VARCHAR(32),
   CONSTRAINT pk_publication PRIMARY KEY (publication,author)
);

-- IF TABLE HAS PRIMARY KEY, AND INDEX IS AUTOMATICALLY CREATED
--CREATE INDEX advisedby_train_pos_PK ON advisedby_train_pos ( student,advisor );
--CREATE INDEX advisedby_train_pos_student ON advisedby_train_pos ( student );
--CREATE INDEX advisedby_train_pos_advisor ON advisedby_train_pos ( advisor );

--CREATE INDEX advisedby_train_neg_PK ON advisedby_train_neg ( student,advisor );
--CREATE INDEX advisedby_train_neg_student ON advisedby_train_neg ( student );
--CREATE INDEX advisedby_train_neg_advisor ON advisedby_train_neg ( advisor );

--CREATE INDEX advisedby_test_pos_PK ON advisedby_test_pos ( student,advisor );
--CREATE INDEX advisedby_test_pos_student ON advisedby_test_pos ( student );
--CREATE INDEX advisedby_test_pos_advisor ON advisedby_test_pos ( advisor );

--CREATE INDEX advisedby_test_neg_PK ON advisedby_test_neg ( student,advisor );
--CREATE INDEX advisedby_test_neg_student ON advisedby_test_neg ( student );
--CREATE INDEX advisedby_test_neg_advisor ON advisedby_test_neg ( advisor );

--CREATE INDEX advisedby_PK ON advisedby ( student,advisor );
--CREATE INDEX advisedby_student ON advisedby ( student );
--CREATE INDEX advisedby_advisor ON advisedby ( advisor );

--CREATE INDEX taughtby_PK ON taughtby ( course,professor,term );
--CREATE INDEX taughtby_course ON taughtby ( course );
--CREATE INDEX taughtby_professor ON taughtby ( professor );
--CREATE INDEX taughtby_term ON taughtby ( term );
