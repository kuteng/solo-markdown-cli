-- DROP TABLE IF EXISTS user;
-- create schema sa;

CREATE TABLE myuser (
    -- id INT AUTO_INCREMENT PRIMARY KEY,
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1),
    account_id VARCHAR(250) NOT NULL,
    name VARCHAR(250) NOT NULL,
    token VARCHAR(250) DEFAULT NULL
);