-- Table: "sampleSchema".employeedetails

-- DROP TABLE "sampleSchema".employeedetails;

CREATE TABLE "sampleSchema".employeedetails
(
    empdetailsid character varying(50) COLLATE pg_catalog."default",
    empname character varying(45) COLLATE pg_catalog."default",
    address character varying(512) COLLATE pg_catalog."default",
    contactnumber bigint,
    contactperson character varying(45) COLLATE pg_catalog."default",
    hasspouse character(1) COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE "sampleSchema".employeedetails
    OWNER to postgres;