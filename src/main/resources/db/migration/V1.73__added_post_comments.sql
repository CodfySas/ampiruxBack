CREATE TABLE if not exists post_comments
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL     DEFAULT false,

    uuid_user        uuid          NULL,
    description      varchar(3000) NULL,
    responses        int8          not null default 0,
    likes            int8          not null default 0,
    loved            int8          not null default 0,
    wows             int8          not null default 0,
    interesting      int8          not null default 0,
    dislikes         int8          not null default 0,
    reacts           int8          not null default 0,
    uuid_post        uuid          null,
    is_response      boolean       not null default false,
    uuid_parent      uuid          NULL,
    CONSTRAINT post_comments_pkey PRIMARY KEY (uuid)
);