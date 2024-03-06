ALTER TABLE public.classroom_students ADD if not exists position int8 NULL DEFAULT 0;
ALTER TABLE public.classroom_students ADD if not exists prom float8 NULL;