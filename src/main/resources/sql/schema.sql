--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: administrator; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.administrator (
    id integer NOT NULL,
    version integer NOT NULL,
    email character varying(255),
    name character varying(255),
    surname character varying(255),
    telephone_number character varying(255),
    photo_id integer,
    user_account_id integer NOT NULL
);


ALTER TABLE public.administrator OWNER TO spring_dev;

--
-- Name: comment; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.comment (
    id integer NOT NULL,
    version integer NOT NULL,
    text character varying(255),
    written_moment timestamp without time zone NOT NULL,
    rent_out_id integer NOT NULL
);


ALTER TABLE public.comment OWNER TO spring_dev;

--
-- Name: customization; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.customization (
    id integer NOT NULL,
    version integer NOT NULL,
    discount_codes character varying(255),
    email character varying(255),
    gold_level integer NOT NULL,
    silver_level integer NOT NULL,
    CONSTRAINT customization_gold_level_check CHECK ((gold_level >= 9)),
    CONSTRAINT customization_silver_level_check CHECK (((silver_level >= 3) AND (silver_level <= 8)))
);


ALTER TABLE public.customization OWNER TO spring_dev;

--
-- Name: eco_truki; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.eco_truki (
    id integer NOT NULL,
    version integer NOT NULL,
    description character varying(255),
    moment timestamp without time zone NOT NULL,
    title character varying(255)
);


ALTER TABLE public.eco_truki OWNER TO spring_dev;

--
-- Name: eco_truki_photos; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.eco_truki_photos (
    eco_truki_id integer NOT NULL,
    photos_id integer NOT NULL
);


ALTER TABLE public.eco_truki_photos OWNER TO spring_dev;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: spring_dev
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO spring_dev;

--
-- Name: owner; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.owner (
    id integer NOT NULL,
    version integer NOT NULL,
    email character varying(255),
    name character varying(255),
    surname character varying(255),
    telephone_number character varying(255),
    photo_id integer,
    user_account_id integer NOT NULL,
    accumulated_months integer NOT NULL,
    iban character varying(255),
    CONSTRAINT owner_accumulated_months_check CHECK ((accumulated_months >= 0))
);


ALTER TABLE public.owner OWNER TO spring_dev;

--
-- Name: photo; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.photo (
    id integer NOT NULL,
    version integer NOT NULL,
    data oid NOT NULL,
    name character varying(255),
    suffix character varying(255)
);


ALTER TABLE public.photo OWNER TO spring_dev;

--
-- Name: rent_out; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.rent_out (
    id integer NOT NULL,
    version integer NOT NULL,
    is_active boolean NOT NULL,
    month integer NOT NULL,
    start_date timestamp without time zone NOT NULL,
    renter_id integer NOT NULL,
    smallholding_id integer NOT NULL,
    valuation_id integer,
    CONSTRAINT rent_out_month_check CHECK ((month >= 0))
);


ALTER TABLE public.rent_out OWNER TO spring_dev;

--
-- Name: renter; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.renter (
    id integer NOT NULL,
    version integer NOT NULL,
    email character varying(255),
    name character varying(255),
    surname character varying(255),
    telephone_number character varying(255),
    photo_id integer,
    user_account_id integer NOT NULL,
    iban character varying(255)
);


ALTER TABLE public.renter OWNER TO spring_dev;

--
-- Name: smallholding; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.smallholding (
    id integer NOT NULL,
    version integer NOT NULL,
    address character varying(255),
    description character varying(255),
    farming_type character varying(255),
    is_argumented boolean NOT NULL,
    is_available boolean NOT NULL,
    latitude character varying(255),
    locality character varying(255),
    longitude character varying(255),
    max_duration integer,
    postal_code character varying(255),
    price double precision NOT NULL,
    province character varying(255),
    size integer NOT NULL,
    status character varying(255),
    title character varying(255),
    owner_id integer NOT NULL,
    CONSTRAINT smallholding_max_duration_check CHECK ((max_duration >= 1)),
    CONSTRAINT smallholding_size_check CHECK ((size >= 1))
);


ALTER TABLE public.smallholding OWNER TO spring_dev;

--
-- Name: smallholding_photos; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.smallholding_photos (
    smallholding_id integer NOT NULL,
    photos_id integer NOT NULL
);


ALTER TABLE public.smallholding_photos OWNER TO spring_dev;

--
-- Name: user_account; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.user_account (
    id integer NOT NULL,
    version integer NOT NULL,
    is_banned boolean NOT NULL,
    password character varying(60),
    username character varying(60)
);


ALTER TABLE public.user_account OWNER TO spring_dev;

--
-- Name: user_account_authorities; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.user_account_authorities (
    user_account_id integer NOT NULL,
    authority character varying(255)
);


ALTER TABLE public.user_account_authorities OWNER TO spring_dev;

--
-- Name: valuation; Type: TABLE; Schema: public; Owner: spring_dev; Tablespace: 
--

CREATE TABLE public.valuation (
    id integer NOT NULL,
    version integer NOT NULL,
    mark integer NOT NULL,
    valuation_moment timestamp without time zone NOT NULL,
    CONSTRAINT valuation_mark_check CHECK (((mark >= 0) AND (mark <= 5)))
);


ALTER TABLE public.valuation OWNER TO spring_dev;

--
-- Name: administrator_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT administrator_pkey PRIMARY KEY (id);


--
-- Name: comment_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (id);


--
-- Name: customization_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.customization
    ADD CONSTRAINT customization_pkey PRIMARY KEY (id);


--
-- Name: eco_truki_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.eco_truki
    ADD CONSTRAINT eco_truki_pkey PRIMARY KEY (id);


--
-- Name: owner_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.owner
    ADD CONSTRAINT owner_pkey PRIMARY KEY (id);


--
-- Name: photo_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- Name: rent_out_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.rent_out
    ADD CONSTRAINT rent_out_pkey PRIMARY KEY (id);


--
-- Name: renter_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.renter
    ADD CONSTRAINT renter_pkey PRIMARY KEY (id);


--
-- Name: smallholding_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.smallholding
    ADD CONSTRAINT smallholding_pkey PRIMARY KEY (id);


--
-- Name: uk_2a5vcjo3stlfcwadosjfq49l1; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT uk_2a5vcjo3stlfcwadosjfq49l1 UNIQUE (user_account_id);


--
-- Name: uk_32merbwan7h6a2mgwcxpyxlts; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.renter
    ADD CONSTRAINT uk_32merbwan7h6a2mgwcxpyxlts UNIQUE (user_account_id);


--
-- Name: uk_33kylxybbnpwcxp2myq46wk2w; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.smallholding_photos
    ADD CONSTRAINT uk_33kylxybbnpwcxp2myq46wk2w UNIQUE (photos_id);


--
-- Name: uk_8lx5melb9aiqsx6uaw8ssbb5r; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.renter
    ADD CONSTRAINT uk_8lx5melb9aiqsx6uaw8ssbb5r UNIQUE (email);


--
-- Name: uk_castjbvpeeus0r8lbpehiu0e4; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT uk_castjbvpeeus0r8lbpehiu0e4 UNIQUE (username);


--
-- Name: uk_hc0nwk401f7t7pohcq2vounjc; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.owner
    ADD CONSTRAINT uk_hc0nwk401f7t7pohcq2vounjc UNIQUE (user_account_id);


--
-- Name: uk_jj3mmcc0vjobqibj67dvuwihk; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT uk_jj3mmcc0vjobqibj67dvuwihk UNIQUE (email);


--
-- Name: uk_kcaoebbgb82ro5cw9nqhw19qb; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.owner
    ADD CONSTRAINT uk_kcaoebbgb82ro5cw9nqhw19qb UNIQUE (email);


--
-- Name: uk_kwvi1eq3k3nnn2jc10wyci9jv; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.customization
    ADD CONSTRAINT uk_kwvi1eq3k3nnn2jc10wyci9jv UNIQUE (email);


--
-- Name: uk_l9xnrhekwkskovh4ytlgvpeo9; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.eco_truki_photos
    ADD CONSTRAINT uk_l9xnrhekwkskovh4ytlgvpeo9 UNIQUE (photos_id);


--
-- Name: user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (id);


--
-- Name: valuation_pkey; Type: CONSTRAINT; Schema: public; Owner: spring_dev; Tablespace: 
--

ALTER TABLE ONLY public.valuation
    ADD CONSTRAINT valuation_pkey PRIMARY KEY (id);


--
-- Name: fk19104dxu3xkf40ux1yah94y3x; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.smallholding_photos
    ADD CONSTRAINT fk19104dxu3xkf40ux1yah94y3x FOREIGN KEY (smallholding_id) REFERENCES public.smallholding(id);


--
-- Name: fk2w4idcn2jnkaxbh3r2pebf5ie; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.rent_out
    ADD CONSTRAINT fk2w4idcn2jnkaxbh3r2pebf5ie FOREIGN KEY (renter_id) REFERENCES public.renter(id);


--
-- Name: fk3ej8yskij2u8bi60xbupb37at; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.eco_truki_photos
    ADD CONSTRAINT fk3ej8yskij2u8bi60xbupb37at FOREIGN KEY (photos_id) REFERENCES public.photo(id);


--
-- Name: fk_2a5vcjo3stlfcwadosjfq49l1; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT fk_2a5vcjo3stlfcwadosjfq49l1 FOREIGN KEY (user_account_id) REFERENCES public.user_account(id);


--
-- Name: fk_2mp2pw1716eb4hf20jtowwx3s; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.renter
    ADD CONSTRAINT fk_2mp2pw1716eb4hf20jtowwx3s FOREIGN KEY (photo_id) REFERENCES public.photo(id);


--
-- Name: fk_32merbwan7h6a2mgwcxpyxlts; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.renter
    ADD CONSTRAINT fk_32merbwan7h6a2mgwcxpyxlts FOREIGN KEY (user_account_id) REFERENCES public.user_account(id);


--
-- Name: fk_hc0nwk401f7t7pohcq2vounjc; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.owner
    ADD CONSTRAINT fk_hc0nwk401f7t7pohcq2vounjc FOREIGN KEY (user_account_id) REFERENCES public.user_account(id);


--
-- Name: fk_q8xtaapphhoue8f1skpjv81p7; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT fk_q8xtaapphhoue8f1skpjv81p7 FOREIGN KEY (photo_id) REFERENCES public.photo(id);


--
-- Name: fk_r4iqvt4tk4jpapqdqb9rsfy07; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.owner
    ADD CONSTRAINT fk_r4iqvt4tk4jpapqdqb9rsfy07 FOREIGN KEY (photo_id) REFERENCES public.photo(id);


--
-- Name: fkb78d844pinbt9yblhjx7svm5d; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.smallholding_photos
    ADD CONSTRAINT fkb78d844pinbt9yblhjx7svm5d FOREIGN KEY (photos_id) REFERENCES public.photo(id);


--
-- Name: fkbw5w7l7d1djjia82bwqjkjndr; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.rent_out
    ADD CONSTRAINT fkbw5w7l7d1djjia82bwqjkjndr FOREIGN KEY (valuation_id) REFERENCES public.valuation(id);


--
-- Name: fki1vsr0uw536n5xewcq6hupv0b; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.rent_out
    ADD CONSTRAINT fki1vsr0uw536n5xewcq6hupv0b FOREIGN KEY (smallholding_id) REFERENCES public.smallholding(id);


--
-- Name: fkjei8pnwif8vhbu7n0t1nkf5tf; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fkjei8pnwif8vhbu7n0t1nkf5tf FOREIGN KEY (rent_out_id) REFERENCES public.rent_out(id);


--
-- Name: fkqg5yuqktw7kjmodb7k1rg3f2o; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.user_account_authorities
    ADD CONSTRAINT fkqg5yuqktw7kjmodb7k1rg3f2o FOREIGN KEY (user_account_id) REFERENCES public.user_account(id);


--
-- Name: fkt41w5r3e6kec8wt0oq2r20cs1; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.smallholding
    ADD CONSTRAINT fkt41w5r3e6kec8wt0oq2r20cs1 FOREIGN KEY (owner_id) REFERENCES public.owner(id);


--
-- Name: fktii04f1l18by9qy2gdkkol4lb; Type: FK CONSTRAINT; Schema: public; Owner: spring_dev
--

ALTER TABLE ONLY public.eco_truki_photos
    ADD CONSTRAINT fktii04f1l18by9qy2gdkkol4lb FOREIGN KEY (eco_truki_id) REFERENCES public.eco_truki(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

