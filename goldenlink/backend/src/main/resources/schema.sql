DROP TABLE IF EXISTS bookmarks;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS entityuser;

CREATE TABLE entityuser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid VARCHAR(50) NOT NULL UNIQUE,
    userpw VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    category VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME(6),
    FOREIGN KEY (userid) REFERENCES entityuser(id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    userid BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME(6),
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    FOREIGN KEY (userid) REFERENCES entityuser(id) ON DELETE CASCADE
);

CREATE TABLE bookmarks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL,
    hid VARCHAR(255) NOT NULL,
    hname VARCHAR(255) NOT NULL,
    haddress VARCHAR(500),
    htel VARCHAR(50),
    hlat DOUBLE,
    hlon DOUBLE,
    distance DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES entityuser(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_hospital (userid, hid),
    INDEX idx_user_id (userid),
    INDEX idx_created_at (created_at)
);