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
3200	0	f	$2a$05$liN1q4inTShcELQ373gIeOyeVDl1mWqAMUEEDmC.jnMw1il7TWp0S	admin1
3300	0	f	$2a$05$6w7s9gZZvfqw9ISAYMerM.I6zuZ55xWdKFv.ImSf7pbxs9PWUDt2q	admin2
3400	0	f	$2a$05$TXrmZFnAZ2l68enU7HfNM.aaPC4lZ8zHqwkvht5bwaqeGxloJFoo2	admin3
\.


--
-- Data for Name: administrator; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.administrator (id, version, email, name, surname, telephone_number, photo_id, user_account_id) FROM stdin;
100	0	admin1@gmail.com	María José	Martinez de Irujo	+34 694567234	\N	3200
101	0	admin2@gmail.com	Sofía	Corona Vil	+34 694567235	\N	3300
102	0	admin3@gmail.com	Ramón	Gómez de la Serna	+34 694567236	\N	3400
\.


--
-- Data for Name: owner; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.owner (id, version, email, name, surname, telephone_number, photo_id, user_account_id, accumulated_months, iban) FROM stdin;
\.


--
-- Data for Name: smallholding; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.smallholding (id, version, address, description, farming_type, is_argumented, is_available, latitude, locality, longitude, max_duration, postal_code, price, province, size, status, title, owner_id) FROM stdin;
\.


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.comment (id, version, text, written_moment, actor_id, smallholding_id) FROM stdin;
\.


--
-- Data for Name: credit_card; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.credit_card (id, version, brand_name, cvv_code, expiration_month, expiration_year, holder_name, number) FROM stdin;
\.


--
-- Data for Name: customization; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.customization (id, version, credit_card_makes, email, gold_level, max_size_photo, silver_level) FROM stdin;
1	0	MASTERCARD,VISA,AMERICAN EXPRESS,DISCOVER,JCB	ecoRenter@gmail.com	12	5	3
\.


--
-- Data for Name: eco_truki; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.eco_truki (id, version, description, moment, title) FROM stdin;
\.


--
-- Data for Name: eco_truki_photos; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.eco_truki_photos (eco_truki_id, photos_id) FROM stdin;
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: spring_dev
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- Data for Name: provider_discount_code; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.provider_discount_code (id, version, discount_codes, link_page, name) FROM stdin;
\.


--
-- Data for Name: renter; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.renter (id, version, email, name, surname, telephone_number, photo_id, user_account_id) FROM stdin;
\.


--
-- Data for Name: valuation; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.valuation (id, version, mark, valuation_moment) FROM stdin;
\.


--
-- Data for Name: rent_out; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.rent_out (id, version, is_active, month, start_date, credit_card_id, renter_id, smallholding_id, valuation_id) FROM stdin;
\.


--
-- Data for Name: renter_credit_cards; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.renter_credit_cards (renter_id, credit_cards_id) FROM stdin;
\.


--
-- Data for Name: smallholding_photos; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.smallholding_photos (smallholding_id, photos_id) FROM stdin;
\.


--
-- Data for Name: user_account_authorities; Type: TABLE DATA; Schema: public; Owner: spring_dev
--

COPY public.user_account_authorities (user_account_id, authority) FROM stdin;
3200	ADMIN
3300	ADMIN
3400	ADMIN
\.


--
-- PostgreSQL database dump complete
--

