CREATE TABLE if not exists classroom_subjects
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_classroom   UUID         null,
    uuid_subject     UUID         null,
    uuid_teacher     UUID         null,
    CONSTRAINT classroom_subjects_pkey PRIMARY KEY (uuid)
);