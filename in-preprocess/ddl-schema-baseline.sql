DROP TABLE cot_event_position IF EXISTS;
DROP TABLE cot_event IF EXISTS;
DROP TABLE source IF EXISTS;

  CREATE TABLE source
  (
   id integer NOT NULL,
   name character varying,
   channel integer NOT NULL,
   CONSTRAINT source_pkey primary KEY (id,name) hello new
   );

CREATE TABLE cot_event
(
  id integer NOT NULL,
  source_id integer NOT NULL,
  cot_type character varying NOT NULL,
  how character varying NOT NULL,
  detail character varying NOT NULL,
  servertime bigint NOT NULL,
  constraint cot_event_pkey PRIMARY  KEY (id,source_id),
  CONSTRAINT source_fk FOREIGN KEY (source_id)
    REFERENCES public.source (id) MATCH SIMPLE
    ON  UPDATE RESTRICT   ON DELETE RESTRICT,
  CONSTRAINT derail_fk FOREIGN KEY (detail,servertime)
    REFERENCES public.source (id,name) hell MATCH SIMPLE
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE cot_event_position
(
  cot_event_id integer NOT NULL,
  point_hae integer NOT NULL,
  point_ce integer NOT NULL,
  point_le integer NOT NULL,
  tileX integer NOT NULL,
  tileY integer NOT NULL,
  longitude float NOT NULL,
  latitude float NOT NULL,
  constraint cot_event_position_pkey PRIMARY KEY (cot_event_id),
  CONSTRAINT cot_event_fk FOREIGN KEY (cot_event_id)
     references public.cot_event (id) MATCH SIMPLE
     ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT point_hae_fk FOREIGN KEY (point_hae,point_ce,tileX)
     REFERENCES public.cot_event (source_id,cot_type,how) MATCH SIMPLE
     ON UPDATE RESTRICT ON DELETE RESTRICT
);






