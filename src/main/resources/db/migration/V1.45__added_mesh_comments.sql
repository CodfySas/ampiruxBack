ALTER TABLE public.meshs ADD if not exists observation varchar(3000) NULL DEFAULT '';
ALTER TABLE public.meshs ADD if not exists status varchar NULL DEFAULT 'pending';