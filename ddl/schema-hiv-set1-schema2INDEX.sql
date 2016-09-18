CREATE TABLE hiv_active_train_pos (
  compound VARCHAR(16)
);
CREATE TABLE hiv_active_train_neg (
  compound VARCHAR(16)
);
CREATE TABLE hiv_active_test_pos (
  compound VARCHAR(16)
);
CREATE TABLE hiv_active_test_neg (
  compound VARCHAR(16)
);

CREATE TABLE compounds (
  compound varchar(16),
  atom varchar(16)
);
CREATE TABLE atoms (
atom VARCHAR(16),
element_C VARCHAR(8),
element_O VARCHAR(8),
element_N VARCHAR(8),
element_H VARCHAR(8),
element_S VARCHAR(8),
element_F VARCHAR(8),
element_Cl VARCHAR(8),
element_P VARCHAR(8),
element_Br VARCHAR(8),
element_I VARCHAR(8),
element_Na VARCHAR(8),
element_Se VARCHAR(8),
element_Sn VARCHAR(8),
element_Cu VARCHAR(8),
element_Si VARCHAR(8),
element_As VARCHAR(8),
element_B VARCHAR(8),
element_Ge VARCHAR(8),
element_Ac VARCHAR(8),
element_Mg VARCHAR(8),
element_Rh VARCHAR(8),
element_Fe VARCHAR(8),
element_Ru VARCHAR(8),
element_Co VARCHAR(8),
element_Bi VARCHAR(8),
element_Pd VARCHAR(8),
element_Ni VARCHAR(8),
element_Sb VARCHAR(8),
element_Pt VARCHAR(8),
element_Ir VARCHAR(8),
element_Re VARCHAR(8),
element_Mn VARCHAR(8),
element_W VARCHAR(8),
element_Mo VARCHAR(8),
element_Gd VARCHAR(8),
element_Tl VARCHAR(8),
element_Zn VARCHAR(8),
element_Hg VARCHAR(8),
element_Ho VARCHAR(8),
element_Pb VARCHAR(8),
element_Cr VARCHAR(8),
element_Ag VARCHAR(8),
element_Ga VARCHAR(8),
element_Au VARCHAR(8),
element_Li VARCHAR(8),
element_K VARCHAR(8),
element_Cs VARCHAR(8),
element_Nb VARCHAR(8),
element_V VARCHAR(8),
element_Nd VARCHAR(8),
element_Al VARCHAR(8),
element_Zr VARCHAR(8),
element_Te VARCHAR(8),
element_Ti VARCHAR(8),
element_Cd VARCHAR(8),
element_Yb VARCHAR(8),
element_Ca VARCHAR(8),
element_U VARCHAR(8),
element_Er VARCHAR(8),
element_Pr VARCHAR(8),
element_Sm VARCHAR(8),
element_Os VARCHAR(8),
element_Tb VARCHAR(8),
p2_0 VARCHAR(8),
p2_1 VARCHAR(8),
p2_2 VARCHAR(8),
p2_3 VARCHAR(8),
p2_4 VARCHAR(8),
p2_5 VARCHAR(8),
p2_6 VARCHAR(8),
p2_7 VARCHAR(8),
p2_8 VARCHAR(8),
p2_9 VARCHAR(8),
p2_10 VARCHAR(8),
p3 VARCHAR(8));

CREATE TABLE bonds ( 
  bond VARCHAR(16),
  atom1 VARCHAR(16),
  atom2 VARCHAR(16),
  bondtype_1 VARCHAR(8),
  bondtype_2 VARCHAR(8),
  bondtype_3 VARCHAR(8));

CREATE INDEX index_compounds_compound ON compounds ( compound );
CREATE INDEX index_compounds_atom ON compounds ( atom );
CREATE INDEX index_atoms_atom ON atoms ( atom );
CREATE INDEX index_bonds_bond ON bonds ( bond );
CREATE INDEX index_bonds_atom1 ON bonds ( atom1 );
CREATE INDEX index_bonds_atom2 ON bonds ( atom2 );

