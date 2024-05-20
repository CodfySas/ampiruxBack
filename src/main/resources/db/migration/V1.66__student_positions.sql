CREATE TABLE if not exists student_positions
(
    uuid                   uuid         NOT NULL,
    code                   varchar(255) NULL,
    version                int8         NULL,
    created_at             timestamp    NULL,
    last_modified_at       timestamp    NULL,
    deleted_at             timestamp    NULL,
    deleted                bool         NULL DEFAULT false,

    uuid_classroom_student uuid         NULL,
    uuid_student           uuid         NULL,
    period                 int8         null,
    position               int8         null,
    prom                   float8       NULL,
    CONSTRAINT student_positions_pkey PRIMARY KEY (uuid)
);

ALTER TABLE public.exam_user_responses
    ADD if not exists uuid_attempt uuid null;