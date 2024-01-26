ALTER TABLE public.subjects ADD if not exists uuid_parent uuid NULL;
ALTER TABLE public.subjects ADD if not exists is_parent boolean NULL default false;