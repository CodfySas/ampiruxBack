CREATE TABLE if not exists attendances
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    day              int8         null,
    month            int8         null,
    uuid_school      uuid         null,
    uuid_classroom   uuid         null,
    uuid_subject   uuid         null,
    CONSTRAINT attendances_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists attendance_fails
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_attendance  uuid         null,
    reason           varchar      null,
    uuid_student     uuid         null,
    CONSTRAINT attendance_fails_pkey PRIMARY KEY (uuid)
);