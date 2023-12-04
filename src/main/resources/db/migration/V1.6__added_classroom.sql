CREATE TABLE if not exists classrooms
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_school      UUID         null,
    name             varchar(255) null,
    uuid_grade       UUID         null,
    year             int8         null DEFAULT 2023,
    CONSTRAINT classrooms_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists classrooms
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_student     UUID         null,
    uuid_classroom   UUID         null,
    CONSTRAINT classroom_students_pkey PRIMARY KEY (uuid)
);