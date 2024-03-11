ALTER TABLE public.classroom_resources ADD if not exists has_file boolean NULL DEFAULT false;
ALTER TABLE public.classroom_resources ADD if not exists ext varchar NULL DEFAULT '';