CREATE TABLE if not exists meshs
(
    uuid                 uuid         NOT NULL,
    code                 varchar(255) NULL,
    version              int8         NULL,
    created_at           timestamp    NULL,
    last_modified_at     timestamp    NULL,
    deleted_at           timestamp    NULL,
    deleted              bool         NULL DEFAULT false,
    axis                 varchar      null,
    content              varchar      null,
    achievements         varchar      null,
    achievement_indicator varchar      null,
    strategies           varchar      null,
    skills               varchar      null,
    classroom            uuid         null,
    subject              uuid         null,
    period               int8         null,
    CONSTRAINT meshs_pkey PRIMARY KEY (uuid)
);