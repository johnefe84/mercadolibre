DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS satellite;

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