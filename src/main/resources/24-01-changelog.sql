-- liquibase formatted sql

-- changeset HP:1692911842410-1
CREATE SEQUENCE IF NOT EXISTS sequence_generator START WITH 1 INCREMENT BY 50;

-- changeset HP:1692911842410-2
CREATE TABLE annual_declaration
(
    id              BIGINT       NOT NULL,
    submission_date date         NOT NULL,
    status          VARCHAR(255) NOT NULL,
    pensioner_id    BIGINT,
    CONSTRAINT pk_annual_declaration PRIMARY KEY (id)
);

-- changeset HP:1692911842410-3
CREATE TABLE beneficiary
(
    entitlement_type      VARCHAR(255) NOT NULL,
    entitlement_details   VARCHAR(255),
    family_members_id     BIGINT       NOT NULL,
    annual_declaration_id BIGINT       NOT NULL,
    CONSTRAINT pk_beneficiary PRIMARY KEY (family_members_id, annual_declaration_id)
);

-- changeset HP:1692911842410-4
CREATE TABLE document
(
    id                    BIGINT                      NOT NULL,
    file_name             VARCHAR(255)                NOT NULL,
    file_path             VARCHAR(255)                NOT NULL,
    submission_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    documentd_type        VARCHAR(255),
    annual_declaration_id BIGINT,
    CONSTRAINT pk_document PRIMARY KEY (id)
);

-- changeset HP:1692911842410-5
CREATE TABLE family_member
(
    id              BIGINT       NOT NULL,
    national_number VARCHAR(12)  NOT NULL,
    name            VARCHAR(255) NOT NULL,
    date_of_birth   date         NOT NULL,
    gender          VARCHAR(255) NOT NULL,
    pensioner_id    BIGINT,
    CONSTRAINT pk_family_member PRIMARY KEY (id)
);

-- changeset HP:1692911842410-6
CREATE TABLE jhi_authority
(
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_jhi_authority PRIMARY KEY (name)
);

-- changeset HP:1692911842410-7
CREATE TABLE jhi_date_time_wrapper
(
    id               BIGINT NOT NULL,
    instant          TIMESTAMP WITHOUT TIME ZONE,
    local_date_time  TIMESTAMP WITHOUT TIME ZONE,
    offset_date_time TIMESTAMP with time zone,
    zoned_date_time  TIMESTAMP with time zone,
    local_time       time WITHOUT TIME ZONE,
    offset_time      time WITH TIME ZONE,
    local_date       date,
    CONSTRAINT pk_jhi_date_time_wrapper PRIMARY KEY (id)
);

-- changeset HP:1692911842410-8
CREATE TABLE jhi_user
(
    id                 BIGINT      NOT NULL,
    created_by         VARCHAR(50) NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    login              VARCHAR(50) NOT NULL,
    password_hash      VARCHAR(60) NOT NULL,
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    email              VARCHAR(254),
    activated          BOOLEAN     NOT NULL,
    lang_key           VARCHAR(10),
    image_url          VARCHAR(256),
    activation_key     VARCHAR(20),
    reset_key          VARCHAR(20),
    reset_date         TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_jhi_user PRIMARY KEY (id)
);

-- changeset HP:1692911842410-9
CREATE TABLE jhi_user_authority
(
    authority_name VARCHAR(50) NOT NULL,
    user_id        BIGINT      NOT NULL,
    CONSTRAINT pk_jhi_user_authority PRIMARY KEY (authority_name, user_id)
);

-- changeset HP:1692911842410-10
CREATE TABLE rel_annual_declaration__family_members
(
    annual_declaration_id BIGINT NOT NULL,
    family_members_id     BIGINT NOT NULL,
    CONSTRAINT pk_rel_annual_declaration__family_members PRIMARY KEY (annual_declaration_id, family_members_id)
);

-- changeset HP:1692911842410-11
CREATE TABLE social_security_pensioner
(
    id              BIGINT       NOT NULL,
    national_number VARCHAR(12)  NOT NULL,
    pension_number  VARCHAR(255) NOT NULL,
    date_of_birth   date         NOT NULL,
    address         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_social_security_pensioner PRIMARY KEY (id)
);

-- changeset HP:1692911842410-12
ALTER TABLE family_member
    ADD CONSTRAINT uc_family_member_national_number UNIQUE (national_number);

-- changeset HP:1692911842410-13
ALTER TABLE jhi_user
    ADD CONSTRAINT uc_jhi_user_email UNIQUE (email);

-- changeset HP:1692911842410-14
ALTER TABLE jhi_user
    ADD CONSTRAINT uc_jhi_user_login UNIQUE (login);

-- changeset HP:1692911842410-15
ALTER TABLE social_security_pensioner
    ADD CONSTRAINT uc_social_security_pensioner_national_number UNIQUE (national_number);

-- changeset HP:1692911842410-16
ALTER TABLE annual_declaration
    ADD CONSTRAINT FK_ANNUAL_DECLARATION_ON_PENSIONER FOREIGN KEY (pensioner_id) REFERENCES social_security_pensioner (id);

-- changeset HP:1692911842410-17
ALTER TABLE beneficiary
    ADD CONSTRAINT FK_BENEFICIARY_ON_ANNUALDECLARATION FOREIGN KEY (annual_declaration_id) REFERENCES annual_declaration (id);

-- changeset HP:1692911842410-18
ALTER TABLE beneficiary
    ADD CONSTRAINT FK_BENEFICIARY_ON_FAMILYMEMBERS FOREIGN KEY (family_members_id) REFERENCES family_member (id);

-- changeset HP:1692911842410-19
ALTER TABLE document
    ADD CONSTRAINT FK_DOCUMENT_ON_ANNUALDECLARATION FOREIGN KEY (annual_declaration_id) REFERENCES annual_declaration (id);

-- changeset HP:1692911842410-20
ALTER TABLE family_member
    ADD CONSTRAINT FK_FAMILY_MEMBER_ON_PENSIONER FOREIGN KEY (pensioner_id) REFERENCES social_security_pensioner (id);

-- changeset HP:1692911842410-21
ALTER TABLE jhi_user_authority
    ADD CONSTRAINT fk_jhiuseaut_on_authority FOREIGN KEY (authority_name) REFERENCES jhi_authority (name);

-- changeset HP:1692911842410-22
ALTER TABLE jhi_user_authority
    ADD CONSTRAINT fk_jhiuseaut_on_user FOREIGN KEY (user_id) REFERENCES jhi_user (id);

-- changeset HP:1692911842410-23
ALTER TABLE rel_annual_declaration__family_members
    ADD CONSTRAINT fk_relanndecfammem_on_annual_declaration FOREIGN KEY (annual_declaration_id) REFERENCES annual_declaration (id);

-- changeset HP:1692911842410-24
ALTER TABLE rel_annual_declaration__family_members
    ADD CONSTRAINT fk_relanndecfammem_on_family_member FOREIGN KEY (family_members_id) REFERENCES family_member (id);

