CREATE TABLE IF NOT EXISTS public.permission (
  id SERIAL,
  description  character varying(80),
  CONSTRAINT permission_pkey PRIMARY KEY (id)
);