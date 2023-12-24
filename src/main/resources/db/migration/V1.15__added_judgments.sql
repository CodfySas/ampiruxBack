CREATE TABLE if not exists judgments
(
    uuid                   uuid         NOT NULL,
    code                   varchar(255) NULL,
    version                int8         NULL,
    created_at             timestamp    NULL,
    last_modified_at       timestamp    NULL,
    deleted_at             timestamp    NULL,
    deleted                bool         NULL DEFAULT false,
    uuid_student           UUID         null,
    name              varchar(255) null,
    period                   int8       null,
    uuid_classroom_student uuid         null,
    uuid_subject           uuid         null,
    CONSTRAINT judgments_pkey PRIMARY KEY (uuid)
);