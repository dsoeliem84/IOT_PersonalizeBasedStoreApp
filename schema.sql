
-- iot_store.Aisle definition
CREATE TABLE `Aisle` (
  `aisle_id` int NOT NULL,
  `floor` int NOT NULL,
  `store_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `aisle_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`aisle_id`,`floor`),
  KEY `Asile_Store_FK` (`store_uuid`),
  CONSTRAINT `Asile_Store_FK` FOREIGN KEY (`store_uuid`) REFERENCES `Store` (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- iot_store.Product definition
CREATE TABLE `Product` (
  `product_id` int NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `product_description` text,
  `product_image` varchar(255) DEFAULT NULL,
  `aisle_id` int NOT NULL,
  `floor` int NOT NULL,
  `product_location` int DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_products_asile` (`aisle_id`,`floor`),
  CONSTRAINT `fk_products_asile` FOREIGN KEY (`aisle_id`, `floor`) REFERENCES `Aisle` (`aisle_id`, `floor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- iot_store.Promotion definition
CREATE TABLE `Promotion` (
  `promotion_id` int NOT NULL AUTO_INCREMENT,
  `discount` varchar(20) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`promotion_id`),
  KEY `promotions_products_FK` (`product_id`),
  CONSTRAINT `promotions_products_FK` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- iot_store.Store definition
CREATE TABLE `Store` (
  `UUID` varchar(255) NOT NULL,
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;