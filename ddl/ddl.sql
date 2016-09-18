CREATE TABLE father_train_pos (
   father VARCHAR(64),
   child VARCHAR(64)
);

CREATE TABLE father_train_neg (
   father VARCHAR(64),
   child VARCHAR(64)
);



CREATE TABLE aunt (
   aunt VARCHAR(64),
   sibkid VARCHAR(64)
);

CREATE TABLE brother (
   brother VARCHAR(64),
   siblilng VARCHAR(64)
);

CREATE TABLE daughter (
   daughter VARCHAR(64),
   parent VARCHAR(64)
);

CREATE TABLE father (
   father VARCHAR(64),
   child VARCHAR(64)
);

CREATE TABLE husband (
   husband VARCHAR(64),
   wife VARCHAR(64)
);

CREATE TABLE mother (
   mother VARCHAR(64),
   child VARCHAR(64)
);

CREATE TABLE nephew (
   nephew VARCHAR(64),
   pibling VARCHAR(64)
);

CREATE TABLE niece (
   niece VARCHAR(64),
   pibling VARCHAR(64)
);

CREATE TABLE sister (
   sister VARCHAR(64),
   sibling VARCHAR(64)
);

CREATE TABLE son (
   son VARCHAR(64),
   parent VARCHAR(64)
);

CREATE TABLE uncle (
   uncle VARCHAR(64),
   sibkid VARCHAR(64)
);

CREATE TABLE wife (
   wife VARCHAR(64),
   husband VARCHAR(64)
);





-- CREATE PROCEDURE FROM CLASS kinship.KinshipTestProcedure1;
-- CREATE PROCEDURE FROM CLASS kinship.KinshipTestProcedure2;
-- CREATE PROCEDURE FROM CLASS kinship.KinshipTestProcedureClear1;
-- CREATE PROCEDURE FROM CLASS kinship.KinshipTestProcedureClear2;   





CREATE TABLE father_neg (
   father VARCHAR(64),
   child VARCHAR(64)
);
