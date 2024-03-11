CREATE TABLE if not exists classroom_resources
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    name             varchar      null,
    type             varchar      null,

    classroom        uuid         null,
    subject          uuid         null,
    period           int8         null,

    finish_time      DATE         null,
    duration_time    int8         null,
    description      varchar      NULL DEFAULT '',
    last_hour        varchar      NULL DEFAULT '23:59',
    CONSTRAINT classroom_resources_pkey PRIMARY KEY (uuid)
);