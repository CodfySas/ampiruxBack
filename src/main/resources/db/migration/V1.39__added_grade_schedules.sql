ALTER TABLE public.grades ADD if not exists hour_init varchar NULL DEFAULT '8:00';
ALTER TABLE public.grades ADD if not exists hour_finish varchar NULL DEFAULT '8:00';
ALTER TABLE public.grades ADD if not exists recess_init varchar NULL DEFAULT '10:00';
ALTER TABLE public.grades ADD if not exists recess_finish varchar NULL DEFAULT '10:30';
ALTER TABLE public.grades ADD if not exists recessa_init varchar NULL;
ALTER TABLE public.grades ADD if not exists recessa_finish varchar NULL;
ALTER TABLE public.grades ADD if not exists duration int8 NULL DEFAULT 60;
ALTER TABLE public.grades ADD if not exists recess int8 NULL DEFAULT 1;