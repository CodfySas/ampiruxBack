CREATE TABLE if not exists teachers
(
    uuid
                     uuid
                                  NOT
                                      NULL,
    code
                     varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_school      UUID         null,
    name             varchar(255)      null,
    dni              varchar(255)      null,
    phone            varchar(255)      null,
    document_type    varchar(255)      null,
    email            varchar(255)      null,
    address          varchar(255)      null,
    CONSTRAINT teachers_pkey PRIMARY KEY
        (
         uuid
            )
);