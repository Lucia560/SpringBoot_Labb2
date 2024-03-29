CREATE TABLE messages
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    title   VARCHAR(255)          NULL,
    content VARCHAR(255)          NULL,
    user_id BIGINT                NULL,
    date    date                  NULL,
    userid  BIGINT                NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE user
(
    userid              BIGINT AUTO_INCREMENT NOT NULL,
    username            VARCHAR(255)          NULL,
    `role`              VARCHAR(255)          NULL,
    name_surname        VARCHAR(255)          NULL,
    email               VARCHAR(255)          NULL,
    profile_picture_url VARCHAR(255)          NULL,
    CONSTRAINT pk_user PRIMARY KEY (userid)
);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USER FOREIGN KEY (user_id) REFERENCES user (userid);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USERID FOREIGN KEY (userid) REFERENCES user (userid);