//Remove public.


Create sequence seq_lams_agreement;

Create sequence seq_lams_rent_increment;


CREATE TABLE eglams_rentincrementtype
(
  id bigint NOT NULL,
  type character varying(64) NOT NULL,
  asset_category bigint NOT NULL, 
  flat_amount decimal(12,2) NOT NULL, 
  fromdate timestamp without time zone NOT NULL, 
  todate timestamp without time zone NOT NULL, 
  percentage decimal (5,2) NOT NULL, 
  CONSTRAINT pk_eglams_rentincrementtype PRIMARY KEY (id)
);



CREATE TABLE eglams_agreement
(
  id bigint NOT NULL,
  agreement_date timestamp without time zone NOT NULL, 
  agreement_no character varying(64) NOT NULL,    // index
  bank_guarantee_amount double precision(12,2), 
  bank_guarantee_date timestamp without time zone,
  case_no character varying(64),
  commencement_date timestamp without time zone NOT NULL, 
  council_date timestamp without time zone,
  council_number character varying(64),
  expiry_date timestamp without time zone,
  nature_of_allotment character varying(16),
  order_date timestamp without time zone, 
  order_details character varying(256),
  order_no character varying(64),
  payment_cycle character varying(16) NOT NULL, 
  registration_fee double precision(12,2), 
  remarks character varying(256),
  rent double precision(12,2) NOT NULL, 
  rr_reading_no character varying(64), 
  security_deposit double precision(12,2),
  security_deposit_date timestamp without time zone,
  solvency_certificate_date timestamp without time zone,
  solvency_certificate_no character varying(64),
  status character varying(32) NOT NULL,
  tin_number character varying(64), 
  tender_date timestamp without time zone,
  tender_number character varying(64), 
  trade_license_number character varying(64),
  allottee bigint NOT NULL, 
  asset bigint NOT NULL, 
  rent_increment_method bigint NOT NULL,
  CONSTRAINT pk_eglams_agreement PRIMARY KEY (id),  
  CONSTRAINT fk_rent_incrementmethod_type FOREIGN KEY (rent_increment_method) 
      REFERENCES eglams_rentincrementtype (id) 
 
);



