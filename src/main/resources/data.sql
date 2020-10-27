DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS satellite;
DELETE FROM user;

CREATE TABLE satellite
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(250) NOT NULL,
    distancia VARCHAR(250) NOT NULL
);

CREATE TABLE messages
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    satellite_id INT NOT NULL,
    message      VARCHAR(50),
    foreign key (satellite_id) references satellite(id)
);

CREATE TABLE IF NOT EXISTS user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50),
    email      VARCHAR(50),
    password   VARCHAR(50)
);

--INSERT INTO user(username,password,email) VALUES('meli','$2a$12$lgHEEh7pdOZ.L2TE1dm2de1Pn44uGHqGnWKNOq.I2NaVQQUz9xTEW','user@gmail.com');