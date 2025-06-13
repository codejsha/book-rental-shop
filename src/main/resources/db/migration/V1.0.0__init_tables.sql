CREATE TABLE IF NOT EXISTS category
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS book
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    author      VARCHAR(255)                                 NOT NULL,
    title       VARCHAR(255)                                 NOT NULL,
    status      ENUM ('UNKNOWN', 'AVAILABLE', 'CHECKED_OUT') NOT NULL,
    category_id SMALLINT                                     NOT NULL,
    created_at  TIMESTAMP                                    NOT NULL,
    updated_at  TIMESTAMP                                    NOT NULL,
    version     BIGINT DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE RESTRICT
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_role
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT       NOT NULL,
    authority VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS token
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT        NOT NULL,
    access_token  VARCHAR(1000) NOT NULL,
    refresh_token VARCHAR(1000) NOT NULL,
    created_at    TIMESTAMP     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rental
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT    NOT NULL,
    book_id        BIGINT    NOT NULL,
    check_out_date TIMESTAMP NOT NULL,
    return_date    TIMESTAMP,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP NOT NULL,
    version        BIGINT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE
) DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
