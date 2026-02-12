DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS boards;
DROP TABLE IF EXISTS EntityUser;

CREATE TABLE EntityUser (
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
                        createdAt DATETIME(6),
                        FOREIGN KEY (userid) REFERENCES EntityUser(id)
);
CREATE TABLE comments (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        content TEXT NOT NULL,
                        userid BIGINT NOT NULL,
                        boardid BIGINT NOT NULL,
                        createdAt DATETIME(6) NOT NULL,
                        CONSTRAINT fk_comments_board FOREIGN KEY (boardid) REFERENCES boards(id),
                        CONSTRAINT fk_comments_user FOREIGN KEY (userid) REFERENCES EntityUser(id)
);
