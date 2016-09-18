CREATE TABLE linkid (
linkid VARCHAR(32)
, CONSTRAINT pk_linkid PRIMARY KEY (linkid)
);
CREATE TABLE page (
page VARCHAR(32)
, CONSTRAINT pk_page PRIMARY KEY (page)
);
CREATE TABLE student_page_test_neg (
page VARCHAR(32)
, CONSTRAINT pk_student_page_test_neg PRIMARY KEY (page)
);
CREATE TABLE student_page_test_pos (
page VARCHAR(32)
, CONSTRAINT pk_student_page_test_pos PRIMARY KEY (page)
);
CREATE TABLE student_page_train_pos (
page VARCHAR(32)
, CONSTRAINT pk_student_page_train_pos PRIMARY KEY (page)
);
CREATE TABLE student_page_train_neg (
page VARCHAR(32)
, CONSTRAINT pk_student_page_train_neg PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmultimedia (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmultimedia PRIMARY KEY (linkid)
);
CREATE TABLE has_small (
page VARCHAR(32)
, CONSTRAINT pk_has_small PRIMARY KEY (page)
);
CREATE TABLE has_anchor_levi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_levi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sharp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sharp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstate (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstate PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsohi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsohi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_borriello (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_borriello PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lazowska (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lazowska PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrecent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrecent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodweather (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodweather PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_build (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_build PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstandard (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstandard PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodproblem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodproblem PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodreview (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreview PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodanand (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanand PRIMARY KEY (linkid)
);
CREATE TABLE has_limit (
page VARCHAR(32)
, CONSTRAINT pk_has_limit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpierc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpierc PRIMARY KEY (linkid)
);
CREATE TABLE has_nation (
page VARCHAR(32)
, CONSTRAINT pk_has_nation PRIMARY KEY (page)
);
CREATE TABLE has_societi (
page VARCHAR(32)
, CONSTRAINT pk_has_societi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodandrew (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodandrew PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvision (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvision PRIMARY KEY (linkid)
);
CREATE TABLE has_part (
page VARCHAR(32)
, CONSTRAINT pk_has_part PRIMARY KEY (page)
);
CREATE TABLE has_anchor_evalu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_evalu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodscienc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodscienc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_scout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_scout PRIMARY KEY (linkid)
);
CREATE TABLE has_tabl (
page VARCHAR(32)
, CONSTRAINT pk_has_tabl PRIMARY KEY (page)
);
CREATE TABLE has_anchor_set (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_set PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodview (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodview PRIMARY KEY (linkid)
);
CREATE TABLE has_compil (
page VARCHAR(32)
, CONSTRAINT pk_has_compil PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhtml (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhtml PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmayberri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmayberri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_peterson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_peterson PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsengul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsengul PRIMARY KEY (linkid)
);
CREATE TABLE has_analysi (
page VARCHAR(32)
, CONSTRAINT pk_has_analysi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_chri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chri PRIMARY KEY (linkid)
);
CREATE TABLE has_explor (
page VARCHAR(32)
, CONSTRAINT pk_has_explor PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodseptemb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodseptemb PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_algebra (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_algebra PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_austin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_austin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpaul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpaul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtunnel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtunnel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtarun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtarun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsteve (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsteve PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_andi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_andi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_solomon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_solomon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpaper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpaper PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_famili (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_famili PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlinear (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlinear PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_server (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_server PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxiao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxiao PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhewett (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhewett PRIMARY KEY (linkid)
);
CREATE TABLE has_relat (
page VARCHAR(32)
, CONSTRAINT pk_has_relat PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodalgorithm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodalgorithm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfavorit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfavorit PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodraghu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodraghu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhand (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhand PRIMARY KEY (linkid)
);
CREATE TABLE has_charact (
page VARCHAR(32)
, CONSTRAINT pk_has_charact PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodresult (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodresult PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chamber (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chamber PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinteract (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinteract PRIMARY KEY (linkid)
);
CREATE TABLE has_adapt (
page VARCHAR(32)
, CONSTRAINT pk_has_adapt PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbalayoghan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbalayoghan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_leavi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_leavi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodping (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodping PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_softbot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_softbot PRIMARY KEY (linkid)
);
CREATE TABLE has_instructor (
page VARCHAR(32)
, CONSTRAINT pk_has_instructor PRIMARY KEY (page)
);
CREATE TABLE has_anchor_falsafi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_falsafi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_forward (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_forward PRIMARY KEY (linkid)
);
CREATE TABLE has_lt (
page VARCHAR(32)
, CONSTRAINT pk_has_lt PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgunnel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgunnel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_document (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_document PRIMARY KEY (linkid)
);
CREATE TABLE has_recent (
page VARCHAR(32)
, CONSTRAINT pk_has_recent PRIMARY KEY (page)
);
CREATE TABLE has_ll (
page VARCHAR(32)
, CONSTRAINT pk_has_ll PRIMARY KEY (page)
);
CREATE TABLE has_avoid (
page VARCHAR(32)
, CONSTRAINT pk_has_avoid PRIMARY KEY (page)
);
CREATE TABLE has_anchor_schedul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_schedul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkevin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkevin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddevis (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddevis PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_file (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_file PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbogo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbogo PRIMARY KEY (linkid)
);
CREATE TABLE has_start (
page VARCHAR(32)
, CONSTRAINT pk_has_start PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodkaplan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkaplan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoffer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoffer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hummert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hummert PRIMARY KEY (linkid)
);
CREATE TABLE has_visit (
page VARCHAR(32)
, CONSTRAINT pk_has_visit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjame (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjame PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_blumof (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_blumof PRIMARY KEY (linkid)
);
CREATE TABLE has_fortran (
page VARCHAR(32)
, CONSTRAINT pk_has_fortran PRIMARY KEY (page)
);
CREATE TABLE has_period (
page VARCHAR(32)
, CONSTRAINT pk_has_period PRIMARY KEY (page)
);
CREATE TABLE has_anchor_keppel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_keppel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_feelei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_feelei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mike (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mike PRIMARY KEY (linkid)
);
CREATE TABLE has_applic (
page VARCHAR(32)
, CONSTRAINT pk_has_applic PRIMARY KEY (page)
);
CREATE TABLE has_austin (
page VARCHAR(32)
, CONSTRAINT pk_has_austin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodstuart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstuart PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodroom (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodroom PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ausland (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ausland PRIMARY KEY (linkid)
);
CREATE TABLE has_switch (
page VARCHAR(32)
, CONSTRAINT pk_has_switch PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpawan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpawan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhung (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhung PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyanbin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyanbin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodann (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodann PRIMARY KEY (linkid)
);
CREATE TABLE has_wisconsin (
page VARCHAR(32)
, CONSTRAINT pk_has_wisconsin PRIMARY KEY (page)
);
CREATE TABLE has_anchor_month (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_month PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_fpga (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fpga PRIMARY KEY (linkid)
);
CREATE TABLE has_char (
page VARCHAR(32)
, CONSTRAINT pk_has_char PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbrian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbrian PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhome (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhome PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkuiper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkuiper PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_palacharla (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_palacharla PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_model (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_model PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chateau (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chateau PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodge (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodge PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwindow (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwindow PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwisconsin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwisconsin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodegger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodegger PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_misra (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_misra PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgt PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcherukup (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcherukup PRIMARY KEY (linkid)
);
CREATE TABLE has_texa (
page VARCHAR(32)
, CONSTRAINT pk_has_texa PRIMARY KEY (page)
);
CREATE TABLE has_cover (
page VARCHAR(32)
, CONSTRAINT pk_has_cover PRIMARY KEY (page)
);
CREATE TABLE has_anchor_maria (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_maria PRIMARY KEY (linkid)
);
CREATE TABLE has_demonstr (
page VARCHAR(32)
, CONSTRAINT pk_has_demonstr PRIMARY KEY (page)
);
CREATE TABLE has_text (
page VARCHAR(32)
, CONSTRAINT pk_has_text PRIMARY KEY (page)
);
CREATE TABLE has_room (
page VARCHAR(32)
, CONSTRAINT pk_has_room PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhuang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhuang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_psp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_psp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodybliu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodybliu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprogram (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprogram PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooderic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooderic PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_databas (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_databas PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkharker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkharker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_contact (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_contact PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcach PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsyllabu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsyllabu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_demo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_demo PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gaetano (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gaetano PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_internet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_internet PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tunnel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tunnel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyau (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyau PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lorenzo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lorenzo PRIMARY KEY (linkid)
);
CREATE TABLE has_declar (
page VARCHAR(32)
, CONSTRAINT pk_has_declar PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodreport (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreport PRIMARY KEY (linkid)
);
CREATE TABLE has_cach (
page VARCHAR(32)
, CONSTRAINT pk_has_cach PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodseattl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodseattl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfridai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfridai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfrancoi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfrancoi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodboard (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodboard PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtravel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtravel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvisit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvisit PRIMARY KEY (linkid)
);
CREATE TABLE has_netscap (
page VARCHAR(32)
, CONSTRAINT pk_has_netscap PRIMARY KEY (page)
);
CREATE TABLE has_anchor_guid (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_guid PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodparadyn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodparadyn PRIMARY KEY (linkid)
);
CREATE TABLE has_combin (
page VARCHAR(32)
, CONSTRAINT pk_has_combin PRIMARY KEY (page)
);
CREATE TABLE has_futur (
page VARCHAR(32)
, CONSTRAINT pk_has_futur PRIMARY KEY (page)
);
CREATE TABLE has_total (
page VARCHAR(32)
, CONSTRAINT pk_has_total PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgamelan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgamelan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsingh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsingh PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_golden (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_golden PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkaltenbach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkaltenbach PRIMARY KEY (linkid)
);
CREATE TABLE has_creat (
page VARCHAR(32)
, CONSTRAINT pk_has_creat PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgouda (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgouda PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_space (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_space PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmateri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmateri PRIMARY KEY (linkid)
);
CREATE TABLE has_pass (
page VARCHAR(32)
, CONSTRAINT pk_has_pass PRIMARY KEY (page)
);
CREATE TABLE has_anchor_ai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkedar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkedar PRIMARY KEY (linkid)
);
CREATE TABLE has_past (
page VARCHAR(32)
, CONSTRAINT pk_has_past PRIMARY KEY (page)
);
CREATE TABLE has_hardwar (
page VARCHAR(32)
, CONSTRAINT pk_has_hardwar PRIMARY KEY (page)
);
CREATE TABLE has_anchor_baer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_baer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ec (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ec PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_email (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_email PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlevi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlevi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_librari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_librari PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmeghan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmeghan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_fortran (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fortran PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodthompson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodthompson PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_summari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_summari PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_astronomi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_astronomi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcruz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcruz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_indian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_indian PRIMARY KEY (linkid)
);
CREATE TABLE has_discuss (
page VARCHAR(32)
, CONSTRAINT pk_has_discuss PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwork (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwork PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_interact (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_interact PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_estlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_estlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooderemolin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooderemolin PRIMARY KEY (linkid)
);
CREATE TABLE has_devic (
page VARCHAR(32)
, CONSTRAINT pk_has_devic PRIMARY KEY (page)
);
CREATE TABLE has_motion (
page VARCHAR(32)
, CONSTRAINT pk_has_motion PRIMARY KEY (page)
);
CREATE TABLE has_anchor_voelker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_voelker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_franklin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_franklin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_numer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_numer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvita (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvita PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tool (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tool PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgong PRIMARY KEY (linkid)
);
CREATE TABLE has_maintain (
page VARCHAR(32)
, CONSTRAINT pk_has_maintain PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsoftwar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsoftwar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgunther (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgunther PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodthing (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodthing PRIMARY KEY (linkid)
);
CREATE TABLE has_written (
page VARCHAR(32)
, CONSTRAINT pk_has_written PRIMARY KEY (page)
);
CREATE TABLE has_anchor_calendar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_calendar PRIMARY KEY (linkid)
);
CREATE TABLE has_symposium (
page VARCHAR(32)
, CONSTRAINT pk_has_symposium PRIMARY KEY (page)
);
CREATE TABLE has_anchor_structur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_structur PRIMARY KEY (linkid)
);
CREATE TABLE has_set (
page VARCHAR(32)
, CONSTRAINT pk_has_set PRIMARY KEY (page)
);
CREATE TABLE has_high (
page VARCHAR(32)
, CONSTRAINT pk_has_high PRIMARY KEY (page)
);
CREATE TABLE has_anchor_group (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_group PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpower (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpower PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmark PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_teitelbaum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_teitelbaum PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cse (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cse PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_csl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_csl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_reason (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_reason PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyoung (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyoung PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_creation (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_creation PRIMARY KEY (linkid)
);
CREATE TABLE has_associ (
page VARCHAR(32)
, CONSTRAINT pk_has_associ PRIMARY KEY (page)
);
CREATE TABLE has_wa (
page VARCHAR(32)
, CONSTRAINT pk_has_wa PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcover (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcover PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_astronaut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_astronaut PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpecina (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpecina PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvladimir (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvladimir PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_exodu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_exodu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_doug (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_doug PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcalvin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcalvin PRIMARY KEY (linkid)
);
CREATE TABLE has_hour (
page VARCHAR(32)
, CONSTRAINT pk_has_hour PRIMARY KEY (page)
);
CREATE TABLE has_anchor_chan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcse (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcse PRIMARY KEY (linkid)
);
CREATE TABLE has_handout (
page VARCHAR(32)
, CONSTRAINT pk_has_handout PRIMARY KEY (page)
);
CREATE TABLE has_event (
page VARCHAR(32)
, CONSTRAINT pk_has_event PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodshore (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshore PRIMARY KEY (linkid)
);
CREATE TABLE has_interpret (
page VARCHAR(32)
, CONSTRAINT pk_has_interpret PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodreturn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreturn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarco (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarco PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgarbag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgarbag PRIMARY KEY (linkid)
);
CREATE TABLE has_industri (
page VARCHAR(32)
, CONSTRAINT pk_has_industri PRIMARY KEY (page)
);
CREATE TABLE has_anchor_salesin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_salesin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cronquist (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cronquist PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_design (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_design PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgeeta (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgeeta PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_david (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_david PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jean (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jean PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbershad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbershad PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbruce (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbruce PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_borland (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_borland PRIMARY KEY (linkid)
);
CREATE TABLE has_import (
page VARCHAR(32)
, CONSTRAINT pk_has_import PRIMARY KEY (page)
);
CREATE TABLE has_anchor_amp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_amp PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlevel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlevel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodadrienn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadrienn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_grade (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_grade PRIMARY KEY (linkid)
);
CREATE TABLE has_share (
page VARCHAR(32)
, CONSTRAINT pk_has_share PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodristo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodristo PRIMARY KEY (linkid)
);
CREATE TABLE has_pictur (
page VARCHAR(32)
, CONSTRAINT pk_has_pictur PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbert PRIMARY KEY (linkid)
);
CREATE TABLE has_gener (
page VARCHAR(32)
, CONSTRAINT pk_has_gener PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbhanu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbhanu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodphilip (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodphilip PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kuiper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kuiper PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_romer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_romer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgrade (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgrade PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_search (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_search PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_charl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_charl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wind (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wind PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodguyer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguyer PRIMARY KEY (linkid)
);
CREATE TABLE has_neural (
page VARCHAR(32)
, CONSTRAINT pk_has_neural PRIMARY KEY (page)
);
CREATE TABLE has_anchor_convers (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_convers PRIMARY KEY (linkid)
);
CREATE TABLE has_number (
page VARCHAR(32)
, CONSTRAINT pk_has_number PRIMARY KEY (page)
);
CREATE TABLE has_anchor_textbook (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_textbook PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodherbert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodherbert PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodterm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodterm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwalker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwalker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tian PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mail PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_main (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_main PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_individu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_individu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddata (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddata PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcad PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddionysio (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddionysio PRIMARY KEY (linkid)
);
CREATE TABLE has_variabl (
page VARCHAR(32)
, CONSTRAINT pk_has_variabl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsrinivasan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsrinivasan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhao PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmicheal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmicheal PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodelain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodelain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlifschitz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlifschitz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_past (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_past PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjul PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_agent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_agent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcontact (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcontact PRIMARY KEY (linkid)
);
CREATE TABLE has_electr (
page VARCHAR(32)
, CONSTRAINT pk_has_electr PRIMARY KEY (page)
);
CREATE TABLE has_anchor_power (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_power PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_associ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_associ PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlyn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlyn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpoint (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpoint PRIMARY KEY (linkid)
);
CREATE TABLE has_ncsa (
page VARCHAR(32)
, CONSTRAINT pk_has_ncsa PRIMARY KEY (page)
);
CREATE TABLE has_integ (
page VARCHAR(32)
, CONSTRAINT pk_has_integ PRIMARY KEY (page)
);
CREATE TABLE has_anchor_public (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_public PRIMARY KEY (linkid)
);
CREATE TABLE has_involv (
page VARCHAR(32)
, CONSTRAINT pk_has_involv PRIMARY KEY (page)
);
CREATE TABLE has_anchor_jeremi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jeremi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxingshan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxingshan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_alain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_alain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmemori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmemori PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodedward (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodedward PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_industri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_industri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodalan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodalan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodastronomi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodastronomi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodben (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodben PRIMARY KEY (linkid)
);
CREATE TABLE has_seattl (
page VARCHAR(32)
, CONSTRAINT pk_has_seattl PRIMARY KEY (page)
);
CREATE TABLE has_anchor_simon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_simon PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_adam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_adam PRIMARY KEY (linkid)
);
CREATE TABLE has_type (
page VARCHAR(32)
, CONSTRAINT pk_has_type PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsolver (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsolver PRIMARY KEY (linkid)
);
CREATE TABLE has_math (
page VARCHAR(32)
, CONSTRAINT pk_has_math PRIMARY KEY (page)
);
CREATE TABLE has_return (
page VARCHAR(32)
, CONSTRAINT pk_has_return PRIMARY KEY (page)
);
CREATE TABLE has_anchor_vladimir (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vladimir PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsieg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsieg PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_perkowitz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_perkowitz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_applic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_applic PRIMARY KEY (linkid)
);
CREATE TABLE has_write (
page VARCHAR(32)
, CONSTRAINT pk_has_write PRIMARY KEY (page)
);
CREATE TABLE has_solv (
page VARCHAR(32)
, CONSTRAINT pk_has_solv PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodyuanj (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyuanj PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddocum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddocum PRIMARY KEY (linkid)
);
CREATE TABLE has_stuff (
page VARCHAR(32)
, CONSTRAINT pk_has_stuff PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodzuo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzuo PRIMARY KEY (linkid)
);
CREATE TABLE has_method (
page VARCHAR(32)
, CONSTRAINT pk_has_method PRIMARY KEY (page)
);
CREATE TABLE has_anchor_linear (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_linear PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lauren (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lauren PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_manufactur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_manufactur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hewett (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hewett PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shuichi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shuichi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodparent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodparent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcall PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtopic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtopic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodliugt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodliugt PRIMARY KEY (linkid)
);
CREATE TABLE has_internet (
page VARCHAR(32)
, CONSTRAINT pk_has_internet PRIMARY KEY (page)
);
CREATE TABLE has_anchor_zahorjan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_zahorjan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmountain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmountain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddian PRIMARY KEY (linkid)
);
CREATE TABLE has_mode (
page VARCHAR(32)
, CONSTRAINT pk_has_mode PRIMARY KEY (page)
);
CREATE TABLE has_anchor_spin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_spin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wood (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wood PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_assign (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_assign PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyufeng (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyufeng PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_eric (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_eric PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_notkin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_notkin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mirank (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mirank PRIMARY KEY (linkid)
);
CREATE TABLE has_fax (
page VARCHAR(32)
, CONSTRAINT pk_has_fax PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodservic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodservic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhank (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhank PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodajohn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodajohn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_messag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_messag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinvestig (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinvestig PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_anderson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_anderson PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chinook (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chinook PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcool (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcool PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddamani (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddamani PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_talk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_talk PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lectur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lectur PRIMARY KEY (linkid)
);
CREATE TABLE has_gmt (
page VARCHAR(32)
, CONSTRAINT pk_has_gmt PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjiany (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjiany PRIMARY KEY (linkid)
);
CREATE TABLE has_system (
page VARCHAR(32)
, CONSTRAINT pk_has_system PRIMARY KEY (page)
);
CREATE TABLE has_anchor_zhang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_zhang PRIMARY KEY (linkid)
);
CREATE TABLE has_research (
page VARCHAR(32)
, CONSTRAINT pk_has_research PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmirank (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmirank PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcpg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcpg PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_directori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_directori PRIMARY KEY (linkid)
);
CREATE TABLE has_project (
page VARCHAR(32)
, CONSTRAINT pk_has_project PRIMARY KEY (page)
);
CREATE TABLE has_anchor_electr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_electr PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_code (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_code PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_motion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_motion PRIMARY KEY (linkid)
);
CREATE TABLE has_similar (
page VARCHAR(32)
, CONSTRAINT pk_has_similar PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsection (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsection PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodboyer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodboyer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_event (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_event PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_year (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_year PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_imag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_imag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodapplic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodapplic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhomepag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhomepag PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_exponenti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_exponenti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_explor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_explor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodassist (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodassist PRIMARY KEY (linkid)
);
CREATE TABLE has_integr (
page VARCHAR(32)
, CONSTRAINT pk_has_integr PRIMARY KEY (page)
);
CREATE TABLE has_compon (
page VARCHAR(32)
, CONSTRAINT pk_has_compon PRIMARY KEY (page)
);
CREATE TABLE has_robert (
page VARCHAR(32)
, CONSTRAINT pk_has_robert PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfeel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfeel PRIMARY KEY (linkid)
);
CREATE TABLE has_global (
page VARCHAR(32)
, CONSTRAINT pk_has_global PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodshenoi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshenoi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodneural (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodneural PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrequir (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrequir PRIMARY KEY (linkid)
);
CREATE TABLE has_sens (
page VARCHAR(32)
, CONSTRAINT pk_has_sens PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhudson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhudson PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsend PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_handout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_handout PRIMARY KEY (linkid)
);
CREATE TABLE has_compress (
page VARCHAR(32)
, CONSTRAINT pk_has_compress PRIMARY KEY (page)
);
CREATE TABLE has_music (
page VARCHAR(32)
, CONSTRAINT pk_has_music PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsustaita (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsustaita PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_depart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_depart PRIMARY KEY (linkid)
);
CREATE TABLE has_byte (
page VARCHAR(32)
, CONSTRAINT pk_has_byte PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodrwo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrwo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkai PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_offer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_offer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemer PRIMARY KEY (linkid)
);
CREATE TABLE has_machin (
page VARCHAR(32)
, CONSTRAINT pk_has_machin PRIMARY KEY (page)
);
CREATE TABLE has_individu (
page VARCHAR(32)
, CONSTRAINT pk_has_individu PRIMARY KEY (page)
);
CREATE TABLE has_anchor_tutori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tutori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_oper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_oper PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfebruari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfebruari PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodintroduct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodintroduct PRIMARY KEY (linkid)
);
CREATE TABLE has_thu (
page VARCHAR(32)
, CONSTRAINT pk_has_thu PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodkorupolu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkorupolu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cortex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cortex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemeri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemeri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhall PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfrank (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfrank PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsfkaplan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsfkaplan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrobot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrobot PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsmaragd (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsmaragd PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_list (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_list PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvisual (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvisual PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcomput (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcomput PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lisp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lisp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnovak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnovak PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_babak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_babak PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodautomat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodautomat PRIMARY KEY (linkid)
);
CREATE TABLE has_document (
page VARCHAR(32)
, CONSTRAINT pk_has_document PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodagent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodagent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpahardin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpahardin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_seq (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_seq PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_raghu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_raghu PRIMARY KEY (linkid)
);
CREATE TABLE has_john (
page VARCHAR(32)
, CONSTRAINT pk_has_john PRIMARY KEY (page)
);
CREATE TABLE has_place (
page VARCHAR(32)
, CONSTRAINT pk_has_place PRIMARY KEY (page)
);
CREATE TABLE has_consist (
page VARCHAR(32)
, CONSTRAINT pk_has_consist PRIMARY KEY (page)
);
CREATE TABLE has_anchor_site (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_site PRIMARY KEY (linkid)
);
CREATE TABLE has_describ (
page VARCHAR(32)
, CONSTRAINT pk_has_describ PRIMARY KEY (page)
);
CREATE TABLE has_anchor_photo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_photo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcanfield (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcanfield PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_parent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_parent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchaotic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchaotic PRIMARY KEY (linkid)
);
CREATE TABLE has_argum (
page VARCHAR(32)
, CONSTRAINT pk_has_argum PRIMARY KEY (page)
);
CREATE TABLE has_question (
page VARCHAR(32)
, CONSTRAINT pk_has_question PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtropschuh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtropschuh PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodecamahor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodecamahor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsmaragdaki (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsmaragdaki PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcase (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcase PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcheck (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcheck PRIMARY KEY (linkid)
);
CREATE TABLE has_html (
page VARCHAR(32)
, CONSTRAINT pk_has_html PRIMARY KEY (page)
);
CREATE TABLE has_state (
page VARCHAR(32)
, CONSTRAINT pk_has_state PRIMARY KEY (page)
);
CREATE TABLE has_issu (
page VARCHAR(32)
, CONSTRAINT pk_has_issu PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprocessor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprocessor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnetwork (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnetwork PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwang PRIMARY KEY (linkid)
);
CREATE TABLE has_tompa (
page VARCHAR(32)
, CONSTRAINT pk_has_tompa PRIMARY KEY (page)
);
CREATE TABLE has_anchor_utexa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_utexa PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjacob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjacob PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjohnston (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjohnston PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_comment (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_comment PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshaob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshaob PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvijaya (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvijaya PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodteam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodteam PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_progress (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_progress PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_rai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_rai PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_system (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_system PRIMARY KEY (linkid)
);
CREATE TABLE has_initi (
page VARCHAR(32)
, CONSTRAINT pk_has_initi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodallen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodallen PRIMARY KEY (linkid)
);
CREATE TABLE has_mime (
page VARCHAR(32)
, CONSTRAINT pk_has_mime PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodftp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodftp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddigit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddigit PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_compil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_compil PRIMARY KEY (linkid)
);
CREATE TABLE has_topic (
page VARCHAR(32)
, CONSTRAINT pk_has_topic PRIMARY KEY (page)
);
CREATE TABLE has_anchor_examin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_examin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddyer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddyer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_section (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_section PRIMARY KEY (linkid)
);
CREATE TABLE has_institut (
page VARCHAR(32)
, CONSTRAINT pk_has_institut PRIMARY KEY (page)
);
CREATE TABLE has_principl (
page VARCHAR(32)
, CONSTRAINT pk_has_principl PRIMARY KEY (page)
);
CREATE TABLE has_menu (
page VARCHAR(32)
, CONSTRAINT pk_has_menu PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodanthoni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanthoni PRIMARY KEY (linkid)
);
CREATE TABLE has_paramet (
page VARCHAR(32)
, CONSTRAINT pk_has_paramet PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfacil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfacil PRIMARY KEY (linkid)
);
CREATE TABLE has_descript (
page VARCHAR(32)
, CONSTRAINT pk_has_descript PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodvisitor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvisitor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_net (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_net PRIMARY KEY (linkid)
);
CREATE TABLE has_find (
page VARCHAR(32)
, CONSTRAINT pk_has_find PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmechan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmechan PRIMARY KEY (linkid)
);
CREATE TABLE has_oct (
page VARCHAR(32)
, CONSTRAINT pk_has_oct PRIMARY KEY (page)
);
CREATE TABLE has_softwar (
page VARCHAR(32)
, CONSTRAINT pk_has_softwar PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpreviou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpreviou PRIMARY KEY (linkid)
);
CREATE TABLE has_lectur (
page VARCHAR(32)
, CONSTRAINT pk_has_lectur PRIMARY KEY (page)
);
CREATE TABLE has_staff (
page VARCHAR(32)
, CONSTRAINT pk_has_staff PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcredit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcredit PRIMARY KEY (linkid)
);
CREATE TABLE has_pattern (
page VARCHAR(32)
, CONSTRAINT pk_has_pattern PRIMARY KEY (page)
);
CREATE TABLE has_anchor_object (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_object PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_comput (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_comput PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_paradis (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_paradis PRIMARY KEY (linkid)
);
CREATE TABLE has_sieg (
page VARCHAR(32)
, CONSTRAINT pk_has_sieg PRIMARY KEY (page)
);
CREATE TABLE has_anchor_hank (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hank PRIMARY KEY (linkid)
);
CREATE TABLE has_select (
page VARCHAR(32)
, CONSTRAINT pk_has_select PRIMARY KEY (page)
);
CREATE TABLE has_anchor_journal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_journal PRIMARY KEY (linkid)
);
CREATE TABLE has_modul (
page VARCHAR(32)
, CONSTRAINT pk_has_modul PRIMARY KEY (page)
);
CREATE TABLE has_anchor_issu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_issu PRIMARY KEY (linkid)
);
CREATE TABLE has_valu (
page VARCHAR(32)
, CONSTRAINT pk_has_valu PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmelski (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmelski PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_autom (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_autom PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsupervis (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsupervis PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchapter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchapter PRIMARY KEY (linkid)
);
CREATE TABLE has_tree (
page VARCHAR(32)
, CONSTRAINT pk_has_tree PRIMARY KEY (page)
);
CREATE TABLE has_constraint (
page VARCHAR(32)
, CONSTRAINT pk_has_constraint PRIMARY KEY (page)
);
CREATE TABLE has_anchor_fix (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fix PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bradlei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bradlei PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhermjakob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhermjakob PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_joel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_joel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjayadev (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjayadev PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_brad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_brad PRIMARY KEY (linkid)
);
CREATE TABLE has_propos (
page VARCHAR(32)
, CONSTRAINT pk_has_propos PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgeorg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgeorg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgnana (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgnana PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_analysi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_analysi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ben (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ben PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_basic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_basic PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_loup (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_loup PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhead (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhead PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_outlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_outlin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_fun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_configur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_configur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchaput (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchaput PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzhuqe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhuqe PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_keith (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_keith PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjprior (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjprior PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_alvisi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_alvisi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodetzioni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodetzioni PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxingang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxingang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlate (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlate PRIMARY KEY (linkid)
);
CREATE TABLE has_thing (
page VARCHAR(32)
, CONSTRAINT pk_has_thing PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodrouter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrouter PRIMARY KEY (linkid)
);
CREATE TABLE has_apr (
page VARCHAR(32)
, CONSTRAINT pk_has_apr PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodformat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodformat PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodka (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodka PRIMARY KEY (linkid)
);
CREATE TABLE has_accept (
page VARCHAR(32)
, CONSTRAINT pk_has_accept PRIMARY KEY (page)
);
CREATE TABLE has_anchor_craig (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_craig PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinternet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinternet PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodelectron (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodelectron PRIMARY KEY (linkid)
);
CREATE TABLE has_sep (
page VARCHAR(32)
, CONSTRAINT pk_has_sep PRIMARY KEY (page)
);
CREATE TABLE has_anchor_miikkulainen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_miikkulainen PRIMARY KEY (linkid)
);
CREATE TABLE has_theori (
page VARCHAR(32)
, CONSTRAINT pk_has_theori PRIMARY KEY (page)
);
CREATE TABLE has_frame (
page VARCHAR(32)
, CONSTRAINT pk_has_frame PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddesign (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddesign PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodturner (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodturner PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_qr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_qr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmaintain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmaintain PRIMARY KEY (linkid)
);
CREATE TABLE has_represent (
page VARCHAR(32)
, CONSTRAINT pk_has_represent PRIMARY KEY (page)
);
CREATE TABLE has_anchor_kei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_quot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_quot PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddescript (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddescript PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_float (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_float PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzhu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhu PRIMARY KEY (linkid)
);
CREATE TABLE has_id (
page VARCHAR(32)
, CONSTRAINT pk_has_id PRIMARY KEY (page)
);
CREATE TABLE has_understand (
page VARCHAR(32)
, CONSTRAINT pk_has_understand PRIMARY KEY (page)
);
CREATE TABLE has_anchor_class (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_class PRIMARY KEY (linkid)
);
CREATE TABLE has_ii (
page VARCHAR(32)
, CONSTRAINT pk_has_ii PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodndale (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodndale PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_christian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_christian PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodschool (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodschool PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodspin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodspin PRIMARY KEY (linkid)
);
CREATE TABLE has_nbsp (
page VARCHAR(32)
, CONSTRAINT pk_has_nbsp PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodexplor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexplor PRIMARY KEY (linkid)
);
CREATE TABLE has_secur (
page VARCHAR(32)
, CONSTRAINT pk_has_secur PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmake (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmake PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodng (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodng PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pardo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pardo PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_url (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_url PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsafeti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsafeti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_academ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_academ PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodweld (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodweld PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_vijaykumar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vijaykumar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchao PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbuilt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbuilt PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlandrum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlandrum PRIMARY KEY (linkid)
);
CREATE TABLE has_sun (
page VARCHAR(32)
, CONSTRAINT pk_has_sun PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodanonym (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanonym PRIMARY KEY (linkid)
);
CREATE TABLE has_sum (
page VARCHAR(32)
, CONSTRAINT pk_has_sum PRIMARY KEY (page)
);
CREATE TABLE has_anchor_quizz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_quizz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_area (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_area PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcommun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcommun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sieg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sieg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmetacrawl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmetacrawl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnuen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnuen PRIMARY KEY (linkid)
);
CREATE TABLE has_david (
page VARCHAR(32)
, CONSTRAINT pk_has_david PRIMARY KEY (page)
);
CREATE TABLE has_circuit (
page VARCHAR(32)
, CONSTRAINT pk_has_circuit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgif (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgif PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_seminar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_seminar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mathemat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mathemat PRIMARY KEY (linkid)
);
CREATE TABLE has_user (
page VARCHAR(32)
, CONSTRAINT pk_has_user PRIMARY KEY (page)
);
CREATE TABLE has_anchor_travel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_travel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzchen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzchen PRIMARY KEY (linkid)
);
CREATE TABLE has_good (
page VARCHAR(32)
, CONSTRAINT pk_has_good PRIMARY KEY (page)
);
CREATE TABLE has_box (
page VARCHAR(32)
, CONSTRAINT pk_has_box PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpeopl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpeopl PRIMARY KEY (linkid)
);
CREATE TABLE has_offer (
page VARCHAR(32)
, CONSTRAINT pk_has_offer PRIMARY KEY (page)
);
CREATE TABLE has_server (
page VARCHAR(32)
, CONSTRAINT pk_has_server PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprocess (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprocess PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_emin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_emin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_facil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_facil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmember (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmember PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmodifi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmodifi PRIMARY KEY (linkid)
);
CREATE TABLE has_year (
page VARCHAR(32)
, CONSTRAINT pk_has_year PRIMARY KEY (page)
);
CREATE TABLE has_lot (
page VARCHAR(32)
, CONSTRAINT pk_has_lot PRIMARY KEY (page)
);
CREATE TABLE has_anchor_coe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_coe PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsoftbot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsoftbot PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_report (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_report PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlorenzo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlorenzo PRIMARY KEY (linkid)
);
CREATE TABLE has_index (
page VARCHAR(32)
, CONSTRAINT pk_has_index PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodssinha (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodssinha PRIMARY KEY (linkid)
);
CREATE TABLE has_log (
page VARCHAR(32)
, CONSTRAINT pk_has_log PRIMARY KEY (page)
);
CREATE TABLE has_anchor_rout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_rout PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfussell (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfussell PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_user (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_user PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhardin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhardin PRIMARY KEY (linkid)
);
CREATE TABLE has_network (
page VARCHAR(32)
, CONSTRAINT pk_has_network PRIMARY KEY (page)
);
CREATE TABLE has_anchor_recent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_recent PRIMARY KEY (linkid)
);
CREATE TABLE has_kei (
page VARCHAR(32)
, CONSTRAINT pk_has_kei PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddiz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddiz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_auml (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_auml PRIMARY KEY (linkid)
);
CREATE TABLE has_top (
page VARCHAR(32)
, CONSTRAINT pk_has_top PRIMARY KEY (page)
);
CREATE TABLE has_enter (
page VARCHAR(32)
, CONSTRAINT pk_has_enter PRIMARY KEY (page)
);
CREATE TABLE has_vol (
page VARCHAR(32)
, CONSTRAINT pk_has_vol PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbenjamin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbenjamin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstudent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstudent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_algorithm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_algorithm PRIMARY KEY (linkid)
);
CREATE TABLE has_long (
page VARCHAR(32)
, CONSTRAINT pk_has_long PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddaniel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddaniel PRIMARY KEY (linkid)
);
CREATE TABLE has_input (
page VARCHAR(32)
, CONSTRAINT pk_has_input PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodkincaid (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkincaid PRIMARY KEY (linkid)
);
CREATE TABLE has_side (
page VARCHAR(32)
, CONSTRAINT pk_has_side PRIMARY KEY (page)
);
CREATE TABLE has_anchor_featur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_featur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoct PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_carl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_carl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_applet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_applet PRIMARY KEY (linkid)
);
CREATE TABLE has_close (
page VARCHAR(32)
, CONSTRAINT pk_has_close PRIMARY KEY (page)
);
CREATE TABLE has_proof (
page VARCHAR(32)
, CONSTRAINT pk_has_proof PRIMARY KEY (page)
);
CREATE TABLE has_anchor_jame (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jame PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddue (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddue PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhyanbin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhyanbin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooderkok (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooderkok PRIMARY KEY (linkid)
);
CREATE TABLE has_featur (
page VARCHAR(32)
, CONSTRAINT pk_has_featur PRIMARY KEY (page)
);
CREATE TABLE has_wed (
page VARCHAR(32)
, CONSTRAINT pk_has_wed PRIMARY KEY (page)
);
CREATE TABLE has_web (
page VARCHAR(32)
, CONSTRAINT pk_has_web PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprashant (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprashant PRIMARY KEY (linkid)
);
CREATE TABLE has_engin (
page VARCHAR(32)
, CONSTRAINT pk_has_engin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsinha (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsinha PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodimplem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodimplem PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodruntim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodruntim PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_return (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_return PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_parallel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_parallel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_paulb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_paulb PRIMARY KEY (linkid)
);
CREATE TABLE has_virtual (
page VARCHAR(32)
, CONSTRAINT pk_has_virtual PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddepart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddepart PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcarl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcarl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgajit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgajit PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_program (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_program PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kaxira (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kaxira PRIMARY KEY (linkid)
);
CREATE TABLE has_expect (
page VARCHAR(32)
, CONSTRAINT pk_has_expect PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmulti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmulti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_homework (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_homework PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_semest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_semest PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_nathan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_nathan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_music (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_music PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_meet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_meet PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_interest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_interest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodconstraint (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconstraint PRIMARY KEY (linkid)
);
CREATE TABLE has_decision (
page VARCHAR(32)
, CONSTRAINT pk_has_decision PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtue (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtue PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgoyal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgoyal PRIMARY KEY (linkid)
);
CREATE TABLE has_access (
page VARCHAR(32)
, CONSTRAINT pk_has_access PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsak PRIMARY KEY (linkid)
);
CREATE TABLE has_turn (
page VARCHAR(32)
, CONSTRAINT pk_has_turn PRIMARY KEY (page)
);
CREATE TABLE has_interfac (
page VARCHAR(32)
, CONSTRAINT pk_has_interfac PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodelectr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodelectr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprofil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprofil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodappli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodappli PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_richard (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_richard PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmoonei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmoonei PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddistribut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddistribut PRIMARY KEY (linkid)
);
CREATE TABLE has_logic (
page VARCHAR(32)
, CONSTRAINT pk_has_logic PRIMARY KEY (page)
);
CREATE TABLE has_design (
page VARCHAR(32)
, CONSTRAINT pk_has_design PRIMARY KEY (page)
);
CREATE TABLE has_anchor_orient (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_orient PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_simul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_simul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhuu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhuu PRIMARY KEY (linkid)
);
CREATE TABLE has_back (
page VARCHAR(32)
, CONSTRAINT pk_has_back PRIMARY KEY (page)
);
CREATE TABLE has_anchor_ph (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ph PRIMARY KEY (linkid)
);
CREATE TABLE has_wait (
page VARCHAR(32)
, CONSTRAINT pk_has_wait PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dave (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dave PRIMARY KEY (linkid)
);
CREATE TABLE has_net (
page VARCHAR(32)
, CONSTRAINT pk_has_net PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodabstract (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodabstract PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_john (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_john PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodunix (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodunix PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwashington (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwashington PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_discuss (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_discuss PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwalkerh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwalkerh PRIMARY KEY (linkid)
);
CREATE TABLE has_thesi (
page VARCHAR(32)
, CONSTRAINT pk_has_thesi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbegin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbegin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmisra (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmisra PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtechnic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtechnic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprogramm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprogramm PRIMARY KEY (linkid)
);
CREATE TABLE has_natur (
page VARCHAR(32)
, CONSTRAINT pk_has_natur PRIMARY KEY (page)
);
CREATE TABLE has_refin (
page VARCHAR(32)
, CONSTRAINT pk_has_refin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodabraham (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodabraham PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddavid (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddavid PRIMARY KEY (linkid)
);
CREATE TABLE has_talk (
page VARCHAR(32)
, CONSTRAINT pk_has_talk PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodxue (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxue PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodend PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtutori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtutori PRIMARY KEY (linkid)
);
CREATE TABLE has_acm (
page VARCHAR(32)
, CONSTRAINT pk_has_acm PRIMARY KEY (page)
);
CREATE TABLE has_full (
page VARCHAR(32)
, CONSTRAINT pk_has_full PRIMARY KEY (page)
);
CREATE TABLE has_materi (
page VARCHAR(32)
, CONSTRAINT pk_has_materi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodson PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodharrick (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodharrick PRIMARY KEY (linkid)
);
CREATE TABLE has_qualit (
page VARCHAR(32)
, CONSTRAINT pk_has_qualit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodausland (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodausland PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_todd (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_todd PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsampl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsampl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhandout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhandout PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstaff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstaff PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlearn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlearn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_benjamin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_benjamin PRIMARY KEY (linkid)
);
CREATE TABLE has_express (
page VARCHAR(32)
, CONSTRAINT pk_has_express PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsearch (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsearch PRIMARY KEY (linkid)
);
CREATE TABLE has_line (
page VARCHAR(32)
, CONSTRAINT pk_has_line PRIMARY KEY (page)
);
CREATE TABLE has_introduct (
page VARCHAR(32)
, CONSTRAINT pk_has_introduct PRIMARY KEY (page)
);
CREATE TABLE has_java (
page VARCHAR(32)
, CONSTRAINT pk_has_java PRIMARY KEY (page)
);
CREATE TABLE has_anchor_daniel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_daniel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_texa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_texa PRIMARY KEY (linkid)
);
CREATE TABLE has_resourc (
page VARCHAR(32)
, CONSTRAINT pk_has_resourc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodchri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchri PRIMARY KEY (linkid)
);
CREATE TABLE has_street (
page VARCHAR(32)
, CONSTRAINT pk_has_street PRIMARY KEY (page)
);
CREATE TABLE has_anchor_text (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_text PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ftp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ftp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsite (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsite PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodconfer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconfer PRIMARY KEY (linkid)
);
CREATE TABLE has_credit (
page VARCHAR(32)
, CONSTRAINT pk_has_credit PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dyer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dyer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodapr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodapr PRIMARY KEY (linkid)
);
CREATE TABLE has_model (
page VARCHAR(32)
, CONSTRAINT pk_has_model PRIMARY KEY (page)
);
CREATE TABLE has_scheme (
page VARCHAR(32)
, CONSTRAINT pk_has_scheme PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwood (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwood PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gareth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gareth PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoptim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoptim PRIMARY KEY (linkid)
);
CREATE TABLE has_defin (
page VARCHAR(32)
, CONSTRAINT pk_has_defin PRIMARY KEY (page)
);
CREATE TABLE has_anchor_wisc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wisc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dwip (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dwip PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodesra (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodesra PRIMARY KEY (linkid)
);
CREATE TABLE has_loop (
page VARCHAR(32)
, CONSTRAINT pk_has_loop PRIMARY KEY (page)
);
CREATE TABLE has_wide (
page VARCHAR(32)
, CONSTRAINT pk_has_wide PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodoguer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoguer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcynthia (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcynthia PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wyl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wyl PRIMARY KEY (linkid)
);
CREATE TABLE has_center (
page VARCHAR(32)
, CONSTRAINT pk_has_center PRIMARY KEY (page)
);
CREATE TABLE has_program (
page VARCHAR(32)
, CONSTRAINT pk_has_program PRIMARY KEY (page)
);
CREATE TABLE has_anchor_grove (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_grove PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwilson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwilson PRIMARY KEY (linkid)
);
CREATE TABLE has_train (
page VARCHAR(32)
, CONSTRAINT pk_has_train PRIMARY KEY (page)
);
CREATE TABLE has_anchor_learn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_learn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcontrol (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcontrol PRIMARY KEY (linkid)
);
CREATE TABLE has_amp (
page VARCHAR(32)
, CONSTRAINT pk_has_amp PRIMARY KEY (page)
);
CREATE TABLE has_hw (
page VARCHAR(32)
, CONSTRAINT pk_has_hw PRIMARY KEY (page)
);
CREATE TABLE has_function (
page VARCHAR(32)
, CONSTRAINT pk_has_function PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodturnidg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodturnidg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsize (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsize PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhaosun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhaosun PRIMARY KEY (linkid)
);
CREATE TABLE has_oper (
page VARCHAR(32)
, CONSTRAINT pk_has_oper PRIMARY KEY (page)
);
CREATE TABLE has_anchor_late (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_late PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemilio (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemilio PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_includ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_includ PRIMARY KEY (linkid)
);
CREATE TABLE link_to (
linkid VARCHAR(32),
page0 VARCHAR(32),
page1 VARCHAR(32)
, CONSTRAINT pk_link_to PRIMARY KEY (linkid,page0,page1)
);
CREATE TABLE has_sourc (
page VARCHAR(32)
, CONSTRAINT pk_has_sourc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodma (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodma PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_oop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_oop PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddegre (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddegre PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrichard (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrichard PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_resourc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_resourc PRIMARY KEY (linkid)
);
CREATE TABLE has_tuesdai (
page VARCHAR(32)
, CONSTRAINT pk_has_tuesdai PRIMARY KEY (page)
);
CREATE TABLE has_anchor_benchmark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_benchmark PRIMARY KEY (linkid)
);
CREATE TABLE has_physic (
page VARCHAR(32)
, CONSTRAINT pk_has_physic PRIMARY KEY (page)
);
CREATE TABLE has_anchor_anna (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_anna PRIMARY KEY (linkid)
);
CREATE TABLE has_result (
page VARCHAR(32)
, CONSTRAINT pk_has_result PRIMARY KEY (page)
);
CREATE TABLE has_anchor_paradyn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_paradyn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_etzioni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_etzioni PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_import (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_import PRIMARY KEY (linkid)
);
CREATE TABLE has_librari (
page VARCHAR(32)
, CONSTRAINT pk_has_librari PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodemin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodneeraj (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodneeraj PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mukherje (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mukherje PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodautom (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodautom PRIMARY KEY (linkid)
);
CREATE TABLE has_manual (
page VARCHAR(32)
, CONSTRAINT pk_has_manual PRIMARY KEY (page)
);
CREATE TABLE has_command (
page VARCHAR(32)
, CONSTRAINT pk_has_command PRIMARY KEY (page)
);
CREATE TABLE has_anchor_video (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_video PRIMARY KEY (linkid)
);
CREATE TABLE has_thursdai (
page VARCHAR(32)
, CONSTRAINT pk_has_thursdai PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbrien (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbrien PRIMARY KEY (linkid)
);
CREATE TABLE has_grade (
page VARCHAR(32)
, CONSTRAINT pk_has_grade PRIMARY KEY (page)
);
CREATE TABLE has_anchor_philipos (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_philipos PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_institut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_institut PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodextend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodextend PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_grail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_grail PRIMARY KEY (linkid)
);
CREATE TABLE has_order (
page VARCHAR(32)
, CONSTRAINT pk_has_order PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodextens (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodextens PRIMARY KEY (linkid)
);
CREATE TABLE has_end (
page VARCHAR(32)
, CONSTRAINT pk_has_end PRIMARY KEY (page)
);
CREATE TABLE has_anchor_view (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_view PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsriram (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsriram PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcnchu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcnchu PRIMARY KEY (linkid)
);
CREATE TABLE has_final (
page VARCHAR(32)
, CONSTRAINT pk_has_final PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhour (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhour PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cours (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cours PRIMARY KEY (linkid)
);
CREATE TABLE has_free (
page VARCHAR(32)
, CONSTRAINT pk_has_free PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinclud (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinclud PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsammi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsammi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodorient (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodorient PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxguo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxguo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkakkad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkakkad PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_madison (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_madison PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodqpt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqpt PRIMARY KEY (linkid)
);
CREATE TABLE has_slide (
page VARCHAR(32)
, CONSTRAINT pk_has_slide PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfinger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfinger PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhardwar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhardwar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_martym (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_martym PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_introduct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_introduct PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_half (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_half PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinform (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinform PRIMARY KEY (linkid)
);
CREATE TABLE has_sign (
page VARCHAR(32)
, CONSTRAINT pk_has_sign PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprof (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprof PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyee (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyee PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddirect (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddirect PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_run (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_run PRIMARY KEY (linkid)
);
CREATE TABLE has_cycl (
page VARCHAR(32)
, CONSTRAINT pk_has_cycl PRIMARY KEY (page)
);
CREATE TABLE has_anchor_artifici (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_artifici PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrout PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pai PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_articl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_articl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfaculti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfaculti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_select (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_select PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ed (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ed PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_process (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_process PRIMARY KEY (linkid)
);
CREATE TABLE has_arrai (
page VARCHAR(32)
, CONSTRAINT pk_has_arrai PRIMARY KEY (page)
);
CREATE TABLE has_anchor_lee (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lee PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodestlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodestlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodindividu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodindividu PRIMARY KEY (linkid)
);
CREATE TABLE has_visual (
page VARCHAR(32)
, CONSTRAINT pk_has_visual PRIMARY KEY (page)
);
CREATE TABLE has_articl (
page VARCHAR(32)
, CONSTRAINT pk_has_articl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodseligman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodseligman PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_visitor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_visitor PRIMARY KEY (linkid)
);
CREATE TABLE has_identifi (
page VARCHAR(32)
, CONSTRAINT pk_has_identifi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtarafdar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtarafdar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_http (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_http PRIMARY KEY (linkid)
);
CREATE TABLE has_term (
page VARCHAR(32)
, CONSTRAINT pk_has_term PRIMARY KEY (page)
);
CREATE TABLE has_dept (
page VARCHAR(32)
, CONSTRAINT pk_has_dept PRIMARY KEY (page)
);
CREATE TABLE has_product (
page VARCHAR(32)
, CONSTRAINT pk_has_product PRIMARY KEY (page)
);
CREATE TABLE has_anchor_multiscalar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_multiscalar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_queri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_queri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mcmurchi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mcmurchi PRIMARY KEY (linkid)
);
CREATE TABLE has_iter (
page VARCHAR(32)
, CONSTRAINT pk_has_iter PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtechniqu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtechniqu PRIMARY KEY (linkid)
);
CREATE TABLE has_comment (
page VARCHAR(32)
, CONSTRAINT pk_has_comment PRIMARY KEY (page)
);
CREATE TABLE has_art (
page VARCHAR(32)
, CONSTRAINT pk_has_art PRIMARY KEY (page)
);
CREATE TABLE has_spring (
page VARCHAR(32)
, CONSTRAINT pk_has_spring PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbenchmark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbenchmark PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodeduc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodeduc PRIMARY KEY (linkid)
);
CREATE TABLE has_item (
page VARCHAR(32)
, CONSTRAINT pk_has_item PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodseminar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodseminar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhenri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhenri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_friend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_friend PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhqliu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhqliu PRIMARY KEY (linkid)
);
CREATE TABLE has_univers (
page VARCHAR(32)
, CONSTRAINT pk_has_univers PRIMARY KEY (page)
);
CREATE TABLE has_anchor_map (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_map PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlivni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlivni PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmachin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmachin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_van (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_van PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnote (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnote PRIMARY KEY (linkid)
);
CREATE TABLE has_cost (
page VARCHAR(32)
, CONSTRAINT pk_has_cost PRIMARY KEY (page)
);
CREATE TABLE has_graphic (
page VARCHAR(32)
, CONSTRAINT pk_has_graphic PRIMARY KEY (page)
);
CREATE TABLE has_anchor_polici (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_polici PRIMARY KEY (linkid)
);
CREATE TABLE has_code (
page VARCHAR(32)
, CONSTRAINT pk_has_code PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodaustin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaustin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodarchitectur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodarchitectur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcarruth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcarruth PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodenjoi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodenjoi PRIMARY KEY (linkid)
);
CREATE TABLE has_decemb (
page VARCHAR(32)
, CONSTRAINT pk_has_decemb PRIMARY KEY (page)
);
CREATE TABLE has_optim (
page VARCHAR(32)
, CONSTRAINT pk_has_optim PRIMARY KEY (page)
);
CREATE TABLE has_anchor_vita (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vita PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bio (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bio PRIMARY KEY (linkid)
);
CREATE TABLE has_linear (
page VARCHAR(32)
, CONSTRAINT pk_has_linear PRIMARY KEY (page)
);
CREATE TABLE has_workshop (
page VARCHAR(32)
, CONSTRAINT pk_has_workshop PRIMARY KEY (page)
);
CREATE TABLE has_anchor_hint (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hint PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwrite (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwrite PRIMARY KEY (linkid)
);
CREATE TABLE has_common (
page VARCHAR(32)
, CONSTRAINT pk_has_common PRIMARY KEY (page)
);
CREATE TABLE has_anchor_ulf (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ulf PRIMARY KEY (linkid)
);
CREATE TABLE has_account (
page VARCHAR(32)
, CONSTRAINT pk_has_account PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dougla (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dougla PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_consortia (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_consortia PRIMARY KEY (linkid)
);
CREATE TABLE has_button (
page VARCHAR(32)
, CONSTRAINT pk_has_button PRIMARY KEY (page)
);
CREATE TABLE has_anchor_propag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_propag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddionisi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddionisi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chen PRIMARY KEY (linkid)
);
CREATE TABLE has_editor (
page VARCHAR(32)
, CONSTRAINT pk_has_editor PRIMARY KEY (page)
);
CREATE TABLE has_anchor_read (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_read PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodindustri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodindustri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_real (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_real PRIMARY KEY (linkid)
);
CREATE TABLE has_programm (
page VARCHAR(32)
, CONSTRAINT pk_has_programm PRIMARY KEY (page)
);
CREATE TABLE has_anchor_hong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hong PRIMARY KEY (linkid)
);
CREATE TABLE has_depart (
page VARCHAR(32)
, CONSTRAINT pk_has_depart PRIMARY KEY (page)
);
CREATE TABLE has_int (
page VARCHAR(32)
, CONSTRAINT pk_has_int PRIMARY KEY (page)
);
CREATE TABLE has_anchor_teamweb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_teamweb PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzhiy (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhiy PRIMARY KEY (linkid)
);
CREATE TABLE has_locat (
page VARCHAR(32)
, CONSTRAINT pk_has_locat PRIMARY KEY (page)
);
CREATE TABLE has_knowledg (
page VARCHAR(32)
, CONSTRAINT pk_has_knowledg PRIMARY KEY (page)
);
CREATE TABLE has_perform (
page VARCHAR(32)
, CONSTRAINT pk_has_perform PRIMARY KEY (page)
);
CREATE TABLE has_summer (
page VARCHAR(32)
, CONSTRAINT pk_has_summer PRIMARY KEY (page)
);
CREATE TABLE has_bit (
page VARCHAR(32)
, CONSTRAINT pk_has_bit PRIMARY KEY (page)
);
CREATE TABLE has_task (
page VARCHAR(32)
, CONSTRAINT pk_has_task PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooderdem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooderdem PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbuild (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbuild PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_schoina (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_schoina PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddane (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddane PRIMARY KEY (linkid)
);
CREATE TABLE has_copyright (
page VARCHAR(32)
, CONSTRAINT pk_has_copyright PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmittal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmittal PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_andrew (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_andrew PRIMARY KEY (linkid)
);
CREATE TABLE has_ed (
page VARCHAR(32)
, CONSTRAINT pk_has_ed PRIMARY KEY (page)
);
CREATE TABLE has_ec (
page VARCHAR(32)
, CONSTRAINT pk_has_ec PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodnimar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnimar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpage (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpage PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_complementar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_complementar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodline (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodline PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnathan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnathan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodutc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodutc PRIMARY KEY (linkid)
);
CREATE TABLE has_string (
page VARCHAR(32)
, CONSTRAINT pk_has_string PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwind (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwind PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodorder (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodorder PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_errata (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_errata PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwong PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddept (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddept PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnell (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnell PRIMARY KEY (linkid)
);
CREATE TABLE has_current (
page VARCHAR(32)
, CONSTRAINT pk_has_current PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodkernel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkernel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_moshovo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_moshovo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwalbourn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwalbourn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemmawu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemmawu PRIMARY KEY (linkid)
);
CREATE TABLE has_low (
page VARCHAR(32)
, CONSTRAINT pk_has_low PRIMARY KEY (page)
);
CREATE TABLE has_anchor_arithmet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_arithmet PRIMARY KEY (linkid)
);
CREATE TABLE has_partial (
page VARCHAR(32)
, CONSTRAINT pk_has_partial PRIMARY KEY (page)
);
CREATE TABLE has_anchor_guangshun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_guangshun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_geoff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_geoff PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cool (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cool PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_collabor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_collabor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dissert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dissert PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodobtain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodobtain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyschoe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyschoe PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jonathan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jonathan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjim PRIMARY KEY (linkid)
);
CREATE TABLE has_interest (
page VARCHAR(32)
, CONSTRAINT pk_has_interest PRIMARY KEY (page)
);
CREATE TABLE has_point (
page VARCHAR(32)
, CONSTRAINT pk_has_point PRIMARY KEY (page)
);
CREATE TABLE has_move (
page VARCHAR(32)
, CONSTRAINT pk_has_move PRIMARY KEY (page)
);
CREATE TABLE has_octob (
page VARCHAR(32)
, CONSTRAINT pk_has_octob PRIMARY KEY (page)
);
CREATE TABLE has_movi (
page VARCHAR(32)
, CONSTRAINT pk_has_movi PRIMARY KEY (page)
);
CREATE TABLE has_leav (
page VARCHAR(32)
, CONSTRAINT pk_has_leav PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddsb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddsb PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjthoma (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjthoma PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlakshmi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlakshmi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodluo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodluo PRIMARY KEY (linkid)
);
CREATE TABLE has_offic (
page VARCHAR(32)
, CONSTRAINT pk_has_offic PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodunderstand (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodunderstand PRIMARY KEY (linkid)
);
CREATE TABLE has_servic (
page VARCHAR(32)
, CONSTRAINT pk_has_servic PRIMARY KEY (page)
);
CREATE TABLE has_exist (
page VARCHAR(32)
, CONSTRAINT pk_has_exist PRIMARY KEY (page)
);
CREATE TABLE has_local (
page VARCHAR(32)
, CONSTRAINT pk_has_local PRIMARY KEY (page)
);
CREATE TABLE has_anchor_mark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mark PRIMARY KEY (linkid)
);
CREATE TABLE has_action (
page VARCHAR(32)
, CONSTRAINT pk_has_action PRIMARY KEY (page)
);
CREATE TABLE has_present (
page VARCHAR(32)
, CONSTRAINT pk_has_present PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodworkshop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodworkshop PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodll (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodll PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlo PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_movi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_movi PRIMARY KEY (linkid)
);
CREATE TABLE has_requir (
page VARCHAR(32)
, CONSTRAINT pk_has_requir PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodassoci (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodassoci PRIMARY KEY (linkid)
);
CREATE TABLE has_finish (
page VARCHAR(32)
, CONSTRAINT pk_has_finish PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgrant (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgrant PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbit PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodproceed (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodproceed PRIMARY KEY (linkid)
);
CREATE TABLE has_extens (
page VARCHAR(32)
, CONSTRAINT pk_has_extens PRIMARY KEY (page)
);
CREATE TABLE has_activ (
page VARCHAR(32)
, CONSTRAINT pk_has_activ PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlt PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_qpt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_qpt PRIMARY KEY (linkid)
);
CREATE TABLE has_singl (
page VARCHAR(32)
, CONSTRAINT pk_has_singl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodexpress (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexpress PRIMARY KEY (linkid)
);
CREATE TABLE has_download (
page VARCHAR(32)
, CONSTRAINT pk_has_download PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodknowledg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodknowledg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodutexa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodutexa PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shailesh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shailesh PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddec (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddec PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnumber (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnumber PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmcquesten (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmcquesten PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodarchiv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodarchiv PRIMARY KEY (linkid)
);
CREATE TABLE has_site (
page VARCHAR(32)
, CONSTRAINT pk_has_site PRIMARY KEY (page)
);
CREATE TABLE has_run (
page VARCHAR(32)
, CONSTRAINT pk_has_run PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodotuomagi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodotuomagi PRIMARY KEY (linkid)
);
CREATE TABLE has_exam (
page VARCHAR(32)
, CONSTRAINT pk_has_exam PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodacm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodacm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodneerajm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodneerajm PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_catalog (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_catalog PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnaughton (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnaughton PRIMARY KEY (linkid)
);
CREATE TABLE has_format (
page VARCHAR(32)
, CONSTRAINT pk_has_format PRIMARY KEY (page)
);
CREATE TABLE has_multipl (
page VARCHAR(32)
, CONSTRAINT pk_has_multipl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodunicron (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodunicron PRIMARY KEY (linkid)
);
CREATE TABLE has_extend (
page VARCHAR(32)
, CONSTRAINT pk_has_extend PRIMARY KEY (page)
);
CREATE TABLE has_file (
page VARCHAR(32)
, CONSTRAINT pk_has_file PRIMARY KEY (page)
);
CREATE TABLE has_anchor_detail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_detail PRIMARY KEY (linkid)
);
CREATE TABLE has_definit (
page VARCHAR(32)
, CONSTRAINT pk_has_definit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodserver (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodserver PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodput (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodput PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_date (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_date PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_data (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_data PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_steel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_steel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrajaram (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrajaram PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbiologi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbiologi PRIMARY KEY (linkid)
);
CREATE TABLE has_bug (
page VARCHAR(32)
, CONSTRAINT pk_has_bug PRIMARY KEY (page)
);
CREATE TABLE has_processor (
page VARCHAR(32)
, CONSTRAINT pk_has_processor PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsamuel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsamuel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooduser (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooduser PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_optim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_optim PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kanpur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kanpur PRIMARY KEY (linkid)
);
CREATE TABLE has_consult (
page VARCHAR(32)
, CONSTRAINT pk_has_consult PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodequip (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodequip PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodharker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodharker PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcomment (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcomment PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwritten (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwritten PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbarn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbarn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lopez (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lopez PRIMARY KEY (linkid)
);
CREATE TABLE has_tool (
page VARCHAR(32)
, CONSTRAINT pk_has_tool PRIMARY KEY (page)
);
CREATE TABLE has_anchor_brian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_brian PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hassoun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hassoun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgordon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgordon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodintellig (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodintellig PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbasic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbasic PRIMARY KEY (linkid)
);
CREATE TABLE has_practic (
page VARCHAR(32)
, CONSTRAINT pk_has_practic PRIMARY KEY (page)
);
CREATE TABLE has_request (
page VARCHAR(32)
, CONSTRAINT pk_has_request PRIMARY KEY (page)
);
CREATE TABLE has_collect (
page VARCHAR(32)
, CONSTRAINT pk_has_collect PRIMARY KEY (page)
);
CREATE TABLE has_anchor_fountain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fountain PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_credit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_credit PRIMARY KEY (linkid)
);
CREATE TABLE has_team (
page VARCHAR(32)
, CONSTRAINT pk_has_team PRIMARY KEY (page)
);
CREATE TABLE has_anchor_chamberlain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chamberlain PRIMARY KEY (linkid)
);
CREATE TABLE has_laboratori (
page VARCHAR(32)
, CONSTRAINT pk_has_laboratori PRIMARY KEY (page)
);
CREATE TABLE has_anchor_constraint (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_constraint PRIMARY KEY (linkid)
);
CREATE TABLE has_letter (
page VARCHAR(32)
, CONSTRAINT pk_has_letter PRIMARY KEY (page)
);
CREATE TABLE has_anchor_alumni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_alumni PRIMARY KEY (linkid)
);
CREATE TABLE has_unit (
page VARCHAR(32)
, CONSTRAINT pk_has_unit PRIMARY KEY (page)
);
CREATE TABLE has_unix (
page VARCHAR(32)
, CONSTRAINT pk_has_unix PRIMARY KEY (page)
);
CREATE TABLE has_path (
page VARCHAR(32)
, CONSTRAINT pk_has_path PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfriend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfriend PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtao PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gnu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gnu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsunghe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsunghe PRIMARY KEY (linkid)
);
CREATE TABLE has_professor (
page VARCHAR(32)
, CONSTRAINT pk_has_professor PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlaboratori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlaboratori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_siff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_siff PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodapplet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodapplet PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_research (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_research PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_da (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_da PRIMARY KEY (linkid)
);
CREATE TABLE has_advanc (
page VARCHAR(32)
, CONSTRAINT pk_has_advanc PRIMARY KEY (page)
);
CREATE TABLE has_anchor_theori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_theori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_de (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_de PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_zimmermann (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_zimmermann PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmari PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodscott (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodscott PRIMARY KEY (linkid)
);
CREATE TABLE has_messag (
page VARCHAR(32)
, CONSTRAINT pk_has_messag PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodberger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodberger PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsimon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsimon PRIMARY KEY (linkid)
);
CREATE TABLE has_cern (
page VARCHAR(32)
, CONSTRAINT pk_has_cern PRIMARY KEY (page)
);
CREATE TABLE has_environ (
page VARCHAR(32)
, CONSTRAINT pk_has_environ PRIMARY KEY (page)
);
CREATE TABLE has_produc (
page VARCHAR(32)
, CONSTRAINT pk_has_produc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodoverview (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoverview PRIMARY KEY (linkid)
);
CREATE TABLE has_shore (
page VARCHAR(32)
, CONSTRAINT pk_has_shore PRIMARY KEY (page)
);
CREATE TABLE has_mechan (
page VARCHAR(32)
, CONSTRAINT pk_has_mechan PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodconsortia (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconsortia PRIMARY KEY (linkid)
);
CREATE TABLE has_short (
page VARCHAR(32)
, CONSTRAINT pk_has_short PRIMARY KEY (page)
);
CREATE TABLE has_anchor_martin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_martin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwerth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwerth PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodart PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_harker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_harker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_guestbook (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_guestbook PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_opal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_opal PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmeasur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmeasur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnorman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnorman PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmail PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchjwang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchjwang PRIMARY KEY (linkid)
);
CREATE TABLE has_graduat (
page VARCHAR(32)
, CONSTRAINT pk_has_graduat PRIMARY KEY (page)
);
CREATE TABLE has_anchor_uniti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_uniti PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtime (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtime PRIMARY KEY (linkid)
);
CREATE TABLE has_distribut (
page VARCHAR(32)
, CONSTRAINT pk_has_distribut PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodskumar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodskumar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodonlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodonlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodguri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsubramanyam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsubramanyam PRIMARY KEY (linkid)
);
CREATE TABLE has_fix (
page VARCHAR(32)
, CONSTRAINT pk_has_fix PRIMARY KEY (page)
);
CREATE TABLE has_region (
page VARCHAR(32)
, CONSTRAINT pk_has_region PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbook (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbook PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshailesh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshailesh PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodschneider (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodschneider PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxie (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxie PRIMARY KEY (linkid)
);
CREATE TABLE has_answer (
page VARCHAR(32)
, CONSTRAINT pk_has_answer PRIMARY KEY (page)
);
CREATE TABLE has_microsoft (
page VARCHAR(32)
, CONSTRAINT pk_has_microsoft PRIMARY KEY (page)
);
CREATE TABLE has_anchor_present (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_present PRIMARY KEY (linkid)
);
CREATE TABLE has_support (
page VARCHAR(32)
, CONSTRAINT pk_has_support PRIMARY KEY (page)
);
CREATE TABLE has_anchor_microsoft (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_microsoft PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ratliff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ratliff PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_darren (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_darren PRIMARY KEY (linkid)
);
CREATE TABLE has_quiz (
page VARCHAR(32)
, CONSTRAINT pk_has_quiz PRIMARY KEY (page)
);
CREATE TABLE has_school (
page VARCHAR(32)
, CONSTRAINT pk_has_school PRIMARY KEY (page)
);
CREATE TABLE has_parallel (
page VARCHAR(32)
, CONSTRAINT pk_has_parallel PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodstart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstart PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_index (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_index PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodruwei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodruwei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tutor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tutor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_favorit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_favorit PRIMARY KEY (linkid)
);
CREATE TABLE has_formal (
page VARCHAR(32)
, CONSTRAINT pk_has_formal PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprzemyslaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprzemyslaw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlead (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlead PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_stefano (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_stefano PRIMARY KEY (linkid)
);
CREATE TABLE has_tutori (
page VARCHAR(32)
, CONSTRAINT pk_has_tutori PRIMARY KEY (page)
);
CREATE TABLE has_st (
page VARCHAR(32)
, CONSTRAINT pk_has_st PRIMARY KEY (page)
);
CREATE TABLE has_anchor_store (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_store PRIMARY KEY (linkid)
);
CREATE TABLE has_store (
page VARCHAR(32)
, CONSTRAINT pk_has_store PRIMARY KEY (page)
);
CREATE TABLE has_execut (
page VARCHAR(32)
, CONSTRAINT pk_has_execut PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhinshaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhinshaw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprovid (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprovid PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmaggi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmaggi PRIMARY KEY (linkid)
);
CREATE TABLE has_direct (
page VARCHAR(32)
, CONSTRAINT pk_has_direct PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodintern (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodintern PRIMARY KEY (linkid)
);
CREATE TABLE has_utexa (
page VARCHAR(32)
, CONSTRAINT pk_has_utexa PRIMARY KEY (page)
);
CREATE TABLE has_orient (
page VARCHAR(32)
, CONSTRAINT pk_has_orient PRIMARY KEY (page)
);
CREATE TABLE has_anchor_separ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_separ PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_professor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_professor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsmall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsmall PRIMARY KEY (linkid)
);
CREATE TABLE has_multimedia (
page VARCHAR(32)
, CONSTRAINT pk_has_multimedia PRIMARY KEY (page)
);
CREATE TABLE has_link (
page VARCHAR(32)
, CONSTRAINT pk_has_link PRIMARY KEY (page)
);
CREATE TABLE has_feb (
page VARCHAR(32)
, CONSTRAINT pk_has_feb PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodworkstat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodworkstat PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_colleg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_colleg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarshall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarshall PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gener (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gener PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfinish (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfinish PRIMARY KEY (linkid)
);
CREATE TABLE has_reason (
page VARCHAR(32)
, CONSTRAINT pk_has_reason PRIMARY KEY (page)
);
CREATE TABLE has_phone (
page VARCHAR(32)
, CONSTRAINT pk_has_phone PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinterest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinterest PRIMARY KEY (linkid)
);
CREATE TABLE has_de (
page VARCHAR(32)
, CONSTRAINT pk_has_de PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtumlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtumlin PRIMARY KEY (linkid)
);
CREATE TABLE has_protocol (
page VARCHAR(32)
, CONSTRAINT pk_has_protocol PRIMARY KEY (page)
);
CREATE TABLE has_displai (
page VARCHAR(32)
, CONSTRAINT pk_has_displai PRIMARY KEY (page)
);
CREATE TABLE has_anchor_philosophi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_philosophi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_galileo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_galileo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodversion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodversion PRIMARY KEY (linkid)
);
CREATE TABLE has_dr (
page VARCHAR(32)
, CONSTRAINT pk_has_dr PRIMARY KEY (page)
);
CREATE TABLE has_anchor_bibliographi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bibliographi PRIMARY KEY (linkid)
);
CREATE TABLE has_experi (
page VARCHAR(32)
, CONSTRAINT pk_has_experi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_intellig (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_intellig PRIMARY KEY (linkid)
);
CREATE TABLE has_develop (
page VARCHAR(32)
, CONSTRAINT pk_has_develop PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhigh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhigh PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_galleri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_galleri PRIMARY KEY (linkid)
);
CREATE TABLE has_util (
page VARCHAR(32)
, CONSTRAINT pk_has_util PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgyx (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgyx PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwyle (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwyle PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_quarter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_quarter PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbase (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbase PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pub (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pub PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtarachandani (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtarachandani PRIMARY KEY (linkid)
);
CREATE TABLE has_control (
page VARCHAR(32)
, CONSTRAINT pk_has_control PRIMARY KEY (page)
);
CREATE TABLE has_form (
page VARCHAR(32)
, CONSTRAINT pk_has_form PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcorrel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcorrel PRIMARY KEY (linkid)
);
CREATE TABLE has_autom (
page VARCHAR(32)
, CONSTRAINT pk_has_autom PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodqr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqr PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shapiro (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shapiro PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_level (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_level PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcarlo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcarlo PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sourc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sourc PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtempest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtempest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarkng (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarkng PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodspecif (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodspecif PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfiuczynski (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfiuczynski PRIMARY KEY (linkid)
);
CREATE TABLE has_improv (
page VARCHAR(32)
, CONSTRAINT pk_has_improv PRIMARY KEY (page)
);
CREATE TABLE has_fri (
page VARCHAR(32)
, CONSTRAINT pk_has_fri PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjose (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjose PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodou PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchuang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchuang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ortega (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ortega PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_burger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_burger PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sohi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sohi PRIMARY KEY (linkid)
);
CREATE TABLE has_graph (
page VARCHAR(32)
, CONSTRAINT pk_has_graph PRIMARY KEY (page)
);
CREATE TABLE has_anchor_steven (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_steven PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmondai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmondai PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_appli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_appli PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjack (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjack PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_greg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_greg PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shubu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shubu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyear (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyear PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyoonsuck (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyoonsuck PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgooti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgooti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcxh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcxh PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcamahort (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcamahort PRIMARY KEY (linkid)
);
CREATE TABLE has_wednesdai (
page VARCHAR(32)
, CONSTRAINT pk_has_wednesdai PRIMARY KEY (page)
);
CREATE TABLE has_member (
page VARCHAR(32)
, CONSTRAINT pk_has_member PRIMARY KEY (page)
);
CREATE TABLE has_anchor_jbuhler (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jbuhler PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ramachandran (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ramachandran PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtechnologi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtechnologi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_suggest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_suggest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfountain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfountain PRIMARY KEY (linkid)
);
CREATE TABLE has_save (
page VARCHAR(32)
, CONSTRAINT pk_has_save PRIMARY KEY (page)
);
CREATE TABLE has_faculti (
page VARCHAR(32)
, CONSTRAINT pk_has_faculti PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodzuckerman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzuckerman PRIMARY KEY (linkid)
);
CREATE TABLE has_procedur (
page VARCHAR(32)
, CONSTRAINT pk_has_procedur PRIMARY KEY (page)
);
CREATE TABLE has_anchor_wa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wa PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lifschitz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lifschitz PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_technic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_technic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpoon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpoon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsymposium (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsymposium PRIMARY KEY (linkid)
);
CREATE TABLE has_page (
page VARCHAR(32)
, CONSTRAINT pk_has_page PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodvbb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvbb PRIMARY KEY (linkid)
);
CREATE TABLE has_confer (
page VARCHAR(32)
, CONSTRAINT pk_has_confer PRIMARY KEY (page)
);
CREATE TABLE has_anchor_jakobovit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jakobovit PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_problem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_problem PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ut PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_uw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_uw PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_risto (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_risto PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_educ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_educ PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_registrar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_registrar PRIMARY KEY (linkid)
);
CREATE TABLE has_make (
page VARCHAR(32)
, CONSTRAINT pk_has_make PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodramakrishnan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodramakrishnan PRIMARY KEY (linkid)
);
CREATE TABLE has_mathemat (
page VARCHAR(32)
, CONSTRAINT pk_has_mathemat PRIMARY KEY (page)
);
CREATE TABLE has_uw (
page VARCHAR(32)
, CONSTRAINT pk_has_uw PRIMARY KEY (page)
);
CREATE TABLE has_ut (
page VARCHAR(32)
, CONSTRAINT pk_has_ut PRIMARY KEY (page)
);
CREATE TABLE has_anchor_krishna (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_krishna PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_previou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_previou PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_person (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_person PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkistler (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkistler PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_technologi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_technologi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemail PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shult (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shult PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstamm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstamm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprior (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprior PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hill (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hill PRIMARY KEY (linkid)
);
CREATE TABLE has_syllabu (
page VARCHAR(32)
, CONSTRAINT pk_has_syllabu PRIMARY KEY (page)
);
CREATE TABLE has_anchor_window (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_window PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_stefan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_stefan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchen PRIMARY KEY (linkid)
);
CREATE TABLE has_deriv (
page VARCHAR(32)
, CONSTRAINT pk_has_deriv PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodzhou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhou PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_karlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_karlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpublic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpublic PRIMARY KEY (linkid)
);
CREATE TABLE has_degre (
page VARCHAR(32)
, CONSTRAINT pk_has_degre PRIMARY KEY (page)
);
CREATE TABLE has_approach (
page VARCHAR(32)
, CONSTRAINT pk_has_approach PRIMARY KEY (page)
);
CREATE TABLE has_refer (
page VARCHAR(32)
, CONSTRAINT pk_has_refer PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtong PRIMARY KEY (linkid)
);
CREATE TABLE has_publish (
page VARCHAR(32)
, CONSTRAINT pk_has_publish PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodform (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodform PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodexam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshma (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshma PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvlr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvlr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmaster (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmaster PRIMARY KEY (linkid)
);
CREATE TABLE has_paul (
page VARCHAR(32)
, CONSTRAINT pk_has_paul PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodalvisi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodalvisi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinstructor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinstructor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_weaver (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_weaver PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrefer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrefer PRIMARY KEY (linkid)
);
CREATE TABLE has_washington (
page VARCHAR(32)
, CONSTRAINT pk_has_washington PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgive (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgive PRIMARY KEY (linkid)
);
CREATE TABLE has_note (
page VARCHAR(32)
, CONSTRAINT pk_has_note PRIMARY KEY (page)
);
CREATE TABLE has_anchor_grant (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_grant PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_consortium (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_consortium PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_trace (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_trace PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chao PRIMARY KEY (linkid)
);
CREATE TABLE has_client (
page VARCHAR(32)
, CONSTRAINT pk_has_client PRIMARY KEY (page)
);
CREATE TABLE has_anchor_georg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_georg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtara (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtara PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmanufactur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmanufactur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hierarchi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hierarchi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddale (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddale PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodborn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodborn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodanim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanim PRIMARY KEY (linkid)
);
CREATE TABLE has_tr (
page VARCHAR(32)
, CONSTRAINT pk_has_tr PRIMARY KEY (page)
);
CREATE TABLE has_anchor_local (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_local PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_session (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_session PRIMARY KEY (linkid)
);
CREATE TABLE has_gt (
page VARCHAR(32)
, CONSTRAINT pk_has_gt PRIMARY KEY (page)
);
CREATE TABLE has_anchor_condor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_condor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_activ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_activ PRIMARY KEY (linkid)
);
CREATE TABLE has_submit (
page VARCHAR(32)
, CONSTRAINT pk_has_submit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsummer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsummer PRIMARY KEY (linkid)
);
CREATE TABLE has_session (
page VARCHAR(32)
, CONSTRAINT pk_has_session PRIMARY KEY (page)
);
CREATE TABLE has_anchor_symbol (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_symbol PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodroberto (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodroberto PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wart PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_centuri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_centuri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooduniti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooduniti PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_homepag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_homepag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodclsy (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodclsy PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfaq (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfaq PRIMARY KEY (linkid)
);
CREATE TABLE has_postscript (
page VARCHAR(32)
, CONSTRAINT pk_has_postscript PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodanderson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanderson PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodreceiv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreceiv PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrajmohan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrajmohan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarku (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarku PRIMARY KEY (linkid)
);
CREATE TABLE has_score (
page VARCHAR(32)
, CONSTRAINT pk_has_score PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmarkj (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarkj PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbrown (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbrown PRIMARY KEY (linkid)
);
CREATE TABLE has_depend (
page VARCHAR(32)
, CONSTRAINT pk_has_depend PRIMARY KEY (page)
);
CREATE TABLE has_anchor_shannon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shannon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodearl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodearl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_workshop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_workshop PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tsioli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tsioli PRIMARY KEY (linkid)
);
CREATE TABLE has_alphanumeric_word (
linkid VARCHAR(32)
, CONSTRAINT pk_has_alphanumeric_word PRIMARY KEY (linkid)
);
CREATE TABLE has_live (
page VARCHAR(32)
, CONSTRAINT pk_has_live PRIMARY KEY (page)
);
CREATE TABLE has_anchor_student (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_student PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_molecular (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_molecular PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmadhukar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmadhukar PRIMARY KEY (linkid)
);
CREATE TABLE has_block (
page VARCHAR(32)
, CONSTRAINT pk_has_block PRIMARY KEY (page)
);
CREATE TABLE has_anchor_offic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_offic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodphysic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodphysic PRIMARY KEY (linkid)
);
CREATE TABLE has_complex (
page VARCHAR(32)
, CONSTRAINT pk_has_complex PRIMARY KEY (page)
);
CREATE TABLE has_anchor_advic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_advic PRIMARY KEY (linkid)
);
CREATE TABLE has_transact (
page VARCHAR(32)
, CONSTRAINT pk_has_transact PRIMARY KEY (page)
);
CREATE TABLE has_anchor_chaotic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chaotic PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chapter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chapter PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchuanjun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchuanjun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_turnidg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_turnidg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodimplement (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodimplement PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dahlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dahlin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodplaxton (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodplaxton PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sung (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sung PRIMARY KEY (linkid)
);
CREATE TABLE has_quot (
page VARCHAR(32)
, CONSTRAINT pk_has_quot PRIMARY KEY (page)
);
CREATE TABLE has_anchor_room (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_room PRIMARY KEY (linkid)
);
CREATE TABLE has_build (
page VARCHAR(32)
, CONSTRAINT pk_has_build PRIMARY KEY (page)
);
CREATE TABLE has_anchor_method (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_method PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtewari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtewari PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodadvanc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadvanc PRIMARY KEY (linkid)
);
CREATE TABLE has_suggest (
page VARCHAR(32)
, CONSTRAINT pk_has_suggest PRIMARY KEY (page)
);
CREATE TABLE has_michael (
page VARCHAR(32)
, CONSTRAINT pk_has_michael PRIMARY KEY (page)
);
CREATE TABLE has_remov (
page VARCHAR(32)
, CONSTRAINT pk_has_remov PRIMARY KEY (page)
);
CREATE TABLE has_plan (
page VARCHAR(32)
, CONSTRAINT pk_has_plan PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodexecut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexecut PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmichael (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmichael PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_frequent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_frequent PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlee (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlee PRIMARY KEY (linkid)
);
CREATE TABLE has_appli (
page VARCHAR(32)
, CONSTRAINT pk_has_appli PRIMARY KEY (page)
);
CREATE TABLE has_elem (
page VARCHAR(32)
, CONSTRAINT pk_has_elem PRIMARY KEY (page)
);
CREATE TABLE has_addition (
page VARCHAR(32)
, CONSTRAINT pk_has_addition PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodquarter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodquarter PRIMARY KEY (linkid)
);
CREATE TABLE has_info (
page VARCHAR(32)
, CONSTRAINT pk_has_info PRIMARY KEY (page)
);
CREATE TABLE has_begin (
page VARCHAR(32)
, CONSTRAINT pk_has_begin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsantanu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsantanu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodquot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodquot PRIMARY KEY (linkid)
);
CREATE TABLE has_travel (
page VARCHAR(32)
, CONSTRAINT pk_has_travel PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmethod (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmethod PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnov (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnov PRIMARY KEY (linkid)
);
CREATE TABLE has_class (
page VARCHAR(32)
, CONSTRAINT pk_has_class PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodperform (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodperform PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnatarajan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnatarajan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrelat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrelat PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodopinion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodopinion PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dailei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dailei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chilimbi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chilimbi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlong PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jack (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jack PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_webmast (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_webmast PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ta (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ta PRIMARY KEY (linkid)
);
CREATE TABLE has_copi (
page VARCHAR(32)
, CONSTRAINT pk_has_copi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_syllabu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_syllabu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjadair (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjadair PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsvkakkad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsvkakkad PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodblumof (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodblumof PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_solver (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_solver PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_autumn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_autumn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpostscript (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpostscript PRIMARY KEY (linkid)
);
CREATE TABLE has_ibm (
page VARCHAR(32)
, CONSTRAINT pk_has_ibm PRIMARY KEY (page)
);
CREATE TABLE has_head (
page VARCHAR(32)
, CONSTRAINT pk_has_head PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodaugust (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaugust PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhope (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhope PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodorigin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodorigin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodph (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodph PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpm PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_assist (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_assist PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlazowska (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlazowska PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_metacrawl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_metacrawl PRIMARY KEY (linkid)
);
CREATE TABLE has_subject (
page VARCHAR(32)
, CONSTRAINT pk_has_subject PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsourc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsourc PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvsr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvsr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodregion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodregion PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mosaic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mosaic PRIMARY KEY (linkid)
);
CREATE TABLE has_dynam (
page VARCHAR(32)
, CONSTRAINT pk_has_dynam PRIMARY KEY (page)
);
CREATE TABLE has_interact (
page VARCHAR(32)
, CONSTRAINT pk_has_interact PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodrou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrou PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_final (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_final PRIMARY KEY (linkid)
);
CREATE TABLE has_madison (
page VARCHAR(32)
, CONSTRAINT pk_has_madison PRIMARY KEY (page)
);
CREATE TABLE has_chapter (
page VARCHAR(32)
, CONSTRAINT pk_has_chapter PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodejp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodejp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtool (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtool PRIMARY KEY (linkid)
);
CREATE TABLE has_lisp (
page VARCHAR(32)
, CONSTRAINT pk_has_lisp PRIMARY KEY (page)
);
CREATE TABLE has_anchor_persist (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_persist PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mangasarian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mangasarian PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ferri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ferri PRIMARY KEY (linkid)
);
CREATE TABLE has_compar (
page VARCHAR(32)
, CONSTRAINT pk_has_compar PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodthoma (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodthoma PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtop PRIMARY KEY (linkid)
);
CREATE TABLE has_version (
page VARCHAR(32)
, CONSTRAINT pk_has_version PRIMARY KEY (page)
);
CREATE TABLE has_anchor_current (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_current PRIMARY KEY (linkid)
);
CREATE TABLE has_technologi (
page VARCHAR(32)
, CONSTRAINT pk_has_technologi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlink (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlink PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chines (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chines PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_guri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_guri PRIMARY KEY (linkid)
);
CREATE TABLE has_dec (
page VARCHAR(32)
, CONSTRAINT pk_has_dec PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodchateau (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchateau PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodliu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodliu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyong PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_note (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_note PRIMARY KEY (linkid)
);
CREATE TABLE has_richard (
page VARCHAR(32)
, CONSTRAINT pk_has_richard PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodslide (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodslide PRIMARY KEY (linkid)
);
CREATE TABLE has_level (
page VARCHAR(32)
, CONSTRAINT pk_has_level PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmajor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmajor PRIMARY KEY (linkid)
);
CREATE TABLE has_mark (
page VARCHAR(32)
, CONSTRAINT pk_has_mark PRIMARY KEY (page)
);
CREATE TABLE has_anchor_henri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_henri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_carlson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_carlson PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbednar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbednar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcoe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcoe PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_scienc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_scienc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_linden (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_linden PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_solv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_solv PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrich (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrich PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmccain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmccain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmiikkulainen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmiikkulainen PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_languag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_languag PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_netscap (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_netscap PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kaleidoscop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kaleidoscop PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_html (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_html PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sidnei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sidnei PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodarora (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodarora PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmodel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmodel PRIMARY KEY (linkid)
);
CREATE TABLE has_connect (
page VARCHAR(32)
, CONSTRAINT pk_has_connect PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpath (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpath PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_week (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_week PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_christoph (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_christoph PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_skyblu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_skyblu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrao PRIMARY KEY (linkid)
);
CREATE TABLE has_wisc (
page VARCHAR(32)
, CONSTRAINT pk_has_wisc PRIMARY KEY (page)
);
CREATE TABLE has_tx (
page VARCHAR(32)
, CONSTRAINT pk_has_tx PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dynam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dynam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsyu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsyu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyang PRIMARY KEY (linkid)
);
CREATE TABLE has_process (
page VARCHAR(32)
, CONSTRAINT pk_has_process PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddean (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddean PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_danc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_danc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dewitt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dewitt PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bjk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bjk PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmallori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmallori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ira (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ira PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshengm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshengm PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_born (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_born PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrespons (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrespons PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfree (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfree PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcreation (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcreation PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_manag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_manag PRIMARY KEY (linkid)
);
CREATE TABLE has_januari (
page VARCHAR(32)
, CONSTRAINT pk_has_januari PRIMARY KEY (page)
);
CREATE TABLE has_time (
page VARCHAR(32)
, CONSTRAINT pk_has_time PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfinal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfinal PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_seattl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_seattl PRIMARY KEY (linkid)
);
CREATE TABLE has_examin (
page VARCHAR(32)
, CONSTRAINT pk_has_examin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpolici (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpolici PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmecaliff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmecaliff PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kevin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kevin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvipin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvipin PRIMARY KEY (linkid)
);
CREATE TABLE has_load (
page VARCHAR(32)
, CONSTRAINT pk_has_load PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodykpei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodykpei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_oral (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_oral PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrepresent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrepresent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tullsen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tullsen PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodconsult (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconsult PRIMARY KEY (linkid)
);
CREATE TABLE has_meet (
page VARCHAR(32)
, CONSTRAINT pk_has_meet PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddissert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddissert PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bockrath (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bockrath PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtaught (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtaught PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfocus (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfocus PRIMARY KEY (linkid)
);
CREATE TABLE has_found (
page VARCHAR(32)
, CONSTRAINT pk_has_found PRIMARY KEY (page)
);
CREATE TABLE has_trace (
page VARCHAR(32)
, CONSTRAINT pk_has_trace PRIMARY KEY (page)
);
CREATE TABLE has_anchor_sodani (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sodani PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodisheldon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodisheldon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodconstruct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconstruct PRIMARY KEY (linkid)
);
CREATE TABLE has_record (
page VARCHAR(32)
, CONSTRAINT pk_has_record PRIMARY KEY (page)
);
CREATE TABLE has_anchor_mcauliff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mcauliff PRIMARY KEY (linkid)
);
CREATE TABLE has_email (
page VARCHAR(32)
, CONSTRAINT pk_has_email PRIMARY KEY (page)
);
CREATE TABLE has_hill (
page VARCHAR(32)
, CONSTRAINT pk_has_hill PRIMARY KEY (page)
);
CREATE TABLE has_anchor_commun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_commun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodarticl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodarticl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_qualifi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_qualifi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodteach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodteach PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_faq (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_faq PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcopi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcopi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_racquetbal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_racquetbal PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbanerje (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbanerje PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlwerth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlwerth PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shore (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shore PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_multimedia (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_multimedia PRIMARY KEY (linkid)
);
CREATE TABLE has_test (
page VARCHAR(32)
, CONSTRAINT pk_has_test PRIMARY KEY (page)
);
CREATE TABLE has_anchor_cach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cach PRIMARY KEY (linkid)
);
CREATE TABLE has_onlin (
page VARCHAR(32)
, CONSTRAINT pk_has_onlin PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodadvisor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadvisor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmadison (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmadison PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mckenzi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mckenzi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_friedman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_friedman PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodadapt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadapt PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pardyak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pardyak PRIMARY KEY (linkid)
);
CREATE TABLE has_ftp (
page VARCHAR(32)
, CONSTRAINT pk_has_ftp PRIMARY KEY (page)
);
CREATE TABLE has_mit (
page VARCHAR(32)
, CONSTRAINT pk_has_mit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodyik (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyik PRIMARY KEY (linkid)
);
CREATE TABLE has_word (
page VARCHAR(32)
, CONSTRAINT pk_has_word PRIMARY KEY (page)
);
CREATE TABLE has_work (
page VARCHAR(32)
, CONSTRAINT pk_has_work PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodoctob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoctob PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtech (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtech PRIMARY KEY (linkid)
);
CREATE TABLE has_show (
page VARCHAR(32)
, CONSTRAINT pk_has_show PRIMARY KEY (page)
);
CREATE TABLE has_anchor_administr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_administr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodunivers (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodunivers PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddynam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddynam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodresum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodresum PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodback (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodback PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrobert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrobert PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodal PRIMARY KEY (linkid)
);
CREATE TABLE has_receiv (
page VARCHAR(32)
, CONSTRAINT pk_has_receiv PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodad PRIMARY KEY (linkid)
);
CREATE TABLE has_organ (
page VARCHAR(32)
, CONSTRAINT pk_has_organ PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodphoto (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodphoto PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_breach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_breach PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddianelaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddianelaw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodclanci (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodclanci PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcollect (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcollect PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtaowang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtaowang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_machin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_machin PRIMARY KEY (linkid)
);
CREATE TABLE has_date (
page VARCHAR(32)
, CONSTRAINT pk_has_date PRIMARY KEY (page)
);
CREATE TABLE has_anchor_ii (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ii PRIMARY KEY (linkid)
);
CREATE TABLE has_rout (
page VARCHAR(32)
, CONSTRAINT pk_has_rout PRIMARY KEY (page)
);
CREATE TABLE has_anchor_lab (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lab PRIMARY KEY (linkid)
);
CREATE TABLE has_data (
page VARCHAR(32)
, CONSTRAINT pk_has_data PRIMARY KEY (page)
);
CREATE TABLE has_docum (
page VARCHAR(32)
, CONSTRAINT pk_has_docum PRIMARY KEY (page)
);
CREATE TABLE has_cpu (
page VARCHAR(32)
, CONSTRAINT pk_has_cpu PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlanc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlanc PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlane (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlane PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtullsen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtullsen PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstefan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstefan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsheldon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsheldon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsolomon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsolomon PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_turn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_turn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddiscuss (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddiscuss PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodaruna (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaruna PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmike (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmike PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ebel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ebel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_inform (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_inform PRIMARY KEY (linkid)
);
CREATE TABLE has_determin (
page VARCHAR(32)
, CONSTRAINT pk_has_determin PRIMARY KEY (page)
);
CREATE TABLE has_anchor_kenneth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kenneth PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_descript (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_descript PRIMARY KEY (linkid)
);
CREATE TABLE has_main (
page VARCHAR(32)
, CONSTRAINT pk_has_main PRIMARY KEY (page)
);
CREATE TABLE has_mail (
page VARCHAR(32)
, CONSTRAINT pk_has_mail PRIMARY KEY (page)
);
CREATE TABLE has_sequenc (
page VARCHAR(32)
, CONSTRAINT pk_has_sequenc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhiep (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhiep PRIMARY KEY (linkid)
);
CREATE TABLE has_prove (
page VARCHAR(32)
, CONSTRAINT pk_has_prove PRIMARY KEY (page)
);
CREATE TABLE has_anchor_quiz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_quiz PRIMARY KEY (linkid)
);
CREATE TABLE has_complet (
page VARCHAR(32)
, CONSTRAINT pk_has_complet PRIMARY KEY (page)
);
CREATE TABLE has_ta (
page VARCHAR(32)
, CONSTRAINT pk_has_ta PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodnamjoshi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnamjoshi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_vision (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vision PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodactiv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodactiv PRIMARY KEY (linkid)
);
CREATE TABLE has_robot (
page VARCHAR(32)
, CONSTRAINT pk_has_robot PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddetail (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddetail PRIMARY KEY (linkid)
);
CREATE TABLE has_assist (
page VARCHAR(32)
, CONSTRAINT pk_has_assist PRIMARY KEY (page)
);
CREATE TABLE has_anchor_laboratori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_laboratori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_book (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_book PRIMARY KEY (linkid)
);
CREATE TABLE has_statist (
page VARCHAR(32)
, CONSTRAINT pk_has_statist PRIMARY KEY (page)
);
CREATE TABLE has_directori (
page VARCHAR(32)
, CONSTRAINT pk_has_directori PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfeng (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfeng PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodaiji (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaiji PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_collect (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_collect PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_logic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_logic PRIMARY KEY (linkid)
);
CREATE TABLE has_home (
page VARCHAR(32)
, CONSTRAINT pk_has_home PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddate (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddate PRIMARY KEY (linkid)
);
CREATE TABLE has_jame (
page VARCHAR(32)
, CONSTRAINT pk_has_jame PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodaddress (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaddress PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtalk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtalk PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_spring (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_spring PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfunction (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfunction PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtokuda (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtokuda PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsubdirectori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsubdirectori PRIMARY KEY (linkid)
);
CREATE TABLE has_exampl (
page VARCHAR(32)
, CONSTRAINT pk_has_exampl PRIMARY KEY (page)
);
CREATE TABLE has_url (
page VARCHAR(32)
, CONSTRAINT pk_has_url PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbogomolni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbogomolni PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgener (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgener PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfoundat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfoundat PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpuchol (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpuchol PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrandom (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrandom PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtheori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtheori PRIMARY KEY (linkid)
);
CREATE TABLE has_pointer (
page VARCHAR(32)
, CONSTRAINT pk_has_pointer PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodut PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_seitz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_seitz PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodaccess (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaccess PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_boyer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_boyer PRIMARY KEY (linkid)
);
CREATE TABLE has_position (
page VARCHAR(32)
, CONSTRAINT pk_has_position PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcollabor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcollabor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_qualit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_qualit PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnguyen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnguyen PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjournal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjournal PRIMARY KEY (linkid)
);
CREATE TABLE has_purpos (
page VARCHAR(32)
, CONSTRAINT pk_has_purpos PRIMARY KEY (page)
);
CREATE TABLE has_ai (
page VARCHAR(32)
, CONSTRAINT pk_has_ai PRIMARY KEY (page)
);
CREATE TABLE has_anchor_graduat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_graduat PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_melski (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_melski PRIMARY KEY (linkid)
);
CREATE TABLE has_ad (
page VARCHAR(32)
, CONSTRAINT pk_has_ad PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodindex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodindex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstore (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstore PRIMARY KEY (linkid)
);
CREATE TABLE has_doesn (
page VARCHAR(32)
, CONSTRAINT pk_has_doesn PRIMARY KEY (page)
);
CREATE TABLE has_brian (
page VARCHAR(32)
, CONSTRAINT pk_has_brian PRIMARY KEY (page)
);
CREATE TABLE has_assum (
page VARCHAR(32)
, CONSTRAINT pk_has_assum PRIMARY KEY (page)
);
CREATE TABLE has_homework (
page VARCHAR(32)
, CONSTRAINT pk_has_homework PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwade (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwade PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshare (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshare PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodaug (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaug PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodschedul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodschedul PRIMARY KEY (linkid)
);
CREATE TABLE has_juli (
page VARCHAR(32)
, CONSTRAINT pk_has_juli PRIMARY KEY (page)
);
CREATE TABLE has_problem (
page VARCHAR(32)
, CONSTRAINT pk_has_problem PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgraphic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgraphic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcecil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcecil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwednesdai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwednesdai PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsowmya (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsowmya PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlibrari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlibrari PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_choi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_choi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodshaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodshaw PRIMARY KEY (linkid)
);
CREATE TABLE has_architectur (
page VARCHAR(32)
, CONSTRAINT pk_has_architectur PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodindian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodindian PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodarea (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodarea PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_debug (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_debug PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_chou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chou PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgupta (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgupta PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_question (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_question PRIMARY KEY (linkid)
);
CREATE TABLE has_correct (
page VARCHAR(32)
, CONSTRAINT pk_has_correct PRIMARY KEY (page)
);
CREATE TABLE has_field (
page VARCHAR(32)
, CONSTRAINT pk_has_field PRIMARY KEY (page)
);
CREATE TABLE has_anchor_wilson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wilson PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jim PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_emac (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_emac PRIMARY KEY (linkid)
);
CREATE TABLE has_jan (
page VARCHAR(32)
, CONSTRAINT pk_has_jan PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcenter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcenter PRIMARY KEY (linkid)
);
CREATE TABLE has_void (
page VARCHAR(32)
, CONSTRAINT pk_has_void PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodweaver (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodweaver PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlectur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlectur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstudy (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstudy PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoren (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoren PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_slide (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_slide PRIMARY KEY (linkid)
);
CREATE TABLE has_modifi (
page VARCHAR(32)
, CONSTRAINT pk_has_modifi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_larri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_larri PRIMARY KEY (linkid)
);
CREATE TABLE has_tue (
page VARCHAR(32)
, CONSTRAINT pk_has_tue PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodstudi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstudi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_plan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_plan PRIMARY KEY (linkid)
);
CREATE TABLE has_human (
page VARCHAR(32)
, CONSTRAINT pk_has_human PRIMARY KEY (page)
);
CREATE TABLE has_anchor_architectur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_architectur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddahlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddahlin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_line (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_line PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_baker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_baker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_link (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_link PRIMARY KEY (linkid)
);
CREATE TABLE has_theorem (
page VARCHAR(32)
, CONSTRAINT pk_has_theorem PRIMARY KEY (page)
);
CREATE TABLE has_anchor_solution (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_solution PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_center (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_center PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodconsortium (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodconsortium PRIMARY KEY (linkid)
);
CREATE TABLE has_output (
page VARCHAR(32)
, CONSTRAINT pk_has_output PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddai PRIMARY KEY (linkid)
);
CREATE TABLE has_construct (
page VARCHAR(32)
, CONSTRAINT pk_has_construct PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dewei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dewei PRIMARY KEY (linkid)
);
CREATE TABLE has_goal (
page VARCHAR(32)
, CONSTRAINT pk_has_goal PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodreflect (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreflect PRIMARY KEY (linkid)
);
CREATE TABLE has_effici (
page VARCHAR(32)
, CONSTRAINT pk_has_effici PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjbednar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjbednar PRIMARY KEY (linkid)
);
CREATE TABLE has_life (
page VARCHAR(32)
, CONSTRAINT pk_has_life PRIMARY KEY (page)
);
CREATE TABLE has_due (
page VARCHAR(32)
, CONSTRAINT pk_has_due PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmusic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmusic PRIMARY KEY (linkid)
);
CREATE TABLE has_mon (
page VARCHAR(32)
, CONSTRAINT pk_has_mon PRIMARY KEY (page)
);
CREATE TABLE has_address (
page VARCHAR(32)
, CONSTRAINT pk_has_address PRIMARY KEY (page)
);
CREATE TABLE has_basic (
page VARCHAR(32)
, CONSTRAINT pk_has_basic PRIMARY KEY (page)
);
CREATE TABLE has_anchor_experi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_experi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_prock (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_prock PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodspring (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodspring PRIMARY KEY (linkid)
);
CREATE TABLE has_call (
page VARCHAR(32)
, CONSTRAINT pk_has_call PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsawada (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsawada PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhomework (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhomework PRIMARY KEY (linkid)
);
CREATE TABLE has_multi (
page VARCHAR(32)
, CONSTRAINT pk_has_multi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_subbarao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_subbarao PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgokul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgokul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfull (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfull PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_onlin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_onlin PRIMARY KEY (linkid)
);
CREATE TABLE has_languag (
page VARCHAR(32)
, CONSTRAINT pk_has_languag PRIMARY KEY (page)
);
CREATE TABLE has_anchor_synthesi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_synthesi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_natur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_natur PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwchen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwchen PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodparallel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodparallel PRIMARY KEY (linkid)
);
CREATE TABLE has_http (
page VARCHAR(32)
, CONSTRAINT pk_has_http PRIMARY KEY (page)
);
CREATE TABLE has_anchor_visit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_visit PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_batori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_batori PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddatabas (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddatabas PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_safeti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_safeti PRIMARY KEY (linkid)
);
CREATE TABLE has_rang (
page VARCHAR(32)
, CONSTRAINT pk_has_rang PRIMARY KEY (page)
);
CREATE TABLE has_anchor_instruct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_instruct PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsavag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsavag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbrowser (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbrowser PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkenneth (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkenneth PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrajaraman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrajaraman PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbookmark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbookmark PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsystem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsystem PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jerri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jerri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_calvin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_calvin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgutierrez (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgutierrez PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_home (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_home PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcaliff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcaliff PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lampert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lampert PRIMARY KEY (linkid)
);
CREATE TABLE has_board (
page VARCHAR(32)
, CONSTRAINT pk_has_board PRIMARY KEY (page)
);
CREATE TABLE has_anchor_paper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_paper PRIMARY KEY (linkid)
);
CREATE TABLE has_condition (
page VARCHAR(32)
, CONSTRAINT pk_has_condition PRIMARY KEY (page)
);
CREATE TABLE has_usa (
page VARCHAR(32)
, CONSTRAINT pk_has_usa PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodvin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvin PRIMARY KEY (linkid)
);
CREATE TABLE has_simul (
page VARCHAR(32)
, CONSTRAINT pk_has_simul PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinstitut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinstitut PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_nautiy (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_nautiy PRIMARY KEY (linkid)
);
CREATE TABLE has_person (
page VARCHAR(32)
, CONSTRAINT pk_has_person PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodauthor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodauthor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmanual (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmanual PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dbm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dbm PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodresourc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodresourc PRIMARY KEY (linkid)
);
CREATE TABLE has_fridai (
page VARCHAR(32)
, CONSTRAINT pk_has_fridai PRIMARY KEY (page)
);
CREATE TABLE has_anchor_standard (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_standard PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ramakrishnan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ramakrishnan PRIMARY KEY (linkid)
);
CREATE TABLE has_proceed (
page VARCHAR(32)
, CONSTRAINT pk_has_proceed PRIMARY KEY (page)
);
CREATE TABLE has_anchor_marvin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_marvin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjyluo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjyluo PRIMARY KEY (linkid)
);
CREATE TABLE has_novemb (
page VARCHAR(32)
, CONSTRAINT pk_has_novemb PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dept (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dept PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_implement (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_implement PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodagapito (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodagapito PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddastuart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddastuart PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodqzuo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqzuo PRIMARY KEY (linkid)
);
CREATE TABLE has_instruct (
page VARCHAR(32)
, CONSTRAINT pk_has_instruct PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodassign (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodassign PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodweb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodweb PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxfeng (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxfeng PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hour (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hour PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hous (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hous PRIMARY KEY (linkid)
);
CREATE TABLE has_hard (
page VARCHAR(32)
, CONSTRAINT pk_has_hard PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmanag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmanag PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_studi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_studi PRIMARY KEY (linkid)
);
CREATE TABLE has_todai (
page VARCHAR(32)
, CONSTRAINT pk_has_todai PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpart PRIMARY KEY (linkid)
);
CREATE TABLE has_week (
page VARCHAR(32)
, CONSTRAINT pk_has_week PRIMARY KEY (page)
);
CREATE TABLE has_anchor_naughton (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_naughton PRIMARY KEY (linkid)
);
CREATE TABLE has_special (
page VARCHAR(32)
, CONSTRAINT pk_has_special PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodgzhang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgzhang PRIMARY KEY (linkid)
);
CREATE TABLE has_cse (
page VARCHAR(32)
, CONSTRAINT pk_has_cse PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodamp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodamp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnarayan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnarayan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsiew (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsiew PRIMARY KEY (linkid)
);
CREATE TABLE has_hand (
page VARCHAR(32)
, CONSTRAINT pk_has_hand PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwisc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwisc PRIMARY KEY (linkid)
);
CREATE TABLE has_ve (
page VARCHAR(32)
, CONSTRAINT pk_has_ve PRIMARY KEY (page)
);
CREATE TABLE has_anchor_consult (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_consult PRIMARY KEY (linkid)
);
CREATE TABLE has_game (
page VARCHAR(32)
, CONSTRAINT pk_has_game PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodguangtian (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguangtian PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodweek (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodweek PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_weekli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_weekli PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwww (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwww PRIMARY KEY (linkid)
);
CREATE TABLE has_quarter (
page VARCHAR(32)
, CONSTRAINT pk_has_quarter PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodorgan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodorgan PRIMARY KEY (linkid)
);
CREATE TABLE has_signal (
page VARCHAR(32)
, CONSTRAINT pk_has_signal PRIMARY KEY (page)
);
CREATE TABLE has_anchor_marc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_marc PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsimul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsimul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpawang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpawang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_morph (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_morph PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodotu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodotu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfrequent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfrequent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_abstract (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_abstract PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_carei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_carei PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_stuart (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_stuart PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfeatur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfeatur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_prof (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_prof PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcompil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcompil PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bricker (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bricker PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_michael (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_michael PRIMARY KEY (linkid)
);
CREATE TABLE has_give (
page VARCHAR(32)
, CONSTRAINT pk_has_give PRIMARY KEY (page)
);
CREATE TABLE has_anchor_knowledg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_knowledg PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_neural (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_neural PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kelli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kelli PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pighin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pighin PRIMARY KEY (linkid)
);
CREATE TABLE has_dai (
page VARCHAR(32)
, CONSTRAINT pk_has_dai PRIMARY KEY (page)
);
CREATE TABLE has_area (
page VARCHAR(32)
, CONSTRAINT pk_has_area PRIMARY KEY (page)
);
CREATE TABLE has_grant (
page VARCHAR(32)
, CONSTRAINT pk_has_grant PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodborland (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodborland PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_world (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_world PRIMARY KEY (linkid)
);
CREATE TABLE has_archiv (
page VARCHAR(32)
, CONSTRAINT pk_has_archiv PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodyuan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyuan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ted (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ted PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcdj (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcdj PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzhang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wisconsin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wisconsin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodimprov (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodimprov PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsusan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsusan PRIMARY KEY (linkid)
);
CREATE TABLE has_educ (
page VARCHAR(32)
, CONSTRAINT pk_has_educ PRIMARY KEY (page)
);
CREATE TABLE has_manag (
page VARCHAR(32)
, CONSTRAINT pk_has_manag PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtwang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtwang PRIMARY KEY (linkid)
);
CREATE TABLE has_mondai (
page VARCHAR(32)
, CONSTRAINT pk_has_mondai PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlist (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlist PRIMARY KEY (linkid)
);
CREATE TABLE has_domain (
page VARCHAR(32)
, CONSTRAINT pk_has_domain PRIMARY KEY (page)
);
CREATE TABLE has_anchor_vortex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vortex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlisp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlisp PRIMARY KEY (linkid)
);
CREATE TABLE has_origin (
page VARCHAR(32)
, CONSTRAINT pk_has_origin PRIMARY KEY (page)
);
CREATE TABLE has_anchor_savag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_savag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooduw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooduw PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_hermjakob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_hermjakob PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodprofessor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprofessor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodedit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodedit PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjoel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjoel PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_beam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_beam PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_neil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_neil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodimag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodimag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoffic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoffic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsuggest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsuggest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmultiscalar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmultiscalar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_refer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_refer PRIMARY KEY (linkid)
);
CREATE TABLE has_base (
page VARCHAR(32)
, CONSTRAINT pk_has_base PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfound (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfound PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodartifici (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodartifici PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodread (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodread PRIMARY KEY (linkid)
);
CREATE TABLE has_public (
page VARCHAR(32)
, CONSTRAINT pk_has_public PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodreal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreal PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstructur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstructur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_miller (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_miller PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_steve (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_steve PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsymbol (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsymbol PRIMARY KEY (linkid)
);
CREATE TABLE has_behavior (
page VARCHAR(32)
, CONSTRAINT pk_has_behavior PRIMARY KEY (page)
);
CREATE TABLE has_anchor_game (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_game PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodintegr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodintegr PRIMARY KEY (linkid)
);
CREATE TABLE has_homepag (
page VARCHAR(32)
, CONSTRAINT pk_has_homepag PRIMARY KEY (page)
);
CREATE TABLE has_implement (
page VARCHAR(32)
, CONSTRAINT pk_has_implement PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodobrien (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodobrien PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodundergradu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodundergradu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_przemyslaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_przemyslaw PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_staff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_staff PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyongxiang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyongxiang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_overview (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_overview PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgroup (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgroup PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgaetano (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgaetano PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodoper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodoper PRIMARY KEY (linkid)
);
CREATE TABLE has_major (
page VARCHAR(32)
, CONSTRAINT pk_has_major PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodleekk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodleekk PRIMARY KEY (linkid)
);
CREATE TABLE has_algorithm (
page VARCHAR(32)
, CONSTRAINT pk_has_algorithm PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpropos (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpropos PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_nick (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_nick PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dai PRIMARY KEY (linkid)
);
CREATE TABLE has_sampl (
page VARCHAR(32)
, CONSTRAINT pk_has_sampl PRIMARY KEY (page)
);
CREATE TABLE has_evalu (
page VARCHAR(32)
, CONSTRAINT pk_has_evalu PRIMARY KEY (page)
);
CREATE TABLE has_anchor_kk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kk PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_announc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_announc PRIMARY KEY (linkid)
);
CREATE TABLE has_book (
page VARCHAR(32)
, CONSTRAINT pk_has_book PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfile (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfile PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcode (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcode PRIMARY KEY (linkid)
);
CREATE TABLE has_ruzzo (
page VARCHAR(32)
, CONSTRAINT pk_has_ruzzo PRIMARY KEY (page)
);
CREATE TABLE has_anchor_test (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_test PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jignesh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jignesh PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mechan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mechan PRIMARY KEY (linkid)
);
CREATE TABLE has_solution (
page VARCHAR(32)
, CONSTRAINT pk_has_solution PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpadgett (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpadgett PRIMARY KEY (linkid)
);
CREATE TABLE has_choos (
page VARCHAR(32)
, CONSTRAINT pk_has_choos PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodajita (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodajita PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodguest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguest PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmartym (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmartym PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_access (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_access PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_number (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_number PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodesteban (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodesteban PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_memori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_memori PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_yu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_yu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodquestion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodquestion PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodobject (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodobject PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_archiv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_archiv PRIMARY KEY (linkid)
);
CREATE TABLE has_option (
page VARCHAR(32)
, CONSTRAINT pk_has_option PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinterfac (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinterfac PRIMARY KEY (linkid)
);
CREATE TABLE has_metacrawl (
page VARCHAR(32)
, CONSTRAINT pk_has_metacrawl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodphd (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodphd PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfall PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_matlab (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_matlab PRIMARY KEY (linkid)
);
CREATE TABLE has_properti (
page VARCHAR(32)
, CONSTRAINT pk_has_properti PRIMARY KEY (page)
);
CREATE TABLE has_anchor_schnarr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_schnarr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchu PRIMARY KEY (linkid)
);
CREATE TABLE has_undergradu (
page VARCHAR(32)
, CONSTRAINT pk_has_undergradu PRIMARY KEY (page)
);
CREATE TABLE has_februari (
page VARCHAR(32)
, CONSTRAINT pk_has_februari PRIMARY KEY (page)
);
CREATE TABLE has_implem (
page VARCHAR(32)
, CONSTRAINT pk_has_implem PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodnetscap (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnetscap PRIMARY KEY (linkid)
);
CREATE TABLE has_color (
page VARCHAR(32)
, CONSTRAINT pk_has_color PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbibliographi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbibliographi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cognit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cognit PRIMARY KEY (linkid)
);
CREATE TABLE has_cpp (
page VARCHAR(32)
, CONSTRAINT pk_has_cpp PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodclick (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodclick PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodresearch (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodresearch PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchong PRIMARY KEY (linkid)
);
CREATE TABLE has_add (
page VARCHAR(32)
, CONSTRAINT pk_has_add PRIMARY KEY (page)
);
CREATE TABLE has_septemb (
page VARCHAR(32)
, CONSTRAINT pk_has_septemb PRIMARY KEY (page)
);
CREATE TABLE has_anchor_don (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_don PRIMARY KEY (linkid)
);
CREATE TABLE has_left (
page VARCHAR(32)
, CONSTRAINT pk_has_left PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodchin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchin PRIMARY KEY (linkid)
);
CREATE TABLE has_dissert (
page VARCHAR(32)
, CONSTRAINT pk_has_dissert PRIMARY KEY (page)
);
CREATE TABLE has_anchor_school (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_school PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_biologi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_biologi PRIMARY KEY (linkid)
);
CREATE TABLE has_proc (
page VARCHAR(32)
, CONSTRAINT pk_has_proc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpapadopoulo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpapadopoulo PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jeffrei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jeffrei PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodharold (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodharold PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodidea (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodidea PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodqueri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqueri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlarg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlarg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgao PRIMARY KEY (linkid)
);
CREATE TABLE has_statem (
page VARCHAR(32)
, CONSTRAINT pk_has_statem PRIMARY KEY (page)
);
CREATE TABLE has_numer (
page VARCHAR(32)
, CONSTRAINT pk_has_numer PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlaru (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlaru PRIMARY KEY (linkid)
);
CREATE TABLE has_section (
page VARCHAR(32)
, CONSTRAINT pk_has_section PRIMARY KEY (page)
);
CREATE TABLE has_anchor_engin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_engin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_qsim (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_qsim PRIMARY KEY (linkid)
);
CREATE TABLE has_review (
page VARCHAR(32)
, CONSTRAINT pk_has_review PRIMARY KEY (page)
);
CREATE TABLE has_anchor_topic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_topic PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodplace (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodplace PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_manual (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_manual PRIMARY KEY (linkid)
);
CREATE TABLE has_window (
page VARCHAR(32)
, CONSTRAINT pk_has_window PRIMARY KEY (page)
);
CREATE TABLE has_april (
page VARCHAR(32)
, CONSTRAINT pk_has_april PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinstruct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinstruct PRIMARY KEY (linkid)
);
CREATE TABLE has_space (
page VARCHAR(32)
, CONSTRAINT pk_has_space PRIMARY KEY (page)
);
CREATE TABLE has_anchor_ioannidi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ioannidi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_tanimoto (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_tanimoto PRIMARY KEY (linkid)
);
CREATE TABLE has_memori (
page VARCHAR(32)
, CONSTRAINT pk_has_memori PRIMARY KEY (page)
);
CREATE TABLE has_anchor_info (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_info PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_reinhardt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_reinhardt PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjava (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjava PRIMARY KEY (linkid)
);
CREATE TABLE has_fall (
page VARCHAR(32)
, CONSTRAINT pk_has_fall PRIMARY KEY (page)
);
CREATE TABLE has_continu (
page VARCHAR(32)
, CONSTRAINT pk_has_continu PRIMARY KEY (page)
);
CREATE TABLE has_handl (
page VARCHAR(32)
, CONSTRAINT pk_has_handl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodqiang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqiang PRIMARY KEY (linkid)
);
CREATE TABLE has_late (
page VARCHAR(32)
, CONSTRAINT pk_has_late PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjeff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjeff PRIMARY KEY (linkid)
);
CREATE TABLE has_cours (
page VARCHAR(32)
, CONSTRAINT pk_has_cours PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbrad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbrad PRIMARY KEY (linkid)
);
CREATE TABLE has_peopl (
page VARCHAR(32)
, CONSTRAINT pk_has_peopl PRIMARY KEY (page)
);
CREATE TABLE has_anchor_base (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_base PRIMARY KEY (linkid)
);
CREATE TABLE has_overview (
page VARCHAR(32)
, CONSTRAINT pk_has_overview PRIMARY KEY (page)
);
CREATE TABLE has_respons (
page VARCHAR(32)
, CONSTRAINT pk_has_respons PRIMARY KEY (page)
);
CREATE TABLE has_anchor_integr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_integr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwide (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwide PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodruweihu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodruweihu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodurl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodurl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_forman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_forman PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_softwar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_softwar PRIMARY KEY (linkid)
);
CREATE TABLE has_alloc (
page VARCHAR(32)
, CONSTRAINT pk_has_alloc PRIMARY KEY (page)
);
CREATE TABLE has_intern (
page VARCHAR(32)
, CONSTRAINT pk_has_intern PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtexa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtexa PRIMARY KEY (linkid)
);
CREATE TABLE has_bound (
page VARCHAR(32)
, CONSTRAINT pk_has_bound PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodclass (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodclass PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_utc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_utc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_finger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_finger PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtext (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtext PRIMARY KEY (linkid)
);
CREATE TABLE has_june (
page VARCHAR(32)
, CONSTRAINT pk_has_june PRIMARY KEY (page)
);
CREATE TABLE has_anchor_exampl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_exampl PRIMARY KEY (linkid)
);
CREATE TABLE has_academ (
page VARCHAR(32)
, CONSTRAINT pk_has_academ PRIMARY KEY (page)
);
CREATE TABLE has_anchor_eun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_eun PRIMARY KEY (linkid)
);
CREATE TABLE has_inform (
page VARCHAR(32)
, CONSTRAINT pk_has_inform PRIMARY KEY (page)
);
CREATE TABLE has_detail (
page VARCHAR(32)
, CONSTRAINT pk_has_detail PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpast (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpast PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_cecil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_cecil PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_imper (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_imper PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkumar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkumar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcolleg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcolleg PRIMARY KEY (linkid)
);
CREATE TABLE has_fact (
page VARCHAR(32)
, CONSTRAINT pk_has_fact PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodevent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodevent PRIMARY KEY (linkid)
);
CREATE TABLE has_assign (
page VARCHAR(32)
, CONSTRAINT pk_has_assign PRIMARY KEY (page)
);
CREATE TABLE has_anchor_bestor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bestor PRIMARY KEY (linkid)
);
CREATE TABLE has_ladner (
page VARCHAR(32)
, CONSTRAINT pk_has_ladner PRIMARY KEY (page)
);
CREATE TABLE has_updat (
page VARCHAR(32)
, CONSTRAINT pk_has_updat PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsession (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsession PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodproject (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodproject PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bednar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bednar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodstuff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstuff PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodom (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodom PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwarshaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwarshaw PRIMARY KEY (linkid)
);
CREATE TABLE has_read (
page VARCHAR(32)
, CONSTRAINT pk_has_read PRIMARY KEY (page)
);
CREATE TABLE has_anchor_mvi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mvi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlogic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlogic PRIMARY KEY (linkid)
);
CREATE TABLE has_report (
page VARCHAR(32)
, CONSTRAINT pk_has_report PRIMARY KEY (page)
);
CREATE TABLE has_scienc (
page VARCHAR(32)
, CONSTRAINT pk_has_scienc PRIMARY KEY (page)
);
CREATE TABLE has_anchor_interfac (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_interfac PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodramachandran (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodramachandran PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sirer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sirer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gokul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gokul PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlauri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlauri PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchung (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchung PRIMARY KEY (linkid)
);
CREATE TABLE has_cc (
page VARCHAR(32)
, CONSTRAINT pk_has_cc PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodbatori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbatori PRIMARY KEY (linkid)
);
CREATE TABLE has_automat (
page VARCHAR(32)
, CONSTRAINT pk_has_automat PRIMARY KEY (page)
);
CREATE TABLE has_scale (
page VARCHAR(32)
, CONSTRAINT pk_has_scale PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddon PRIMARY KEY (linkid)
);
CREATE TABLE has_style (
page VARCHAR(32)
, CONSTRAINT pk_has_style PRIMARY KEY (page)
);
CREATE TABLE has_anchor_salli (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_salli PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlife (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlife PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_multi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_multi PRIMARY KEY (linkid)
);
CREATE TABLE has_sort (
page VARCHAR(32)
, CONSTRAINT pk_has_sort PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlab (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlab PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvideo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvideo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddirectori (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddirectori PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlap (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlap PRIMARY KEY (linkid)
);
CREATE TABLE has_comput (
page VARCHAR(32)
, CONSTRAINT pk_has_comput PRIMARY KEY (page)
);
CREATE TABLE has_guid (
page VARCHAR(32)
, CONSTRAINT pk_has_guid PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlaw (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlaw PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtree (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtree PRIMARY KEY (linkid)
);
CREATE TABLE has_man (
page VARCHAR(32)
, CONSTRAINT pk_has_man PRIMARY KEY (page)
);
CREATE TABLE has_effect (
page VARCHAR(32)
, CONSTRAINT pk_has_effect PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcampu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcampu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_goodman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_goodman PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_function (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_function PRIMARY KEY (linkid)
);
CREATE TABLE has_approxim (
page VARCHAR(32)
, CONSTRAINT pk_has_approxim PRIMARY KEY (page)
);
CREATE TABLE has_digit (
page VARCHAR(32)
, CONSTRAINT pk_has_digit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodzhouxiao (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzhouxiao PRIMARY KEY (linkid)
);
CREATE TABLE has_mar (
page VARCHAR(32)
, CONSTRAINT pk_has_mar PRIMARY KEY (page)
);
CREATE TABLE has_ieee (
page VARCHAR(32)
, CONSTRAINT pk_has_ieee PRIMARY KEY (page)
);
CREATE TABLE has_anchor_perform (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_perform PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmessag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmessag PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgraham (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgraham PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodashi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodashi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvortex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvortex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodthesi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodthesi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_graphic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_graphic PRIMARY KEY (linkid)
);
CREATE TABLE has_schedul (
page VARCHAR(32)
, CONSTRAINT pk_has_schedul PRIMARY KEY (page)
);
CREATE TABLE has_anchor_chuck (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_chuck PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_physic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_physic PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodajit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodajit PRIMARY KEY (linkid)
);
CREATE TABLE has_job (
page VARCHAR(32)
, CONSTRAINT pk_has_job PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodta (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodta PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcreat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcreat PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtrace (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtrace PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrtan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrtan PRIMARY KEY (linkid)
);
CREATE TABLE has_journal (
page VARCHAR(32)
, CONSTRAINT pk_has_journal PRIMARY KEY (page)
);
CREATE TABLE has_chang (
page VARCHAR(32)
, CONSTRAINT pk_has_chang PRIMARY KEY (page)
);
CREATE TABLE has_regist (
page VARCHAR(32)
, CONSTRAINT pk_has_regist PRIMARY KEY (page)
);
CREATE TABLE has_click (
page VARCHAR(32)
, CONSTRAINT pk_has_click PRIMARY KEY (page)
);
CREATE TABLE has_error (
page VARCHAR(32)
, CONSTRAINT pk_has_error PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjob PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_geijn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_geijn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlanguag (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlanguag PRIMARY KEY (linkid)
);
CREATE TABLE has_west (
page VARCHAR(32)
, CONSTRAINT pk_has_west PRIMARY KEY (page)
);
CREATE TABLE has_neighborhooddecision (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddecision PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmain (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmain PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbarbancon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbarbancon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkei PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnet PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbayardo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbayardo PRIMARY KEY (linkid)
);
CREATE TABLE has_abstract (
page VARCHAR(32)
, CONSTRAINT pk_has_abstract PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmovi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmovi PRIMARY KEY (linkid)
);
CREATE TABLE has_structur (
page VARCHAR(32)
, CONSTRAINT pk_has_structur PRIMARY KEY (page)
);
CREATE TABLE has_anchor_teach (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_teach PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodluxu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodluxu PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_member (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_member PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_personnel (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_personnel PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmove (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmove PRIMARY KEY (linkid)
);
CREATE TABLE has_press (
page VARCHAR(32)
, CONSTRAINT pk_has_press PRIMARY KEY (page)
);
CREATE TABLE has_ph (
page VARCHAR(32)
, CONSTRAINT pk_has_ph PRIMARY KEY (page)
);
CREATE TABLE has_anchor_network (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_network PRIMARY KEY (linkid)
);
CREATE TABLE has_paper (
page VARCHAR(32)
, CONSTRAINT pk_has_paper PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodadair (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadair PRIMARY KEY (linkid)
);
CREATE TABLE has_vision (
page VARCHAR(32)
, CONSTRAINT pk_has_vision PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhsi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhsi PRIMARY KEY (linkid)
);
CREATE TABLE has_august (
page VARCHAR(32)
, CONSTRAINT pk_has_august PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodaddition (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaddition PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_snyder (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_snyder PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_egger (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_egger PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_album (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_album PRIMARY KEY (linkid)
);
CREATE TABLE has_length (
page VARCHAR(32)
, CONSTRAINT pk_has_length PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodinfo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinfo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhing (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhing PRIMARY KEY (linkid)
);
CREATE TABLE has_learn (
page VARCHAR(32)
, CONSTRAINT pk_has_learn PRIMARY KEY (page)
);
CREATE TABLE has_emac (
page VARCHAR(32)
, CONSTRAINT pk_has_emac PRIMARY KEY (page)
);
CREATE TABLE has_anchor_kabra (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kabra PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_faculti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_faculti PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhuiqun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhuiqun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcthomp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcthomp PRIMARY KEY (linkid)
);
CREATE TABLE has_shape (
page VARCHAR(32)
, CONSTRAINT pk_has_shape PRIMARY KEY (page)
);
CREATE TABLE has_group (
page VARCHAR(32)
, CONSTRAINT pk_has_group PRIMARY KEY (page)
);
CREATE TABLE has_anchor_midterm (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_midterm PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_undergradu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_undergradu PRIMARY KEY (linkid)
);
CREATE TABLE has_summari (
page VARCHAR(32)
, CONSTRAINT pk_has_summari PRIMARY KEY (page)
);
CREATE TABLE has_world (
page VARCHAR(32)
, CONSTRAINT pk_has_world PRIMARY KEY (page)
);
CREATE TABLE has_specif (
page VARCHAR(32)
, CONSTRAINT pk_has_specif PRIMARY KEY (page)
);
CREATE TABLE has_view (
page VARCHAR(32)
, CONSTRAINT pk_has_view PRIMARY KEY (page)
);
CREATE TABLE has_open (
page VARCHAR(32)
, CONSTRAINT pk_has_open PRIMARY KEY (page)
);
CREATE TABLE has_provid (
page VARCHAR(32)
, CONSTRAINT pk_has_provid PRIMARY KEY (page)
);
CREATE TABLE has_electron (
page VARCHAR(32)
, CONSTRAINT pk_has_electron PRIMARY KEY (page)
);
CREATE TABLE has_anchor_harrick (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_harrick PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_high (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_high PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_servic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_servic PRIMARY KEY (linkid)
);
CREATE TABLE has_wi (
page VARCHAR(32)
, CONSTRAINT pk_has_wi PRIMARY KEY (page)
);
CREATE TABLE has_disk (
page VARCHAR(32)
, CONSTRAINT pk_has_disk PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpublish (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpublish PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnewsgroup (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnewsgroup PRIMARY KEY (linkid)
);
CREATE TABLE has_thread (
page VARCHAR(32)
, CONSTRAINT pk_has_thread PRIMARY KEY (page)
);
CREATE TABLE has_anchor_stuff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_stuff PRIMARY KEY (linkid)
);
CREATE TABLE has_zpl (
page VARCHAR(32)
, CONSTRAINT pk_has_zpl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodperson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodperson PRIMARY KEY (linkid)
);
CREATE TABLE has_post (
page VARCHAR(32)
, CONSTRAINT pk_has_post PRIMARY KEY (page)
);
CREATE TABLE has_techniqu (
page VARCHAR(32)
, CONSTRAINT pk_has_techniqu PRIMARY KEY (page)
);
CREATE TABLE has_anchor_relat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_relat PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsun PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_common (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_common PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodacadem (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodacadem PRIMARY KEY (linkid)
);
CREATE TABLE has_pp (
page VARCHAR(32)
, CONSTRAINT pk_has_pp PRIMARY KEY (page)
);
CREATE TABLE has_anchor_sci (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sci PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_koga (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_koga PRIMARY KEY (linkid)
);
CREATE TABLE has_pm (
page VARCHAR(32)
, CONSTRAINT pk_has_pm PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprimari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprimari PRIMARY KEY (linkid)
);
CREATE TABLE has_pc (
page VARCHAR(32)
, CONSTRAINT pk_has_pc PRIMARY KEY (page)
);
CREATE TABLE has_hall (
page VARCHAR(32)
, CONSTRAINT pk_has_hall PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmicrosoft (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmicrosoft PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_fund (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fund PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodinvolv (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodinvolv PRIMARY KEY (linkid)
);
CREATE TABLE has_amount (
page VARCHAR(32)
, CONSTRAINT pk_has_amount PRIMARY KEY (page)
);
CREATE TABLE has_anchor_yanni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_yanni PRIMARY KEY (linkid)
);
CREATE TABLE has_buffer (
page VARCHAR(32)
, CONSTRAINT pk_has_buffer PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcluster (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcluster PRIMARY KEY (linkid)
);
CREATE TABLE has_midterm (
page VARCHAR(32)
, CONSTRAINT pk_has_midterm PRIMARY KEY (page)
);
CREATE TABLE has_anchor_robot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_robot PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgraduat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgraduat PRIMARY KEY (linkid)
);
CREATE TABLE has_put (
page VARCHAR(32)
, CONSTRAINT pk_has_put PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodqing (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqing PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_oren (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_oren PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhttp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhttp PRIMARY KEY (linkid)
);
CREATE TABLE has_student (
page VARCHAR(32)
, CONSTRAINT pk_has_student PRIMARY KEY (page)
);
CREATE TABLE has_anchor_intro (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_intro PRIMARY KEY (linkid)
);
CREATE TABLE has_intellig (
page VARCHAR(32)
, CONSTRAINT pk_has_intellig PRIMARY KEY (page)
);
CREATE TABLE has_anchor_vlsi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vlsi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgreg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgreg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmeet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmeet PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_robert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_robert PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_exam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_exam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgeijn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgeijn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodselect (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodselect PRIMARY KEY (linkid)
);
CREATE TABLE has_browser (
page VARCHAR(32)
, CONSTRAINT pk_has_browser PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodpresent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpresent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_wwt (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wwt PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_www (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_www PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mream (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mream PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_send (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_send PRIMARY KEY (linkid)
);
CREATE TABLE has_standard (
page VARCHAR(32)
, CONSTRAINT pk_has_standard PRIMARY KEY (page)
);
CREATE TABLE has_anchor_wiscinfo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_wiscinfo PRIMARY KEY (linkid)
);
CREATE TABLE has_true (
page VARCHAR(32)
, CONSTRAINT pk_has_true PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodguid (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguid PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodanalysi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodanalysi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_avinash (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_avinash PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpractic (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpractic PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_raymond (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_raymond PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_profil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_profil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcours (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcours PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodusa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodusa PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwilliam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwilliam PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_laru (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_laru PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodisaac (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodisaac PRIMARY KEY (linkid)
);
CREATE TABLE has_symbol (
page VARCHAR(32)
, CONSTRAINT pk_has_symbol PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfrederick (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfrederick PRIMARY KEY (linkid)
);
CREATE TABLE has_titl (
page VARCHAR(32)
, CONSTRAINT pk_has_titl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlot (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlot PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwu PRIMARY KEY (linkid)
);
CREATE TABLE has_made (
page VARCHAR(32)
, CONSTRAINT pk_has_made PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmartin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmartin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_start (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_start PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_form (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_form PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_washington (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_washington PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodwa (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwa PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodkornerup (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodkornerup PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_dean (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dean PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_review (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_review PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_java (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_java PRIMARY KEY (linkid)
);
CREATE TABLE has_search (
page VARCHAR(32)
, CONSTRAINT pk_has_search PRIMARY KEY (page)
);
CREATE TABLE has_databas (
page VARCHAR(32)
, CONSTRAINT pk_has_databas PRIMARY KEY (page)
);
CREATE TABLE has_anchor_bershad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bershad PRIMARY KEY (linkid)
);
CREATE TABLE has_node (
page VARCHAR(32)
, CONSTRAINT pk_has_node PRIMARY KEY (page)
);
CREATE TABLE has_concept (
page VARCHAR(32)
, CONSTRAINT pk_has_concept PRIMARY KEY (page)
);
CREATE TABLE has_anchor_environ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_environ PRIMARY KEY (linkid)
);
CREATE TABLE has_list (
page VARCHAR(32)
, CONSTRAINT pk_has_list PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodporter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodporter PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_organ (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_organ PRIMARY KEY (linkid)
);
CREATE TABLE has_figur (
page VARCHAR(32)
, CONSTRAINT pk_has_figur PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodreason (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodreason PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooded (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooded PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyanni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyanni PRIMARY KEY (linkid)
);
CREATE TABLE has_real (
page VARCHAR(32)
, CONSTRAINT pk_has_real PRIMARY KEY (page)
);
CREATE TABLE has_anchor_visual (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_visual PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_instructor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_instructor PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_jeff (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_jeff PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_web (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_web PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_distribut (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_distribut PRIMARY KEY (linkid)
);
CREATE TABLE has_teach (
page VARCHAR(32)
, CONSTRAINT pk_has_teach PRIMARY KEY (page)
);
CREATE TABLE has_investig (
page VARCHAR(32)
, CONSTRAINT pk_has_investig PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsemest (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsemest PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_shaob (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_shaob PRIMARY KEY (linkid)
);
CREATE TABLE has_measur (
page VARCHAR(32)
, CONSTRAINT pk_has_measur PRIMARY KEY (page)
);
CREATE TABLE has_anchor_marku (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_marku PRIMARY KEY (linkid)
);
CREATE TABLE has_case (
page VARCHAR(32)
, CONSTRAINT pk_has_case PRIMARY KEY (page)
);
CREATE TABLE has_anchor_dion (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_dion PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnation (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnation PRIMARY KEY (linkid)
);
CREATE TABLE has_check (
page VARCHAR(32)
, CONSTRAINT pk_has_check PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodfind (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfind PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyu PRIMARY KEY (linkid)
);
CREATE TABLE has_step (
page VARCHAR(32)
, CONSTRAINT pk_has_step PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodyangyang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyangyang PRIMARY KEY (linkid)
);
CREATE TABLE has_edit (
page VARCHAR(32)
, CONSTRAINT pk_has_edit PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlevent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlevent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_vin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_vin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_write (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_write PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodapril (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodapril PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodraymond (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodraymond PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gordon (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gordon PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodimport (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodimport PRIMARY KEY (linkid)
);
CREATE TABLE all_words_capitalized (
linkid VARCHAR(32)
, CONSTRAINT pk_all_words_capitalized PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_resum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_resum PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnatur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnatur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_committe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_committe PRIMARY KEY (linkid)
);
CREATE TABLE has_technic (
page VARCHAR(32)
, CONSTRAINT pk_has_technic PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcomplex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcomplex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcomplet (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcomplet PRIMARY KEY (linkid)
);
CREATE TABLE has_install (
page VARCHAR(32)
, CONSTRAINT pk_has_install PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodromer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodromer PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlogin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlogin PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_mobil (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_mobil PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodfang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodfang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddougla (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddougla PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodscout (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodscout PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmolecular (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmolecular PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_pictur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_pictur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_scott (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_scott PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddeji (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddeji PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodhaizhou (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhaizhou PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_version (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_version PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_project (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_project PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsolution (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsolution PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_kunchithapadam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kunchithapadam PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_back (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_back PRIMARY KEY (linkid)
);
CREATE TABLE has_rule (
page VARCHAR(32)
, CONSTRAINT pk_has_rule PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlatex (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlatex PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchoe (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchoe PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchoi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchoi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_coral (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_coral PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bad (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bad PRIMARY KEY (linkid)
);
CREATE TABLE has_queri (
page VARCHAR(32)
, CONSTRAINT pk_has_queri PRIMARY KEY (page)
);
CREATE TABLE has_anchor_automat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_automat PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_bookmark (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_bookmark PRIMARY KEY (linkid)
);
CREATE TABLE has_synchron (
page VARCHAR(32)
, CONSTRAINT pk_has_synchron PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodckwong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodckwong PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodtodd (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtodd PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodzpl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodzpl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmarvin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmarvin PRIMARY KEY (linkid)
);
CREATE TABLE has_polici (
page VARCHAR(32)
, CONSTRAINT pk_has_polici PRIMARY KEY (page)
);
CREATE TABLE has_repres (
page VARCHAR(32)
, CONSTRAINT pk_has_repres PRIMARY KEY (page)
);
CREATE TABLE has_anchor_porter (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_porter PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ma (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ma PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjfang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjfang PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_campu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_campu PRIMARY KEY (linkid)
);
CREATE TABLE has_send (
page VARCHAR(32)
, CONSTRAINT pk_has_send PRIMARY KEY (page)
);
CREATE TABLE has_commun (
page VARCHAR(32)
, CONSTRAINT pk_has_commun PRIMARY KEY (page)
);
CREATE TABLE has_print (
page VARCHAR(32)
, CONSTRAINT pk_has_print PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodstephen (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodstephen PRIMARY KEY (linkid)
);
CREATE TABLE has_simpl (
page VARCHAR(32)
, CONSTRAINT pk_has_simpl PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodwkmak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodwkmak PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_navin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_navin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodspace (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodspace PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_unit (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_unit PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodferri (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodferri PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_sean (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sean PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_unix (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_unix PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddave (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddave PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_drive (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_drive PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmisconduct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmisconduct PRIMARY KEY (linkid)
);
CREATE TABLE has_pst (
page VARCHAR(32)
, CONSTRAINT pk_has_pst PRIMARY KEY (page)
);
CREATE TABLE has_anchor_univers (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_univers PRIMARY KEY (linkid)
);
CREATE TABLE has_size (
page VARCHAR(32)
, CONSTRAINT pk_has_size PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlive (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlive PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodplai (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodplai PRIMARY KEY (linkid)
);
CREATE TABLE has_previou (
page VARCHAR(32)
, CONSTRAINT pk_has_previou PRIMARY KEY (page)
);
CREATE TABLE has_anchor_susan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_susan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_gun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_gun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodplan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodplan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_peopl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_peopl PRIMARY KEY (linkid)
);
CREATE TABLE has_larg (
page VARCHAR(32)
, CONSTRAINT pk_has_larg PRIMARY KEY (page)
);
CREATE TABLE has_imag (
page VARCHAR(32)
, CONSTRAINT pk_has_imag PRIMARY KEY (page)
);
CREATE TABLE has_lab (
page VARCHAR(32)
, CONSTRAINT pk_has_lab PRIMARY KEY (page)
);
CREATE TABLE has_anchor_time (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_time PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodexperiment (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexperiment PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsummari (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsummari PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodevalu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodevalu PRIMARY KEY (linkid)
);
CREATE TABLE has_content (
page VARCHAR(32)
, CONSTRAINT pk_has_content PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodrvdg (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrvdg PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodleft (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodleft PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_work (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_work PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodworld (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodworld PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_advanc (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_advanc PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_docum (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_docum PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodengin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodengin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrupert (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrupert PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpictur (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpictur PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_william (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_william PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrdb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrdb PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsupport (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsupport PRIMARY KEY (linkid)
);
CREATE TABLE has_includ (
page VARCHAR(32)
, CONSTRAINT pk_has_includ PRIMARY KEY (page)
);
CREATE TABLE has_power (
page VARCHAR(32)
, CONSTRAINT pk_has_power PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodmoriarti (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmoriarti PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodulf (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodulf PRIMARY KEY (linkid)
);
CREATE TABLE has_const (
page VARCHAR(32)
, CONSTRAINT pk_has_const PRIMARY KEY (page)
);
CREATE TABLE has_binari (
page VARCHAR(32)
, CONSTRAINT pk_has_binari PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodde (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodde PRIMARY KEY (linkid)
);
CREATE TABLE has_faq (
page VARCHAR(32)
, CONSTRAINT pk_has_faq PRIMARY KEY (page)
);
CREATE TABLE has_induct (
page VARCHAR(32)
, CONSTRAINT pk_has_induct PRIMARY KEY (page)
);
CREATE TABLE has_anchor_plapack (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_plapack PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddh (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddh PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcraig (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcraig PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddr (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddr PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddwip (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddwip PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyingjun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyingjun PRIMARY KEY (linkid)
);
CREATE TABLE has_count (
page VARCHAR(32)
, CONSTRAINT pk_has_count PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodqime (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodqime PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodremolina (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodremolina PRIMARY KEY (linkid)
);
CREATE TABLE has_idea (
page VARCHAR(32)
, CONSTRAINT pk_has_idea PRIMARY KEY (page)
);
CREATE TABLE has_anchor_fall (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_fall PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsalesin (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsalesin PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcharl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcharl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_brown (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_brown PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsundeep (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsundeep PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodposnak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodposnak PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpaulmcq (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpaulmcq PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodccp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodccp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmwbarn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmwbarn PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodenviron (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodenviron PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_misconduct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_misconduct PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrhwang (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrhwang PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodcurrent (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcurrent PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_rich (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_rich PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodlavend (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlavend PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_ladner (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_ladner PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgoal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgoal PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_good (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_good PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrenu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrenu PRIMARY KEY (linkid)
);
CREATE TABLE has_artifici (
page VARCHAR(32)
, CONSTRAINT pk_has_artifici PRIMARY KEY (page)
);
CREATE TABLE has_anchor_paul (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_paul PRIMARY KEY (linkid)
);
CREATE TABLE has_transform (
page VARCHAR(32)
, CONSTRAINT pk_has_transform PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodcondor (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodcondor PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodchamber (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodchamber PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodupdat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodupdat PRIMARY KEY (linkid)
);
CREATE TABLE has_nov (
page VARCHAR(32)
, CONSTRAINT pk_has_nov PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodsheetal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsheetal PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_zpl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_zpl PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_moonei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_moonei PRIMARY KEY (linkid)
);
CREATE TABLE has_author (
page VARCHAR(32)
, CONSTRAINT pk_has_author PRIMARY KEY (page)
);
CREATE TABLE has_anchor_click (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_click PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_olvi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_olvi PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_construct (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_construct PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodpardyak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodpardyak PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodnbsp (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodnbsp PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodguo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodguo PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddecemb (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddecemb PRIMARY KEY (linkid)
);
CREATE TABLE has_semant (
page VARCHAR(32)
, CONSTRAINT pk_has_semant PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodjohn (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjohn PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_confer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_confer PRIMARY KEY (linkid)
);
CREATE TABLE has_march (
page VARCHAR(32)
, CONSTRAINT pk_has_march PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodvl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodadam (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodadam PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodxu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodxu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodve (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodve PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_milo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_milo PRIMARY KEY (linkid)
);
CREATE TABLE has_object (
page VARCHAR(32)
, CONSTRAINT pk_has_object PRIMARY KEY (page)
);
CREATE TABLE has_anchor_kumar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_kumar PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_alan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_alan PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodjan (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodjan PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_page (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_page PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrong (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrong PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrun PRIMARY KEY (linkid)
);
CREATE TABLE has_studi (
page VARCHAR(32)
, CONSTRAINT pk_has_studi PRIMARY KEY (page)
);
CREATE TABLE has_anchor_sampl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_sampl PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodrui (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrui PRIMARY KEY (linkid)
);
CREATE TABLE has_translat (
page VARCHAR(32)
, CONSTRAINT pk_has_translat PRIMARY KEY (page)
);
CREATE TABLE has_aug (
page VARCHAR(32)
, CONSTRAINT pk_has_aug PRIMARY KEY (page)
);
CREATE TABLE has_anchor_point (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_point PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_devis (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_devis PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsep (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsep PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodset (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodset PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmathemat (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmathemat PRIMARY KEY (linkid)
);
CREATE TABLE has_colleg (
page VARCHAR(32)
, CONSTRAINT pk_has_colleg PRIMARY KEY (page)
);
CREATE TABLE has_anchor_toni (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_toni PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodngk (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodngk PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodgood (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodgood PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodemerson (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodemerson PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_metip (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_metip PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_lo (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_lo PRIMARY KEY (linkid)
);
CREATE TABLE has_separ (
page VARCHAR(32)
, CONSTRAINT pk_has_separ PRIMARY KEY (page)
);
CREATE TABLE has_map (
page VARCHAR(32)
, CONSTRAINT pk_has_map PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodprincipl (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodprincipl PRIMARY KEY (linkid)
);
CREATE TABLE has_don (
page VARCHAR(32)
, CONSTRAINT pk_has_don PRIMARY KEY (page)
);
CREATE TABLE has_citi (
page VARCHAR(32)
, CONSTRAINT pk_has_citi PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodlocal (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodlocal PRIMARY KEY (linkid)
);
CREATE TABLE has_announc (
page VARCHAR(32)
, CONSTRAINT pk_has_announc PRIMARY KEY (page)
);
CREATE TABLE has_www (
page VARCHAR(32)
, CONSTRAINT pk_has_www PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodexperi (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodexperi PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodaddala (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodaddala PRIMARY KEY (linkid)
);
CREATE TABLE has_appoint (
page VARCHAR(32)
, CONSTRAINT pk_has_appoint PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodrraj (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodrraj PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodsirer (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodsirer PRIMARY KEY (linkid)
);
CREATE TABLE has_anchor_novak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_anchor_novak PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodissu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodissu PRIMARY KEY (linkid)
);
CREATE TABLE has_great (
page VARCHAR(32)
, CONSTRAINT pk_has_great PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodvaidyaraman (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvaidyaraman PRIMARY KEY (linkid)
);
CREATE TABLE has_contact (
page VARCHAR(32)
, CONSTRAINT pk_has_contact PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodhill (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodhill PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodquiz (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodquiz PRIMARY KEY (linkid)
);
CREATE TABLE has_foundat (
page VARCHAR(32)
, CONSTRAINT pk_has_foundat PRIMARY KEY (page)
);
CREATE TABLE has_neighborhoodtser (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodtser PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmak (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmak PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodyonglu (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodyonglu PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodvurgun (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodvurgun PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmap (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmap PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhooddevelop (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhooddevelop PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodmar (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodmar PRIMARY KEY (linkid)
);
CREATE TABLE has_neighborhoodbradlei (
linkid VARCHAR(32)
, CONSTRAINT pk_has_neighborhoodbradlei PRIMARY KEY (linkid)
);
