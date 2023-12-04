CREATE TABLE if not exists grades
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
    name             varchar(255) null,
    CONSTRAINT grades_pkey PRIMARY KEY
        (
         uuid
            )
);

ALTER TABLE public.users ADD if not exists actual_grade VARCHAR NULL;