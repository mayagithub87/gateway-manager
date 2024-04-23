/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

-- Volcando datos para la tabla gateways-db.gateways: ~5 rows (aproximadamente)
/*!40000 ALTER TABLE `gateways`
    DISABLE KEYS */;
INSERT IGNORE INTO `gateways` (`id`, `ip4v_address`, `name`, `serial_number`)
VALUES (1, '150.50.2.16', 'Gateway 1', 'c22555b3-4559-458b-80ef-8b02f546173e'),
       (2, '10.5.20.216', 'Gateway 2', 'e265b880-dd6e-4f13-9f79-2e0e475c3036'),
       (3, '24.2.53.136', 'Gateway 3', '3255963a-c9c7-4ec5-851d-1fe7b55216df'),
       (4, '192.168.2.45', 'Gateway 4', '9a51a155-0eb2-47a9-9d34-09f30aef933b'),
       (5, '114.32.20.12', 'Gateway 5', 'd7dcb50f-281a-4442-98a2-56f11fdd610d');
/*!40000 ALTER TABLE `gateways`
    ENABLE KEYS */;

-- Volcando datos para la tabla gateways-db.peripherals: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `peripherals`
    DISABLE KEYS */;
INSERT IGNORE INTO `peripherals` (`id`, `created_date`, `status`, `uid`, `vendor`, `gateway_id`)
VALUES (1, '2021-08-04 22:19:19', 'ONLINE', 788991, 'Intel', 1),
       (2, '2021-08-04 22:49:42', 'OFFLINE', 55545, 'IBM', 2),
       (3, '2021-08-14 22:49:42', 'ONLINE', 6468464, 'Apple', 2),
       (4, '2020-08-04 22:49:42', 'ONLINE', 13786, 'Samsung', 2);
/*!40000 ALTER TABLE `peripherals`
    ENABLE KEYS */;

/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;
