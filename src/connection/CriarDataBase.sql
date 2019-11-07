CREATE DATABASE IF NOT EXISTS sd;

USE sd;

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL,
  `nome` varchar(255),
  `email` varchar(255),
  `celular` varchar(255)
) ENGINE = InnoDB;

ALTER TABLE `cliente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;