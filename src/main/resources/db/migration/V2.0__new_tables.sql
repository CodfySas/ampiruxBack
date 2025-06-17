CREATE TABLE IF NOT EXISTS appointments
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    client_uuid      UUID         NULL,
    barber_uuid      UUID         NULL,
    service_uuid     UUID         NULL,
    scheduled_at     TIMESTAMP    NULL,
    status           VARCHAR(50)  NULL,
    notes            TEXT         NULL,

    CONSTRAINT pk_appointments PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS barbers
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    name             VARCHAR(255)     NULL,
    address          VARCHAR(255)     NULL,
    phone            VARCHAR(100)     NULL,
    email            VARCHAR(255)     NULL,
    dni              VARCHAR(100)     NULL,
    position         VARCHAR(100)     NULL,
    hire_date        DATE             NULL,
    base_salary      NUMERIC(15, 2)   NULL,
    commission_rate  DOUBLE PRECISION NULL,
    status           VARCHAR(100)     NULL,

    CONSTRAINT pk_barbers PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS barbershop
(
    uuid              UUID             NOT NULL,
    code              VARCHAR(255)     NULL,
    version           BIGINT           NULL,
    created_at        TIMESTAMP        NULL,
    last_modified_at  TIMESTAMP        NULL,
    deleted_at        TIMESTAMP        NULL,
    deleted           BOOLEAN DEFAULT FALSE,

    name              VARCHAR(255)     NULL,
    nit               VARCHAR(100)     NULL,
    address           VARCHAR(255)     NULL,
    phone             VARCHAR(100)     NULL,
    email             VARCHAR(255)     NULL,
    logo_url          VARCHAR(500)     NULL,
    default_cash_base NUMERIC(15, 2)   NULL,
    iva_percent       DOUBLE PRECISION NULL,
    apply_iva         BOOLEAN          NULL,
    retention_percent DOUBLE PRECISION NULL,

    CONSTRAINT pk_barbershop PRIMARY KEY (uuid)
);


CREATE TABLE IF NOT EXISTS cash
(
    uuid             UUID           NOT NULL,
    code             VARCHAR(255)   NULL,
    version          BIGINT         NULL,
    created_at       TIMESTAMP      NULL,
    last_modified_at TIMESTAMP      NULL,
    deleted_at       TIMESTAMP      NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    barbershop_uuid  UUID           NULL,
    date             DATE           NULL,
    opened_at        TIMESTAMP      NULL,
    closed_at        TIMESTAMP      NULL,
    base_amount      NUMERIC(15, 2) NULL,
    status           VARCHAR(100)   NULL,
    cash_sales       NUMERIC(15, 2) NULL,
    card_sales       NUMERIC(15, 2) NULL,
    transfer_sales   NUMERIC(15, 2) NULL,

    CONSTRAINT pk_cash PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS clients
(
    uuid                  UUID         NOT NULL,
    code                  VARCHAR(255) NULL,
    version               BIGINT       NULL,
    created_at            TIMESTAMP    NULL,
    last_modified_at      TIMESTAMP    NULL,
    deleted_at            TIMESTAMP    NULL,
    deleted               BOOLEAN DEFAULT FALSE,

    name                  VARCHAR(255) NULL,
    phone                 VARCHAR(100) NULL,
    email                 VARCHAR(255) NULL,
    preferred_barber_uuid UUID         NULL,
    dni                   VARCHAR(100) NULL,
    notes                 TEXT         NULL,

    CONSTRAINT pk_clients PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS commission
(
    uuid              UUID           NOT NULL,
    code              VARCHAR(255)   NULL,
    version           BIGINT         NULL,
    created_at        TIMESTAMP      NULL,
    last_modified_at  TIMESTAMP      NULL,
    deleted_at        TIMESTAMP      NULL,
    deleted           BOOLEAN DEFAULT FALSE,

    barber_uuid       UUID           NULL,
    sale_service_uuid UUID           NULL,
    amount            NUMERIC(15, 2) NULL,
    status            VARCHAR(100)   NULL,
    paid_at           TIMESTAMP      NULL,

    CONSTRAINT pk_commission PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS discount
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    name             VARCHAR(255)     NULL,
    description      TEXT             NULL,
    init_promotion   TIMESTAMP        NULL,
    finish_promotion TIMESTAMP        NULL,
    percentage       DOUBLE PRECISION NULL,
    applies_to       VARCHAR(50)      NULL, -- SERVICES, PRODUCTS, ALL, PERSONALIZED
    cost_assumption  VARCHAR(50)      NULL, -- BUSINESS, BARBER, BOTH

    CONSTRAINT pk_discount PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS discount_product
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    discount_uuid    UUID         NULL,
    product_uuid     UUID         NULL,

    CONSTRAINT pk_discount_product PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS discount_service
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    discount_uuid    UUID         NULL,
    service_uuid     UUID         NULL,

    CONSTRAINT pk_discount_service PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS barber_schedule
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    barber_uuid      UUID         NULL,
    day_of_week      INTEGER      NULL, -- 0 = Sunday, 6 = Saturday
    active           BOOLEAN      NULL,
    start_time       TIME         NULL,
    end_time         TIME         NULL,

    CONSTRAINT pk_barber_schedule PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS expense
(
    uuid             UUID           NOT NULL,
    code             VARCHAR(255)   NULL,
    version          BIGINT         NULL,
    created_at       TIMESTAMP      NULL,
    last_modified_at TIMESTAMP      NULL,
    deleted_at       TIMESTAMP      NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    cash_uuid        UUID           NULL,
    category         VARCHAR(100)   NULL,
    amount           NUMERIC(15, 2) NULL,
    notes            TEXT           NULL,

    CONSTRAINT pk_expense PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS products
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    name             VARCHAR(255)     NULL,
    description      TEXT             NULL,
    price            NUMERIC(15, 2)   NULL,
    stock            DOUBLE PRECISION NULL,
    unit             VARCHAR(50)      NULL,
    category_uuid    UUID             NULL,

    CONSTRAINT pk_products PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS product_category
(
    uuid             UUID         NOT NULL,
    code             VARCHAR(255) NULL,
    version          BIGINT       NULL,
    created_at       TIMESTAMP    NULL,
    last_modified_at TIMESTAMP    NULL,
    deleted_at       TIMESTAMP    NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    name             VARCHAR(255) NULL,

    CONSTRAINT pk_product_category PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS sale
(
    uuid             UUID           NOT NULL,
    code             VARCHAR(255)   NULL,
    version          BIGINT         NULL,
    created_at       TIMESTAMP      NULL,
    last_modified_at TIMESTAMP      NULL,
    deleted_at       TIMESTAMP      NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    client_uuid      UUID           NULL,
    barber_uuid      UUID           NULL,
    date             TIMESTAMP      NULL,
    status           VARCHAR(100)   NULL,
    payment_method   VARCHAR(100)   NULL,
    is_courtesy      BOOLEAN        NULL,
    total            NUMERIC(15, 2) NULL,

    CONSTRAINT pk_sale PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS sale_product
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    sale_uuid        UUID             NULL,
    product_uuid     UUID             NULL,
    quantity         DOUBLE PRECISION NULL,
    price            NUMERIC(15, 2)   NULL,
    total            NUMERIC(15, 2)   NULL,

    CONSTRAINT pk_sale_product PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS sale_service
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    sale_uuid        UUID             NULL,
    service_uuid     UUID             NULL,
    price            NUMERIC(15, 2)   NULL,
    is_courtesy      BOOLEAN          NULL,
    commission_rate  DOUBLE PRECISION NULL,
    barber_uuid      UUID             NULL,

    CONSTRAINT pk_sale_service PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS sale_service_product
(
    uuid              UUID             NOT NULL,
    code              VARCHAR(255)     NULL,
    version           BIGINT           NULL,
    created_at        TIMESTAMP        NULL,
    last_modified_at  TIMESTAMP        NULL,
    deleted_at        TIMESTAMP        NULL,
    deleted           BOOLEAN DEFAULT FALSE,

    sale_service_uuid UUID             NULL,
    product_uuid      UUID             NULL,
    quantity          DOUBLE PRECISION NULL,
    unit              VARCHAR(50)      NULL,
    cost_type         VARCHAR(50)      NULL,

    CONSTRAINT pk_sale_service_product PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS services
(
    uuid             UUID           NOT NULL,
    code             VARCHAR(255)   NULL,
    version          BIGINT         NULL,
    created_at       TIMESTAMP      NULL,
    last_modified_at TIMESTAMP      NULL,
    deleted_at       TIMESTAMP      NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    name             VARCHAR(255)   NULL,
    description      TEXT           NULL,
    price            NUMERIC(15, 2) NULL,
    duration_minutes INTEGER        NULL,
    popularity       INTEGER        NULL,

    CONSTRAINT pk_services PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS service_default_product
(
    uuid             UUID             NOT NULL,
    code             VARCHAR(255)     NULL,
    version          BIGINT           NULL,
    created_at       TIMESTAMP        NULL,
    last_modified_at TIMESTAMP        NULL,
    deleted_at       TIMESTAMP        NULL,
    deleted          BOOLEAN DEFAULT FALSE,

    service_uuid     UUID             NULL,
    product_uuid     UUID             NULL,
    quantity         DOUBLE PRECISION NULL,
    unit             VARCHAR(50)      NULL,
    cost_type        VARCHAR(50)      NULL,

    CONSTRAINT pk_service_default_product PRIMARY KEY (uuid)
);