CREATE TABLE if not exists exam_questions
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    description      varchar      NULL DEFAULT '',
    type             varchar      null,
    uuid_exam        uuid         NULL,
    ordered          int8         NULL,
    CONSTRAINT exam_questions_pkey PRIMARY KEY (uuid)
);