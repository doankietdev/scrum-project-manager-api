CREATE TABLE `profiles` (
    `id` CHAR(36) NOT NULL PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `first_name` VARCHAR(20),
    `last_name` VARCHAR(50),
    `username` VARCHAR(50) NOT NULL,
    `display_name` VARCHAR(50),
    `email` VARCHAR(255) NOT NULL,
    `avatar` VARCHAR(2048),
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
ALTER TABLE `profiles`
    ADD UNIQUE KEY `uniq_profile_user_id` (`user_id`),
    ADD UNIQUE KEY `uniq_profile_username` (`username`),
    ADD UNIQUE KEY `uniq_profile_email` (`email`),
    ADD CONSTRAINT `chk_username_length` CHECK (CHAR_LENGTH(`username`) >= 3),
    ADD CONSTRAINT `chk_display_name_length` CHECK (CHAR_LENGTH(`display_name`) >= 3);
