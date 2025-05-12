CREATE TABLE `login_session` (
    `id` VARCHAR(36) NOT NULL COMMENT 'Primary Key (as per original image)',
    `user_id` CHAR(36) NOT NULL COMMENT 'User ID (if determined, e.g., after successful login or for failed attempts on known users)',
    `public_key` BLOB NOT NULL COMMENT 'Public key generated (if successfully)',
    `jti` CHAR(36) NOT NULL COMMENT 'Login token generated (if successful)',
    `ip_address` VARCHAR(45) NOT NULL COMMENT 'IP address used for the action',
    `user_agent` VARCHAR(255) NOT NULL COMMENT 'User agent string (browser/device information)',
    `expires_at` TIMESTAMP NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_login_session_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON UPDATE CASCADE,

    KEY `idx_login_user_id` (`user_id`), -- Index for faster lookups by user_id
    KEY `idx_login_user_id_jti` (`user_id`, `jti`), -- Index for faster lookups by user_id and jti
    KEY `idx_login_created_at` (`created_at`) -- Index for faster lookups/sorting by creation time
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores the history of login/logout events';