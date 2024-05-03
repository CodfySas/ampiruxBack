CREATE TABLE if not exists exam_user_responses
(
    uuid               uuid         NOT NULL,
    code               varchar(255) NULL,
    version            int8         NULL,
    created_at         timestamp    NULL,
    last_modified_at   timestamp    NULL,
    deleted_at         timestamp    NULL,
    deleted            bool         NULL DEFAULT false,

    uuid_exam_question uuid         NULL,
    uuid_exam_response uuid         NULL,
    correct            boolean      null DEFAULT false,
    response           varchar      NULL,
    CONSTRAINT exam_user_responses_pkey PRIMARY KEY (uuid)
);