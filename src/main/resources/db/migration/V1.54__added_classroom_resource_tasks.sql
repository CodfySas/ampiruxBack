CREATE TABLE if not exists classroom_resource_tasks
(
    uuid                    uuid         NOT NULL,
    code                    varchar(255) NULL,
    version                 int8         NULL,
    created_at              timestamp    NULL,
    last_modified_at        timestamp    NULL,
    deleted_at              timestamp    NULL,
    deleted                 bool         NULL DEFAULT false,
    uuid_classroom_resource uuid         null,
    uuid_student            uuid         null,
    uuid_classroom_student  uuid         null,

    name                    varchar      null,
    ext                     varchar      null,
    has_file                varchar      null,

    description             varchar      NULL DEFAULT '',
    submit_at               DATE         null,
    submit_at_hour          varchar      NULL DEFAULT '00:00',
    note                    float8       null,
    observation             varchar      null,
    CONSTRAINT classroom_resource_tasks_pkey PRIMARY KEY (uuid)
);