CREATE DATABASE IF NOT EXISTS jasa_desain;
USE jasa_desain;

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('pemesan', 'pembuat') NOT NULL
    );

CREATE TABLE IF NOT EXISTS design_requests (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               user_id INT NOT NULL,
                                               title VARCHAR(100) NOT NULL,
    design_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    color VARCHAR(100),
    size VARCHAR(50),
    reference_link TEXT,
    deadline DATE,
    status VARCHAR(20) DEFAULT 'Menunggu',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

INSERT INTO users (username, password, role) VALUES
                                                 ('pemesan1', '123456', 'pemesan'),
                                                 ('pembuat1', '123456', 'pembuat');