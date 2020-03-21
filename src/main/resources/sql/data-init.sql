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
-- Data for Name: photo; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.photo (id, version, data, name, suffix) FROM stdin;
\.


--
-- Data for Name: user_account; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.user_account (id, version, is_banned, password, username) FROM stdin;
32	0	f	$2a$05$liN1q4inTShcELQ373gIeOyeVDl1mWqAMUEEDmC.jnMw1il7TWp0S	admin1
33	0	f	$2a$05$6w7s9gZZvfqw9ISAYMerM.I6zuZ55xWdKFv.ImSf7pbxs9PWUDt2q	admin2
34	0	f	$2a$05$TXrmZFnAZ2l68enU7HfNM.aaPC4lZ8zHqwkvht5bwaqeGxloJFoo2	admin3
\.


--
-- Data for Name: administrator; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.administrator (id, version, email, image, name, surname, telephone_number, photo_id, user_account_id) FROM stdin;
100	0	admin1@gmail.com	https://estaticos.muyinteresante.es/media/cache/400x300_thumb/uploads/images/dossier/5e6b9e535cafe8decee46032/fe-lix-rodri-guez-de-la-fuente.jpg	Name cero	Surname cero	+34 694567234	\N	32
101	0	admin2@gmail.com		Name uno	Surname uno	+34 694567235	\N	33
102	0	admin3@gmail.com		Name dos	Surname dos	+34 694567236	\N	34
\.


--
-- Data for Name: owner; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.owner (id, version, email, image, name, surname, telephone_number, photo_id, user_account_id, accumulated_months, iban) FROM stdin;
\.


--
-- Data for Name: renter; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.renter (id, version, email, image, name, surname, telephone_number, photo_id, user_account_id, iban) FROM stdin;
\.


--
-- Data for Name: smallholding; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.smallholding (id, version, address, description, farming_type, images, is_available, latitude, locality, longitude, max_duration, postal_code, price, province, size, status, title, owner_id) FROM stdin;
\.


--
-- Data for Name: valuation; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.valuation (id, version, mark, valuation_moment) FROM stdin;
\.


--
-- Data for Name: rent_out; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.rent_out (id, version, is_active, month, start_date, renter_id, smallholding_id, valuation_id) FROM stdin;
\.


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.comment (id, version, text, written_moment, rent_out_id) FROM stdin;
\.


--
-- Data for Name: customization; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.customization (id, version, discount_codes, eco_truki, email, gold_level, silver_level) FROM stdin;
1	0			ecoRenter@gmail.com	12	3
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: spring_dev
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- Data for Name: smallholding_photos; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.smallholding_photos (smallholding_id, photos_id) FROM stdin;
\.


--
-- Data for Name: user_account_authorities; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.user_account_authorities (user_account_id, authority) FROM stdin;
32	ADMIN
33	ADMIN
34	ADMIN
\.


--
-- PostgreSQL database dump complete
--

