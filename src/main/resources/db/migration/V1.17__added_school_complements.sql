ALTER TABLE public.schools ADD if not exists enabled_teacher boolean NOT NULL DEFAULT true;
ALTER TABLE public.schools ADD if not exists enabled_student boolean NOT NULL DEFAULT true;
ALTER TABLE public.schools ADD if not exists max_note int8 NOT NULL DEFAULT 5;
ALTER TABLE public.schools ADD if not exists min_note int8 NOT NULL DEFAULT 3;
ALTER TABLE public.schools ADD if not exists to_lose int8 NOT NULL DEFAULT 3;