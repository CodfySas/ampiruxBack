CREATE TABLE if not exists student_notes
(
    uuid                   uuid         NOT NULL,
    code                   varchar(255) NULL,
    version                int8         NULL,
    created_at             timestamp    NULL,
    last_modified_at       timestamp    NULL,
    deleted_at             timestamp    NULL,
    deleted                bool         NULL DEFAULT false,
    uuid_student           UUID         null,
    number                 int8         null,
    note_name              varchar(255) null,
    note                   float8       null,
    uuid_classroom_student uuid         null,
    uuid_subject           uuid         null,
    CONSTRAINT student_notes_pkey PRIMARY KEY (uuid)
);