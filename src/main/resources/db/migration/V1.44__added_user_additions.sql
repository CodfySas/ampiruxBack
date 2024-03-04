ALTER TABLE public.users ADD if not exists active boolean NULL DEFAULT true;
ALTER TABLE public.users ADD if not exists super_user boolean NULL DEFAULT FALSE;
