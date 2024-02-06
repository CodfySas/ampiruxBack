CREATE TABLE if not exists calendar_tasks
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL DEFAULT false,
    uuid_school      UUID          null,
    day              date          null,
    hour_init        varchar(255)  null,
    hour_finish      varchar(255)  null,
    task_type        varchar(255)  null,
    description      varchar(1000) null,
    assigned_to      uuid          null,
    uuid_resource    uuid          null,
    CONSTRAINT calendar_tasks_pkey PRIMARY KEY (uuid)
);