ALTER TABLE public.schools ADD if not exists actual_year int8 NOT NULL DEFAULT 2023;
ALTER TABLE public.school_periods ADD if not exists actual_year int8 NOT NULL DEFAULT 2023;
