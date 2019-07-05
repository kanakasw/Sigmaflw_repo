--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2
-- Dumped by pg_dump version 11.2

-- Started on 2019-07-05 15:17:38

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "SamplePostgresDb";
--
-- TOC entry 2817 (class 1262 OID 16393)
-- Name: SamplePostgresDb; Type: DATABASE; Schema: -; Owner: vivek
--

CREATE DATABASE "SamplePostgresDb" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_India.1252' LC_CTYPE = 'English_India.1252';


ALTER DATABASE "SamplePostgresDb" OWNER TO vivek;

\connect "SamplePostgresDb"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 8 (class 2615 OID 16400)
-- Name: sampleSchema; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "sampleSchema";


ALTER SCHEMA "sampleSchema" OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 197 (class 1259 OID 16479)
-- Name: employee; Type: TABLE; Schema: sampleSchema; Owner: postgres
--

CREATE TABLE "sampleSchema".employee (
    empid character varying(50),
    departmentid character varying(45),
    joiningdate timestamp without time zone,
    confirmationdate timestamp without time zone,
    reportingmanager character varying(45)
);


ALTER TABLE "sampleSchema".employee OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16496)
-- Name: employeedetails; Type: TABLE; Schema: sampleSchema; Owner: postgres
--

CREATE TABLE "sampleSchema".employeedetails (
    empdetailsid character varying(50),
    empname character varying(45),
    address character varying(512),
    contactnumber bigint,
    contactperson character varying(45),
    hasspouse character(1)
);


ALTER TABLE "sampleSchema".employeedetails OWNER TO postgres;

--
-- TOC entry 2810 (class 0 OID 16479)
-- Dependencies: 197
-- Data for Name: employee; Type: TABLE DATA; Schema: sampleSchema; Owner: postgres
--



--
-- TOC entry 2811 (class 0 OID 16496)
-- Dependencies: 198
-- Data for Name: employeedetails; Type: TABLE DATA; Schema: sampleSchema; Owner: postgres
--



-- Completed on 2019-07-05 15:17:38

--
-- PostgreSQL database dump complete
--

