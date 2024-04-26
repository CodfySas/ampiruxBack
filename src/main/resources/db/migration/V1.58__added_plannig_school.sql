ALTER TABLE public.schools ADD if not exists planning_type varchar null DEFAULT 'group';
ALTER TABLE public.plannings ADD if not exists uuid_teacher uuid null;