DROP TABLE IF EXISTS user_details;
CREATE TABLE user_details (
                                id                    bigserial,
                                first_name            VARCHAR(50) NOT NULL,
                                last_name             VARCHAR(50) NOT NULL,
                                email                 VARCHAR(50) NOT NULL,
                                phone                 VARCHAR(15) NOT NULL,
                                PRIMARY KEY (id)
);


DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id                    bigserial,
                       username              varchar(50) NOT NULL,
                       password              varchar(80) NOT NULL,
                       details_id            bigint,
                       PRIMARY KEY (id),
                       CONSTRAINT users_details_fk FOREIGN KEY (details_id) REFERENCES user_details (id)
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
                       id                    serial,
                       name                  VARCHAR(50) NOT NULL,
                       PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
                             user_id               INT NOT NULL,
                             role_id               INT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id)
                                 REFERENCES users (id),
                             FOREIGN KEY (role_id)
                                 REFERENCES roles (id)
);

INSERT INTO user_details (first_name, last_name, email, phone) VALUES ('Admin','Admin','admin@gmail.com','+79881111111');

INSERT INTO roles (name)
VALUES
    ('ROLE_USER'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username, password, details_id)
VALUES
    ('admin','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i',1);

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3);

