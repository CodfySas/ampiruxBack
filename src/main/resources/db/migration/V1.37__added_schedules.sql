CREATE TABLE if not exists schedules
(
    uuid                   uuid         NOT NULL,
    code                   varchar(255) NULL,
    version                int8         NULL,
    created_at             timestamp    NULL,
    last_modified_at       timestamp    NULL,
    deleted_at             timestamp    NULL,
    deleted                bool         NULL DEFAULT false,
    init                   varchar      null,
    finish                 varchar      null,
    day_of_week            int8         null,
    uuid_grade_subject     uuid         null,
    uuid_classroom_subject uuid         null,
    uuid_school            uuid         null,
    uuid_classroom         uuid         null,
    CONSTRAINT schedules_pkey PRIMARY KEY (uuid)
);