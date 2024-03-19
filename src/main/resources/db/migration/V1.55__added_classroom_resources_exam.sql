ALTER TABLE public.classroom_resources
    ADD if not exists init_time DATE null;
ALTER TABLE public.classroom_resources
    ADD if not exists init_hour varchar NULL DEFAULT '00:00';
ALTER TABLE public.classroom_resources
    ADD if not exists attempts int8 null;