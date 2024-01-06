CREATE TABLE if not exists student_subjects
(
    uuid                   uuid         NOT NULL,
    code                   varchar(255) NULL,
    version                int8         NULL,
    created_at             timestamp    NULL,
    last_modified_at       timestamp    NULL,
    deleted_at             timestamp    NULL,
    deleted                bool         NULL DEFAULT false,
    uuid_student           UUID         null,
    period                 int8         null,
    uuid_classroom_student uuid         null,
    uuid_subject           uuid         null,
    def                    float8       null,
    recovery               float8       null,
    CONSTRAINT student_subjects_pkey PRIMARY KEY (uuid)
);