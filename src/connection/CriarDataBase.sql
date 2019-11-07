CREATE DATABASE IF NOT EXISTS sd;

USE sd;

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(255),
  `email` varchar(255),
  `celular` varchar(255)
) ENGINE = InnoDB;