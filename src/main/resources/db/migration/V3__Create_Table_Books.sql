-- Create table person
CREATE TABLE IF NOT EXISTS public.books (
      id SERIAL,
      author TEXT,
      launch_date date NOT NULL,
      price decimal(65,2) NOT NULL,
      title TEXT,
      CONSTRAINT books_pkey PRIMARY KEY (id)
);
