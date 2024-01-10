CREATE TABLE if not exists notifications
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL DEFAULT false,
    uuid_user        UUID          null,
    description      varchar(1000) null,
    url_link         varchar(500)  null,
    viewed           boolean       null default false,
    datetime         timestamp     null,
    CONSTRAINT notifications_pkey PRIMARY KEY (uuid)
);