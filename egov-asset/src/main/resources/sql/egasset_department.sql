CREATE TABLE EGASSET_DEPARTMENT (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(250) NOT NULL,
	code CHARACTER VARYING(250) NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT PK_EGASSET_DEPARTMENT PRIMARY KEY (id)
);

CREATE SEQUENCE SEQ_EGASSET_DEPARTMENT INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;