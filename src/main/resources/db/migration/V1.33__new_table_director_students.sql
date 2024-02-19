CREATE TABLE if not exists director_students
(
    uuid                   uuid          NOT NULL,
    code                   varchar(255)  NULL,
    version                int8          NULL,
    created_at             timestamp     NULL,
    last_modified_at       timestamp     NULL,
    deleted_at             timestamp     NULL,
    deleted                bool          NULL DEFAULT false,
    uuid_classroom_student uuid          null,
    uuid_student           uuid          null,
    period                 int8          null,
    description            varchar(1000) null,
    CONSTRAINT director_students_pkey PRIMARY KEY (uuid)
);