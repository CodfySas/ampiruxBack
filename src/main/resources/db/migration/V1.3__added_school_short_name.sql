ALTER TABLE public.schools ADD if not exists short_name varchar NULL;
ALTER TABLE public.users ADD if not exists lastname varchar NULL;
ALTER TABLE public.users ADD if not exists document_type varchar NULL;
ALTER TABLE public.users ADD if not exists dni varchar NULL;
ALTER TABLE public.teachers ADD if not exists lastname varchar NULL;
ALTER TABLE public.users ADD if not exists uuid_role uuid NULL;
