ALTER TABLE public.users ALTER COLUMN actual_grade TYPE uuid USING actual_grade::uuid;
