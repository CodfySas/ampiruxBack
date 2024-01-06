ALTER TABLE public.schools ADD if not exists enabled_final_recovery boolean NOT NULL DEFAULT false;
ALTER TABLE public.school_periods ADD if not exists recovery boolean NOT NULL DEFAULT false;