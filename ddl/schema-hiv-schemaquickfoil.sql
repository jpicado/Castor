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
CREATE TABLE element_C (
  atom VARCHAR(16)
);
CREATE TABLE element_O (
  atom VARCHAR(16)
);
CREATE TABLE element_N (
  atom VARCHAR(16)
);
CREATE TABLE element_H (
  atom VARCHAR(16)
);
CREATE TABLE element_S (
  atom VARCHAR(16)
);
CREATE TABLE element_F (
  atom VARCHAR(16)
);
CREATE TABLE element_Cl (
  atom VARCHAR(16)
);
CREATE TABLE element_P (
  atom VARCHAR(16)
);
CREATE TABLE element_Br (
  atom VARCHAR(16)
);
CREATE TABLE element_I (
  atom VARCHAR(16)
);
CREATE TABLE element_Na (
  atom VARCHAR(16)
);
CREATE TABLE element_Se (
  atom VARCHAR(16)
);
CREATE TABLE element_Sn (
  atom VARCHAR(16)
);
CREATE TABLE element_Cu (
  atom VARCHAR(16)
);
CREATE TABLE element_Si (
  atom VARCHAR(16)
);
CREATE TABLE element_As (
  atom VARCHAR(16)
);
CREATE TABLE element_B (
  atom VARCHAR(16)
);
CREATE TABLE element_Ge (
  atom VARCHAR(16)
);
CREATE TABLE element_Ac (
  atom VARCHAR(16)
);
CREATE TABLE element_Mg (
  atom VARCHAR(16)
);
CREATE TABLE element_Rh (
  atom VARCHAR(16)
);
CREATE TABLE element_Fe (
  atom VARCHAR(16)
);
CREATE TABLE element_Ru (
  atom VARCHAR(16)
);
CREATE TABLE element_Co (
  atom VARCHAR(16)
);
CREATE TABLE element_Bi (
  atom VARCHAR(16)
);
CREATE TABLE element_Pd (
  atom VARCHAR(16)
);
CREATE TABLE element_Ni (
  atom VARCHAR(16)
);
CREATE TABLE element_Sb (
  atom VARCHAR(16)
);
CREATE TABLE element_Pt (
  atom VARCHAR(16)
);
CREATE TABLE element_Ir (
  atom VARCHAR(16)
);
CREATE TABLE element_Re (
  atom VARCHAR(16)
);
CREATE TABLE element_Mn (
  atom VARCHAR(16)
);
CREATE TABLE element_W (
  atom VARCHAR(16)
);
CREATE TABLE element_Mo (
  atom VARCHAR(16)
);
CREATE TABLE element_Gd (
  atom VARCHAR(16)
);
CREATE TABLE element_Tl (
  atom VARCHAR(16)
);
CREATE TABLE element_Zn (
  atom VARCHAR(16)
);
CREATE TABLE element_Hg (
  atom VARCHAR(16)
);
CREATE TABLE element_Ho (
  atom VARCHAR(16)
);
CREATE TABLE element_Pb (
  atom VARCHAR(16)
);
CREATE TABLE element_Cr (
  atom VARCHAR(16)
);
CREATE TABLE element_Ag (
  atom VARCHAR(16)
);
CREATE TABLE element_Ga (
  atom VARCHAR(16)
);
CREATE TABLE element_Au (
  atom VARCHAR(16)
);
CREATE TABLE element_Li (
  atom VARCHAR(16)
);
CREATE TABLE element_K (
  atom VARCHAR(16)
);
CREATE TABLE element_Cs (
  atom VARCHAR(16)
);
CREATE TABLE element_Nb (
  atom VARCHAR(16)
);
CREATE TABLE element_V (
  atom VARCHAR(16)
);
CREATE TABLE element_Nd (
  atom VARCHAR(16)
);
CREATE TABLE element_Al (
  atom VARCHAR(16)
);
CREATE TABLE element_Zr (
  atom VARCHAR(16)
);
CREATE TABLE element_Te (
  atom VARCHAR(16)
);
CREATE TABLE element_Ti (
  atom VARCHAR(16)
);
CREATE TABLE element_Cd (
  atom VARCHAR(16)
);
CREATE TABLE element_Yb (
  atom VARCHAR(16)
);
CREATE TABLE element_Ca (
  atom VARCHAR(16)
);
CREATE TABLE element_U (
  atom VARCHAR(16)
);
CREATE TABLE element_Er (
  atom VARCHAR(16)
);
CREATE TABLE element_Pr (
  atom VARCHAR(16)
);
CREATE TABLE element_Sm (
  atom VARCHAR(16)
);
CREATE TABLE element_Os (
  atom VARCHAR(16)
);
CREATE TABLE element_Tb (
  atom VARCHAR(16)
);
CREATE TABLE p2_0 (
  atom VARCHAR(16)
);
CREATE TABLE p2_1 (
  atom VARCHAR(16)
);
CREATE TABLE p2_2 (
  atom VARCHAR(16)
);
CREATE TABLE p2_3 (
  atom VARCHAR(16)
);
CREATE TABLE p2_4 (
  atom VARCHAR(16)
);
CREATE TABLE p2_5 (
  atom VARCHAR(16)
);
CREATE TABLE p2_6 (
  atom VARCHAR(16)
);
CREATE TABLE p2_7 (
  atom VARCHAR(16)
);
CREATE TABLE p2_8 (
  atom VARCHAR(16)
);
CREATE TABLE p2_9 (
  atom VARCHAR(16)
);
CREATE TABLE p2_10 (
  atom VARCHAR(16)
);
CREATE TABLE p3 (
  atom VARCHAR(16)
);
CREATE TABLE bonds ( 
  bond VARCHAR(16),
  atom1 VARCHAR(16),
  atom2 VARCHAR(16)
);
CREATE TABLE bondtype_1 (
  bond VARCHAR(16)
);
CREATE TABLE bondtype_2 (
  bond VARCHAR(16)
);
CREATE TABLE bondtype_3 (
  bond VARCHAR(16)
);


CREATE INDEX index_compounds_compound ON compounds ( compound );
CREATE INDEX index_compounds_atom ON compounds ( atom );

CREATE INDEX index_element_C_atom ON element_C ( atom );
CREATE INDEX index_element_O_atom ON element_O ( atom );
CREATE INDEX index_element_N_atom ON element_N ( atom );
CREATE INDEX index_element_H_atom ON element_H ( atom );
CREATE INDEX index_element_S_atom ON element_S ( atom );
CREATE INDEX index_element_F_atom ON element_F ( atom );
CREATE INDEX index_element_Cl_atom ON element_Cl ( atom );
CREATE INDEX index_element_P_atom ON element_P ( atom );
CREATE INDEX index_element_Br_atom ON element_Br ( atom );
CREATE INDEX index_element_I_atom ON element_I ( atom );
CREATE INDEX index_element_Na_atom ON element_Na ( atom );
CREATE INDEX index_element_Se_atom ON element_Se ( atom );
CREATE INDEX index_element_Sn_atom ON element_Sn ( atom );
CREATE INDEX index_element_Cu_atom ON element_Cu ( atom );
CREATE INDEX index_element_Si_atom ON element_Si ( atom );
CREATE INDEX index_element_As_atom ON element_As ( atom );
CREATE INDEX index_element_B_atom ON element_B ( atom );
CREATE INDEX index_element_Ge_atom ON element_Ge ( atom );
CREATE INDEX index_element_Ac_atom ON element_Ac ( atom );
CREATE INDEX index_element_Mg_atom ON element_Mg ( atom );
CREATE INDEX index_element_Rh_atom ON element_Rh ( atom );
CREATE INDEX index_element_Fe_atom ON element_Fe ( atom );
CREATE INDEX index_element_Ru_atom ON element_Ru ( atom );
CREATE INDEX index_element_Co_atom ON element_Co ( atom );
CREATE INDEX index_element_Bi_atom ON element_Bi ( atom );
CREATE INDEX index_element_Pd_atom ON element_Pd ( atom );
CREATE INDEX index_element_Ni_atom ON element_Ni ( atom );
CREATE INDEX index_element_Sb_atom ON element_Sb ( atom );
CREATE INDEX index_element_Pt_atom ON element_Pt ( atom );
CREATE INDEX index_element_Ir_atom ON element_Ir ( atom );
CREATE INDEX index_element_Re_atom ON element_Re ( atom );
CREATE INDEX index_element_Mn_atom ON element_Mn ( atom );
CREATE INDEX index_element_W_atom ON element_W ( atom );
CREATE INDEX index_element_Mo_atom ON element_Mo ( atom );
CREATE INDEX index_element_Gd_atom ON element_Gd ( atom );
CREATE INDEX index_element_Tl_atom ON element_Tl ( atom );
CREATE INDEX index_element_Zn_atom ON element_Zn ( atom );
CREATE INDEX index_element_Hg_atom ON element_Hg ( atom );
CREATE INDEX index_element_Ho_atom ON element_Ho ( atom );
CREATE INDEX index_element_Pb_atom ON element_Pb ( atom );
CREATE INDEX index_element_Cr_atom ON element_Cr ( atom );
CREATE INDEX index_element_Ag_atom ON element_Ag ( atom );
CREATE INDEX index_element_Ga_atom ON element_Ga ( atom );
CREATE INDEX index_element_Au_atom ON element_Au ( atom );
CREATE INDEX index_element_Li_atom ON element_Li ( atom );
CREATE INDEX index_element_K_atom ON element_K ( atom );
CREATE INDEX index_element_Cs_atom ON element_Cs ( atom );
CREATE INDEX index_element_Nb_atom ON element_Nb ( atom );
CREATE INDEX index_element_V_atom ON element_V ( atom );
CREATE INDEX index_element_Nd_atom ON element_Nd ( atom );
CREATE INDEX index_element_Al_atom ON element_Al ( atom );
CREATE INDEX index_element_Zr_atom ON element_Zr ( atom );
CREATE INDEX index_element_Te_atom ON element_Te ( atom );
CREATE INDEX index_element_Ti_atom ON element_Ti ( atom );
CREATE INDEX index_element_Cd_atom ON element_Cd ( atom );
CREATE INDEX index_element_Yb_atom ON element_Yb ( atom );
CREATE INDEX index_element_Ca_atom ON element_Ca ( atom );
CREATE INDEX index_element_U_atom ON element_U ( atom );
CREATE INDEX index_element_Er_atom ON element_Er ( atom );
CREATE INDEX index_element_Pr_atom ON element_Pr ( atom );
CREATE INDEX index_element_Sm_atom ON element_Sm ( atom );
CREATE INDEX index_element_Os_atom ON element_Os ( atom );
CREATE INDEX index_element_Tb_atom ON element_Tb ( atom );
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
