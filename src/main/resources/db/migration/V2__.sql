ALTER TABLE messages
    ADD status_private BIT(1) NULL;

ALTER TABLE messages
    MODIFY status_private BIT(1) NOT NULL;