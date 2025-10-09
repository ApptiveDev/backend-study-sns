CREATE TABLE IF NOT EXISTS post (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    content VARCHAR(255),
    like_count BIGINT,
    username VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    );
