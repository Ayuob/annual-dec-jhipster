INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', true, 'en', null, null, 'system', null, null, 'system', null);
INSERT INTO public.jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', true, 'en', null, null, 'system', null, null, 'system', null);
INSERT INTO public.jhi_authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO public.jhi_authority (name) VALUES ('ROLE_USER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
