CREATE TABLE if not exists post_reacts
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL     DEFAULT false,

    uuid_user        uuid         NULL,
    react            int8         not null default 0,
    uuid_post        uuid         null,
    uuid_comment     uuid         NULL,
    CONSTRAINT post_reacts_pkey PRIMARY KEY (uuid)
);