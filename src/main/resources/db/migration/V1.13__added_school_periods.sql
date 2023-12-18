CREATE TABLE if not exists school_periods
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    number           int8         null,
    init             timestamp    null,
    finish           timestamp    null,
    uuid_school      UUID         null,
    CONSTRAINT school_periods_pkey PRIMARY KEY (uuid)
);