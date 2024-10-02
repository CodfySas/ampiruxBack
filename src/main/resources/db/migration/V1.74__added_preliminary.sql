CREATE TABLE if not exists preliminaries
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL     DEFAULT false,

    uuid_classroom   uuid          NULL,
    uuid_student     uuid          NULL,
    uuid_subject     uuid          NULL,
    target           varchar(3000) NULL,
    aspect           varchar(3000) NULL,
    observations     varchar(3000) NULL,
    period           int8          not null default 0,
    success          boolean       not null default false,
    CONSTRAINT preliminaries_pkey PRIMARY KEY (uuid)
);