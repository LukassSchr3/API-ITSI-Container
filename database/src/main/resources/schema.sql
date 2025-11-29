-- H2 Database Schema

CREATE TABLE IF NOT EXISTS users (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS images ( -- Represents the Exercise and will sended by the frontend.
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    image_ref VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS instances ( -- Finish Container Instances Table which can be detected over a user and the Image
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    container_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE,
    image_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    status VARCHAR(50) DEFAULT 'created',
    FOREIGN KEY(image_id) REFERENCES images(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

