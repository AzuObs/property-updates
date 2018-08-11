CREATE DATABASE property_updates_local;

USE property_updates_local;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(32) NOT NULL,
  `lastname` VARCHAR(32) NOT NULL,
  `email` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties` (
  `property_id` INT(11) UNSIGNED NOT NULL,
  `address` VARCHAR(32) NOT NULL,
  `price_euro_pence` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `properties_watchers`;
CREATE TABLE `properties_watchers` (
  `property_id` INT(11) UNSIGNED NOT NULL,
  `user_id` INT(11) UNSIGNED NOT NULL,
  CONSTRAINT `fk_property_id` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES
  (1, 'Louis', 'Armstrong', 'louis.armstrong@gmail.com'),
  (2, 'Frank', 'Sinatra', 'frank.sinatra@gmail.com'),
  (3, 'Edith', 'Piaf', 'edith.piaf@gmail.com');
UNLOCK TABLES;

LOCK TABLES `properties` WRITE;
INSERT INTO `properties` VALUES
  (1, '4 rue Gambetta, 34310, Capestang', 9800000),
  (2, 'Lieu dit la Tamarisiere, 34310, Capestang', 8600000);
UNLOCK TABLES;

LOCK TABLES `properties_watchers` WRITE;
INSERT INTO `properties_watchers` VALUES
  (1, 1),
  (1, 3),
  (2, 2),
  (2, 3);
UNLOCK TABLES;
