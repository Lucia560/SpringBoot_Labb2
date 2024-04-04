ALTER TABLE messages
    ADD status_private BIT(1) NULL;

UPDATE messages
SET status_private = 0
WHERE status_private IS NULL;
ALTER TABLE messages
    MODIFY status_private BIT(1) NOT NULL;