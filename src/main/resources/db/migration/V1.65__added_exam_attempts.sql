CREATE TABLE if not exists exam_attempts
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_student     uuid         NULL,
    uuid_exam        uuid         NULL,
    note             float8       null,
    observation      varchar      NULL,
    CONSTRAINT exam_attempts_pkey PRIMARY KEY (uuid)
);

ALTER TABLE public.exam_user_responses ADD if not exists uuid_attempt uuid null;