CREATE TABLE if not exists plannings
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL DEFAULT false,

    day              varchar       null,
    position         int8          null,

    area             varchar       null,
    goals            varchar       null,
    topic            varchar       null,
    activity         varchar       null,
    resources        varchar       null,

    classroom        uuid          null,
    subject          uuid          null,
    week             int8          null,

    date_range       varchar       null,
    observation      varchar(3000) null,
    status           varchar       NULL DEFAULT 'pending',
    user_review      uuid          NULL,

    CONSTRAINT planning_pkey PRIMARY KEY (uuid)
);