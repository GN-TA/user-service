CREATE TABLE users (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(40) NOT NULL,
                      password VARCHAR(40) NOT NULL,
                      username VARCHAR(255) NOT NULL,
                      auth VARCHAR(40) NOT NULL,
                      provider VARCHAR(10)
);
