CREATE TABLE if not exists logs
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_user        uuid         NULL,
    movement         varchar      NULL,
    day              DATE         null,
    hour             varchar      null,
    status           varchar      NULL,
    detail           varchar      NULL,
    CONSTRAINT logs_pkey PRIMARY KEY (uuid)
);