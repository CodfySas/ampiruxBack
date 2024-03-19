CREATE TABLE if not exists exam_responses
(
    uuid               uuid         NOT NULL,
    code               varchar(255) NULL,
    version            int8         NULL,
    created_at         timestamp    NULL,
    last_modified_at   timestamp    NULL,
    deleted_at         timestamp    NULL,
    deleted            bool         NULL DEFAULT false,

    description        varchar      NULL DEFAULT '',
    correct            boolean      null DEFAULT false,
    uuid_exam_question uuid         NULL,
    CONSTRAINT exam_responses_pkey PRIMARY KEY (uuid)
);