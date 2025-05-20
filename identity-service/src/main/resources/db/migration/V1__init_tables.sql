CREATE TABLE `users` (
    `id` CHAR(36) NOT NULL PRIMARY KEY,
    `identifier` VARCHAR(150) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `identity_type` ENUM('EMAIL') NOT NULL,
    `status` ENUM('PENDING', 'ACTIVE', 'DISABLED', 'BANNED') DEFAULT 'PENDING',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
ALTER TABLE `users` 
    ADD UNIQUE `unique_user_identifier`(`identifier`);

CREATE TABLE `otps` (
    `id` CHAR(36) NOT NULL PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `code` CHAR(6) NOT NULL,
    `type` ENUM('EMAIL_VERIFICATION', 'PASSWORD_RESET') NOT NULL,
    `status` ENUM('ACTIVE', 'DISABLE', 'BANNED') DEFAULT 'ACTIVE',
    `expires_at` TIMESTAMP NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
ALTER TABLE `otps` 
    ADD UNIQUE `unique_otp_user_id_code_type`(`user_id`, `code`, `type`);

CREATE TABLE `login_sessions` (
    `id` VARCHAR(36) NOT NULL  PRIMARY KEY COMMENT 'Primary Key (as per original image)',
    `user_id` CHAR(36) NOT NULL COMMENT 'User ID (if determined, e.g., after successful login or for failed attempts on known users)',
    `public_key` BLOB NOT NULL COMMENT 'Public key generated (if successfully)',
    `jti` CHAR(36) NOT NULL COMMENT 'Login token generated (if successful)',
    `ip_address` VARCHAR(45) NOT NULL COMMENT 'IP address used for the action',
    `user_agent` VARCHAR(255) NOT NULL COMMENT 'User agent string (browser/device information)',
    `expires_at` TIMESTAMP NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL,
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores the history of login/logout events';
ALTER TABLE `login_sessions` 
    ADD UNIQUE `unique_login_session_user_id_jti`(`user_id`, `jti`);

CREATE TABLE `roles` (
    `id` VARCHAR(36) NOT NULL  PRIMARY KEY,
    `name` VARCHAR(30) NOT NULL,
    `description` VARCHAR(255) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
);
ALTER TABLE `roles` 
    ADD UNIQUE `unique_role_name`(`name`);

CREATE TABLE `permissions` (
    `id` VARCHAR(36) NOT NULL  PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(255) NULL,
    `type` ENUM('API', 'PAGE', 'MENU', 'BUTTON') NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
);
ALTER TABLE `permissions` 
    ADD UNIQUE `unique_permission_name`(`name`, `type`);

CREATE TABLE `user_roles` (
    `role_id` VARCHAR(36) NOT NULL,
    `user_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`user_id`, `role_id`)
);

CREATE TABLE `role_permissions` (
    `role_id` VARCHAR(36) NOT NULL,
    `permission_id` VARCHAR(36) NOT NULL,

    PRIMARY KEY (`role_id`, `permission_id`)
);

ALTER TABLE `otps`
    ADD CONSTRAINT `fk_otp_user_id` FOREIGN KEY(`user_id`) REFERENCES `users`(`id`) ON UPDATE CASCADE;

ALTER TABLE `login_sessions`
    ADD CONSTRAINT `fk_login_session_user_id`FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

ALTER TABLE `user_roles`
    ADD CONSTRAINT `fk_user_role_user_id`FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

ALTER TABLE `user_roles`
    ADD CONSTRAINT `fk_user_role_role_id`FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON UPDATE CASCADE;

ALTER TABLE `role_permissions`
    ADD CONSTRAINT `fk_role_permission_role_id`FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON UPDATE CASCADE;

ALTER TABLE `role_permissions`
    ADD CONSTRAINT `fk_role_permission_permission_id`FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON UPDATE CASCADE;
