CREATE TABLE member_tbl (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255),
    user_pwd VARCHAR(255),
    user_name VARCHAR(255),
    p_number VARCHAR(255),
    nickName VARCHAR(255),
    email VARCHAR(255),
    create_at DATETIME,
    modified_at DATETIME,
    CONSTRAINT nick_unique_fk UNIQUE (nickName),
    CONSTRAINT email_unique_fk UNIQUE (email)

);