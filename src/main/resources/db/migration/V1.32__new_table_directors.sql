CREATE TABLE if not exists directors
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_teacher     uuid         null,
    uuid_classroom   uuid         null,
    CONSTRAINT directors_pkey PRIMARY KEY (uuid)
);