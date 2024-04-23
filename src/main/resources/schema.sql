-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.18-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


-- Volcando estructura de base de datos para gateways-db
CREATE DATABASE IF NOT EXISTS `gateways-db` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `gateways-db`;

-- Volcando estructura para tabla gateways-db.gateways
CREATE TABLE IF NOT EXISTS `gateways`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `ip4v_address`  varchar(255) DEFAULT NULL,
    `name`          varchar(255) DEFAULT NULL,
    `serial_number` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Volcando datos para la tabla gateways-db.gateways: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `gateways`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `gateways`
    ENABLE KEYS */;

-- Volcando estructura para tabla gateways-db.peripherals
CREATE TABLE IF NOT EXISTS `peripherals`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `created_date` datetime     DEFAULT NULL,
    `status`       varchar(255) DEFAULT NULL,
    `uid`          int(11)      DEFAULT NULL,
    `vendor`       varchar(255) DEFAULT NULL,
    `gateway_id`   bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_cjf8jrf3o89cbconoie7t7ybs` (`uid`),
    KEY `FK9f9qicqlso276wa3icp984wo3` (`gateway_id`),
    CONSTRAINT `FK9f9qicqlso276wa3icp984wo3` FOREIGN KEY (`gateway_id`) REFERENCES `gateways` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Volcando datos para la tabla gateways-db.peripherals: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `peripherals`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `peripherals`
    ENABLE KEYS */;

/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;
