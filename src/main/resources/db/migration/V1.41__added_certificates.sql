CREATE TABLE if not exists certificates
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    type             varchar      null,
    approved         bool         null,
    uuid_school      uuid         null,
    uuid_user        uuid         null,
    approved_at      timestamp    null,
    CONSTRAINT certificates_pkey PRIMARY KEY (uuid)
);