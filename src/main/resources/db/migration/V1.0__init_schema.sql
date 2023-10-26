CREATE TABLE if not exists companies
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    name             varchar(255) NULL,
    active           bool         NULL DEFAULT true,
    expire_date      timestamp    NULL,
    CONSTRAINT companies_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists users
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    name             varchar(255) NULL,
    username         varchar(255) NULL,
    password         varchar(255) NULL,
    role             varchar(255) NULL,
    uuid_company     uuid         NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists posts
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    deleted_at       timestamp    NULL,
    is_deleted       bool         NULL,
    last_modified_at timestamp    NULL,
    --table data
    description      varchar      NULL,
    comments         int8         NULL,
    CONSTRAINT posts_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists modules
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    name             varchar(255) NULL,
    "order"          int8         NULL,
    CONSTRAINT modules_pkey PRIMARY KEY (uuid)
);

-- DROP TABLE sub_modules;

CREATE TABLE if not exists sub_modules
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    name             varchar(255) NULL,
    route_name       varchar(255) NULL,
    uuid_module      uuid         NOT NULL,
    "order"          int8         NULL,
    CONSTRAINT sub_modules_pkey PRIMARY KEY (uuid)
);

-- DROP TABLE sub_module_users;

CREATE TABLE if not exists sub_module_users
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,
    uuid_user        uuid         NOT NULL,
    uuid_sub_module  uuid         NOT NULL,
    CONSTRAINT sub_module_users_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists calendar_tasks
(
    uuid             uuid          NOT NULL,
    code             varchar(255)  NULL,
    version          int8          NULL,
    created_at       timestamp     NULL,
    last_modified_at timestamp     NULL,
    deleted_at       timestamp     NULL,
    deleted          bool          NULL DEFAULT false,

    uuid_company     UUID          null,
    schedule_init    timestamp     null,
    hour             varchar       null,
    schedule_finish  timestamp     null,
    task_type        varchar       null,
    description      varchar(1000) null,
    assigned_to      UUID          null,
    uuid_client      UUID          null,
    CONSTRAINT calendar_tasks_pkey PRIMARY KEY (uuid)
);


CREATE TABLE if not exists clients
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_company     UUID         null,
    name             varchar      null,
    id               varchar      null,
    phone            varchar      null,
    CONSTRAINT clients_pkey PRIMARY KEY (uuid)
);

CREATE TABLE if not exists workers
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_company     UUID         null,
    name             varchar      null,
    id               varchar      null,
    phone            varchar      null,
    CONSTRAINT workers_pkey PRIMARY KEY (uuid)
);