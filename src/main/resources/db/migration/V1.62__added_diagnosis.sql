CREATE TABLE if not exists diagnoses
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    name             varchar      NULL,
    lastname         varchar      NULL,
    age              int8         NULL,
    grade            varchar      NULL,
    uuid_student     uuid         NULL,
    diagnosis        varchar      NULL,
    CONSTRAINT diagnoses_pkey PRIMARY KEY (uuid)
);