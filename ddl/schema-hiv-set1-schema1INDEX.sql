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
CREATE TABLE atoms_element_C (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_O (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_N (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_H (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_S (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_F (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Cl (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_P (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Br (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_I (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Na (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Se (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Sn (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Cu (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Si (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_As (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_B (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ge (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ac (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Mg (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Rh (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Fe (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ru (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Co (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Bi (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Pd (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ni (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Sb (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Pt (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ir (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Re (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Mn (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_W (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Mo (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Gd (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Tl (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Zn (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Hg (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ho (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Pb (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Cr (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ag (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ga (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Au (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Li (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_K (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Cs (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Nb (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_V (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Nd (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Al (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Zr (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Te (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ti (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Cd (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Yb (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Ca (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_U (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Er (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Pr (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Sm (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Os (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE atoms_element_Tb (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_0 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_1 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_2 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_3 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_4 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_5 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_6 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_7 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_8 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_9 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p2_10 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE p3 (
  atom VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE bonds ( 
  bond VARCHAR(16),
  atom1 VARCHAR(16),
  atom2 VARCHAR(16)
);
CREATE TABLE bondtype_1 (
  bond VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE bondtype_2 (
  bond VARCHAR(16),
  flag VARCHAR(8)
);
CREATE TABLE bondtype_3 (
  bond VARCHAR(16),
  flag VARCHAR(8)
);


CREATE INDEX index_compounds_compound ON compounds ( compound );
CREATE INDEX index_compounds_atom ON compounds ( atom );

CREATE INDEX index_atoms_element_C_atom ON atoms_element_C ( atom );
CREATE INDEX index_atoms_element_O_atom ON atoms_element_O ( atom );
CREATE INDEX index_atoms_element_N_atom ON atoms_element_N ( atom );
CREATE INDEX index_atoms_element_H_atom ON atoms_element_H ( atom );
CREATE INDEX index_atoms_element_S_atom ON atoms_element_S ( atom );
CREATE INDEX index_atoms_element_F_atom ON atoms_element_F ( atom );
CREATE INDEX index_atoms_element_Cl_atom ON atoms_element_Cl ( atom );
CREATE INDEX index_atoms_element_P_atom ON atoms_element_P ( atom );
CREATE INDEX index_atoms_element_Br_atom ON atoms_element_Br ( atom );
CREATE INDEX index_atoms_element_I_atom ON atoms_element_I ( atom );
CREATE INDEX index_atoms_element_Na_atom ON atoms_element_Na ( atom );
CREATE INDEX index_atoms_element_Se_atom ON atoms_element_Se ( atom );
CREATE INDEX index_atoms_element_Sn_atom ON atoms_element_Sn ( atom );
CREATE INDEX index_atoms_element_Cu_atom ON atoms_element_Cu ( atom );
CREATE INDEX index_atoms_element_Si_atom ON atoms_element_Si ( atom );
CREATE INDEX index_atoms_element_As_atom ON atoms_element_As ( atom );
CREATE INDEX index_atoms_element_B_atom ON atoms_element_B ( atom );
CREATE INDEX index_atoms_element_Ge_atom ON atoms_element_Ge ( atom );
CREATE INDEX index_atoms_element_Ac_atom ON atoms_element_Ac ( atom );
CREATE INDEX index_atoms_element_Mg_atom ON atoms_element_Mg ( atom );
CREATE INDEX index_atoms_element_Rh_atom ON atoms_element_Rh ( atom );
CREATE INDEX index_atoms_element_Fe_atom ON atoms_element_Fe ( atom );
CREATE INDEX index_atoms_element_Ru_atom ON atoms_element_Ru ( atom );
CREATE INDEX index_atoms_element_Co_atom ON atoms_element_Co ( atom );
CREATE INDEX index_atoms_element_Bi_atom ON atoms_element_Bi ( atom );
CREATE INDEX index_atoms_element_Pd_atom ON atoms_element_Pd ( atom );
CREATE INDEX index_atoms_element_Ni_atom ON atoms_element_Ni ( atom );
CREATE INDEX index_atoms_element_Sb_atom ON atoms_element_Sb ( atom );
CREATE INDEX index_atoms_element_Pt_atom ON atoms_element_Pt ( atom );
CREATE INDEX index_atoms_element_Ir_atom ON atoms_element_Ir ( atom );
CREATE INDEX index_atoms_element_Re_atom ON atoms_element_Re ( atom );
CREATE INDEX index_atoms_element_Mn_atom ON atoms_element_Mn ( atom );
CREATE INDEX index_atoms_element_W_atom ON atoms_element_W ( atom );
CREATE INDEX index_atoms_element_Mo_atom ON atoms_element_Mo ( atom );
CREATE INDEX index_atoms_element_Gd_atom ON atoms_element_Gd ( atom );
CREATE INDEX index_atoms_element_Tl_atom ON atoms_element_Tl ( atom );
CREATE INDEX index_atoms_element_Zn_atom ON atoms_element_Zn ( atom );
CREATE INDEX index_atoms_element_Hg_atom ON atoms_element_Hg ( atom );
CREATE INDEX index_atoms_element_Ho_atom ON atoms_element_Ho ( atom );
CREATE INDEX index_atoms_element_Pb_atom ON atoms_element_Pb ( atom );
CREATE INDEX index_atoms_element_Cr_atom ON atoms_element_Cr ( atom );
CREATE INDEX index_atoms_element_Ag_atom ON atoms_element_Ag ( atom );
CREATE INDEX index_atoms_element_Ga_atom ON atoms_element_Ga ( atom );
CREATE INDEX index_atoms_element_Au_atom ON atoms_element_Au ( atom );
CREATE INDEX index_atoms_element_Li_atom ON atoms_element_Li ( atom );
CREATE INDEX index_atoms_element_K_atom ON atoms_element_K ( atom );
CREATE INDEX index_atoms_element_Cs_atom ON atoms_element_Cs ( atom );
CREATE INDEX index_atoms_element_Nb_atom ON atoms_element_Nb ( atom );
CREATE INDEX index_atoms_element_V_atom ON atoms_element_V ( atom );
CREATE INDEX index_atoms_element_Nd_atom ON atoms_element_Nd ( atom );
CREATE INDEX index_atoms_element_Al_atom ON atoms_element_Al ( atom );
CREATE INDEX index_atoms_element_Zr_atom ON atoms_element_Zr ( atom );
CREATE INDEX index_atoms_element_Te_atom ON atoms_element_Te ( atom );
CREATE INDEX index_atoms_element_Ti_atom ON atoms_element_Ti ( atom );
CREATE INDEX index_atoms_element_Cd_atom ON atoms_element_Cd ( atom );
CREATE INDEX index_atoms_element_Yb_atom ON atoms_element_Yb ( atom );
CREATE INDEX index_atoms_element_Ca_atom ON atoms_element_Ca ( atom );
CREATE INDEX index_atoms_element_U_atom ON atoms_element_U ( atom );
CREATE INDEX index_atoms_element_Er_atom ON atoms_element_Er ( atom );
CREATE INDEX index_atoms_element_Pr_atom ON atoms_element_Pr ( atom );
CREATE INDEX index_atoms_element_Sm_atom ON atoms_element_Sm ( atom );
CREATE INDEX index_atoms_element_Os_atom ON atoms_element_Os ( atom );
CREATE INDEX index_atoms_element_Tb_atom ON atoms_element_Tb ( atom );
CREATE INDEX index_p2_0_atom ON p2_0 ( atom );
CREATE INDEX index_p2_1_atom ON p2_1 ( atom );
CREATE INDEX index_p2_2_atom ON p2_2 ( atom );
CREATE INDEX index_p2_3_atom ON p2_3 ( atom );
CREATE INDEX index_p2_4_atom ON p2_4 ( atom );
CREATE INDEX index_p2_5_atom ON p2_5 ( atom );
CREATE INDEX index_p2_6_atom ON p2_6 ( atom );
CREATE INDEX index_p2_7_atom ON p2_7 ( atom );
CREATE INDEX index_p2_8_atom ON p2_8 ( atom );
CREATE INDEX index_p2_9_atom ON p2_9 ( atom );
CREATE INDEX index_p2_10_atom ON p2_10 ( atom );
CREATE INDEX index_p3_atom ON p3 ( atom );

CREATE INDEX index_bonds_bond ON bonds ( bond );
CREATE INDEX index_bonds_atom1 ON bonds ( atom1 );
CREATE INDEX index_bonds_atom2 ON bonds ( atom2 );

CREATE INDEX index_bondtype_1_bond ON bondtype_1 ( bond );
CREATE INDEX index_bondtype_2_bond ON bondtype_2 ( bond );
CREATE INDEX index_bondtype_3_bond ON bondtype_3 ( bond );
