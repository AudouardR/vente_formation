
CREATE DATABASE vente_formation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vente_formation;

CREATE TABLE USER_(
   username VARCHAR(30) PRIMARY KEY,
   password VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

/*
INSERT INTO USER_(username, password) VALUES 
("AudouardR", "root"),
("BretM", "bret01");
*/

CREATE TABLE COURSE(
   id_course INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(30) NOT NULL,
   description VARCHAR(200),
   days INT,
   is_remote BOOLEAN,
   price DECIMAL(10,2)
) ENGINE=InnoDB;

/*
INSERT INTO COURSE (id_course, name, description, days, is_remote, price) VALUES
(1, "Java", "Java SE 8 : Syntaxe & Poo", 20, 0, 5.99),
(2, "Java avancé", "Spring Core/Mvc/Security", 20, 0, 8.53),
(3, "Spring", "Java SE 8 : Syntaxe & Poo", 20, 1, 6.99),
(4, "Php frameworks", "Symphony", 15, 1, 11.23),
(5, "C#", "DotNet Core", 20, 0, 7.50);
*/

CREATE TABLE CUSTOMER(
   id_customer INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   first_name VARCHAR(30) NOT NULL,
   last_name VARCHAR(30) NOT NULL,
   email VARCHAR(50),
   home_address VARCHAR(100),
   phone_number CHAR(10),
   username VARCHAR(30) NOT NULL,
   FOREIGN KEY(username) REFERENCES USER_(username) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

/*
INSERT INTO CUSTOMER (id_customer, first_name, last_name, email, home_address, phone_number, username) VALUES
(1, "Patrick", "Dumont", "patrick.dupont@gmail.com", "23 Rue des Alaoudes, 40230 Tosse", "0672564322", "AudouardR"),
(2, "Kylian", "Perrin", "kylian.perrin@gmail.com", "231 Av. des Lièvres, 40150 Soorts-Hossegor", "0742786331", "AudouardR"),
(3, "Fabrice", "Lafargue", "fabrice.lafargue@msn.com", "3 Chem. de la Croix de Jubilé, 40140 Soustons", "0752010867", "BretM");
*/

CREATE TABLE CART(
   id_cart INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   is_ordered BOOLEAN NOT NULL,
   id_customer INT UNSIGNED NOT NULL,
   FOREIGN KEY(id_customer) REFERENCES CUSTOMER(id_customer) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ORDER_(
   id_course INT UNSIGNED,
   id_cart INT UNSIGNED,
   PRIMARY KEY(id_course, id_cart),
   FOREIGN KEY(id_course) REFERENCES COURSE(id_course),
   FOREIGN KEY(id_cart) REFERENCES CART(id_cart) ON DELETE CASCADE
) ENGINE=InnoDB;
