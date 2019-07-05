-- Table: "sampleSchema".employee

-- DROP TABLE "sampleSchema".employee;

CREATE TABLE "sampleSchema".employee
(
    empid character varying(50) COLLATE pg_catalog."default",
    departmentid character varying(45) COLLATE pg_catalog."default",
    joiningdate timestamp without time zone,
    confirmationdate timestamp without time zone,
    reportingmanager character varying(45) COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "sampleSchema".employee
    OWNER to postgres;