-- ========================================
-- Tabla USERS
-- ========================================
CREATE TABLE IF NOT EXISTS users
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    username         VARCHAR(255) NULL,
    password         VARCHAR(255) NULL,
    name             VARCHAR(255) NULL,
    lastname         VARCHAR(255) NULL,
    role             VARCHAR(255) NULL,
    active           BOOLEAN DEFAULT TRUE,
    phone            VARCHAR(255) NULL,
    email            VARCHAR(255) NULL,
    image            VARCHAR      NULL,

    CONSTRAINT pk_users PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS notifications
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN               DEFAULT FALSE,

    user_uuid        uuid         NULL,
    sender_uuid      uuid         NULL,
    type             varchar(500) NULL,
    message          varchar      NULL,
    read             boolean      NOT NULL default false,

    CONSTRAINT pk_notifications PRIMARY KEY (uuid),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_uuid) REFERENCES users (uuid)

);
ALTER TABLE notifications
    ADD COLUMN link varchar null;

CREATE TABLE IF NOT EXISTS conversations
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    user_one_uuid    uuid         NULL,
    user_two_uuid    uuid         NULL,

    CONSTRAINT pk_conversations PRIMARY KEY (uuid),
    CONSTRAINT fk_conversations_user_one FOREIGN KEY (user_one_uuid) REFERENCES users (uuid),
    CONSTRAINT fk_conversations_user_two FOREIGN KEY (user_two_uuid) REFERENCES users (uuid)

);


CREATE TABLE IF NOT EXISTS messages
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    sender_uuid      uuid         NULL,
    receiver_uuid    uuid         NULL,
    message          varchar      NULL,

    CONSTRAINT pk_messages PRIMARY KEY (uuid),
    CONSTRAINT fk_messages_senders FOREIGN KEY (sender_uuid) REFERENCES users (uuid),
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_uuid) REFERENCES users (uuid)

);

ALTER TABLE messages
    ADD COLUMN conversation_uuid uuid null,
    ADD CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_uuid) REFERENCES conversations (uuid);

ALTER TABLE messages
    ADD COLUMN read boolean null;

ALTER TABLE notifications
    ADD COLUMN post_uuid uuid null;