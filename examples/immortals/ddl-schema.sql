DROP TABLE cot_event_position IF EXISTS;
DROP TABLE cot_event IF EXISTS;
DROP TABLE source IF EXISTS;

CREATE TABLE source
(
 source_id VARCHAR(16) NOT NULL,
 name VARCHAR(16),
 channel VARCHAR(16) NOT NULL,
 CONSTRAINT source_pkey PRIMARY KEY (source_id)
 );

CREATE TABLE cot_event
(
  id VARCHAR(16) NOT NULL,
  source_id VARCHAR(16) NOT NULL,
  cot_type VARCHAR(16) NOT NULL,
  how VARCHAR(16) NOT NULL,
  detail VARCHAR(400) NOT NULL,
  servertime VARCHAR(16) NOT NULL,
  CONSTRAINT cot_event_pkey PRIMARY KEY (id),
  CONSTRAINT source_pk FOREIGN KEY (source_id)
    REFERENCES public.source (source_id) MATCH SIMPLE
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE cot_event_position
(
  id VARCHAR(16) NOT NULL,
  point_hae VARCHAR(16) NOT NULL,
  point_ce VARCHAR(16) NOT NULL,
  point_le VARCHAR(16) NOT NULL,
  tileX VARCHAR(16) NOT NULL,
  tileY VARCHAR(16) NOT NULL,
  longitude VARCHAR(24) NOT NULL,
  latitude VARCHAR(24) NOT NULL,
  CONSTRAINT cot_event_position_pkey PRIMARY KEY (id),
  CONSTRAINT cot_event_fk FOREIGN KEY (id)
     REFERENCES public.cot_event (id) MATCH SIMPLE
     ON UPDATE RESTRICT ON DELETE RESTRICT
);
