CREATE TABLE advisedby_train_pos (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE advisedby_train_neg (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE advisedby_test_pos (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE advisedby_test_neg (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE advisedby (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE tempAdvisedby (
   student VARCHAR(32),
   advisor VARCHAR(32)
);

CREATE TABLE taughtby (
   course VARCHAR(32),
   professor VARCHAR(32),
   term VARCHAR(32)
);

CREATE TABLE courselevel (
   course VARCHAR(32),
   level VARCHAR(32)
);

CREATE TABLE hasposition (
   professor VARCHAR(32),
   pos VARCHAR(32)
);

CREATE TABLE projectmember (
   project VARCHAR(32),
   member VARCHAR(32)
);

CREATE TABLE inphase (
   student VARCHAR(32),
   phase VARCHAR(32)
);

CREATE TABLE yearsinprogram (
   student VARCHAR(32),
   years VARCHAR(32)
);

CREATE TABLE ta (
   course VARCHAR(32),
   student VARCHAR(32),
   term VARCHAR(32)
);

CREATE TABLE professor (
   professor VARCHAR(32)
);

CREATE TABLE student (
   student VARCHAR(32)
);

CREATE TABLE publication (
   publication VARCHAR(32),
   author VARCHAR(32)
);
