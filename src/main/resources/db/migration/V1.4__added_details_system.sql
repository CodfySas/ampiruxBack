ALTER TABLE public.sub_modules ADD if not exists parent BOOLEAN DEFAULT FALSE NULL;
ALTER TABLE public.sub_modules ADD if not exists parent_uuid uuid NULL;
