CREATE TABLE if not exists schools
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
    CONSTRAINT schools_pkey PRIMARY KEY (uuid)
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
    uuid_school      uuid         NOT NULL,
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
    ordered            int8         NULL,
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
    uuid_module      uuid         NULL,
    ordered            int8         NULL,
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

CREATE TABLE if not exists clients
(
    uuid             uuid         NOT NULL,
    code             varchar(255) NULL,
    version          int8         NULL,
    created_at       timestamp    NULL,
    last_modified_at timestamp    NULL,
    deleted_at       timestamp    NULL,
    deleted          bool         NULL DEFAULT false,

    uuid_school      UUID         null,
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

    uuid_school      UUID         null,
    name             varchar      null,
    id               varchar      null,
    phone            varchar      null,
    CONSTRAINT workers_pkey PRIMARY KEY (uuid)
);