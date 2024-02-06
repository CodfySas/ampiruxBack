CREATE TABLE if not exists resources
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL DEFAULT false,
    uuid_school        UUID          null,
    name      varchar(255) null,
    CONSTRAINT resources_pkey PRIMARY KEY (uuid)
);