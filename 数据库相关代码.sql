create database wchat;


CREATE TABLE users (
                       user_id BIGSERIAL PRIMARY KEY,  -- 用户的唯一标识符
                       user_name VARCHAR(100) NOT NULL,  -- 用户的账号名
                       nick_name VARCHAR(100) NOT NULL,  -- 用户昵称
                       phone VARCHAR(20) ,  -- 用户的电话号码，用于登录
                       email varchar(30),  --邮件，用于登录
                       password_hash VARCHAR(64) NOT NULL,  -- 用户密码的SHA256哈希值
                       created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 用户账号创建时间戳
                       updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 用户账号更新时间戳
                       avatar_url VARCHAR(255)
);

CREATE TABLE private_messages (
                                  message_id BIGSERIAL PRIMARY KEY, -- 消息的唯一标识符
                                  sender_id INT, -- 发送消息的用户的唯一标识符
                                  receiver_id INT, -- 接收消息的用户的唯一标识符
                                  message_text VARCHAR(1000), -- 消息的文本内容
                                  message_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 消息的发送时间
                                  message_type varchar(20), --消息类型
                                  message_status varchar(20) --是否已读

);

CREATE TABLE group_messages (
                                message_id BIGSERIAL PRIMARY KEY, -- 消息的唯一标识符
                                sender_id INT, -- 发送消息的用户的唯一标识符
                                group_id INT, -- 消息发送到的群聊的唯一标识符
                                message_text VARCHAR(1000), -- 消息的文本内容
                                message_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 消息的发送时间
                                message_type varchar(20) --消息类型

);

CREATE TABLE friends (
                         friend_id bigserial primary key ,
                         user_id1 INT,  -- 用户的唯一标识符
                         user_id2 INT,  -- 用户的好友的唯一标识符
                         message_num INT,--对应用户未读消息数目，正数表示用户一未读，负数表示用户二
                         status VARCHAR(10)    --是否成功 applying 申请中，success，fail
);

CREATE TABLE groups (
                        group_id serial,  -- 群聊的唯一标识符
                        group_name VARCHAR(100),  -- 群聊的名称
                        group_owner int8
);

CREATE TABLE user_groups (
                             user_groups_id bigserial,
                             user_id INT,  -- 用户的唯一标识符
                             group_id INT,  -- 群聊的唯一标识符
                             message_num INT, --对应用户未读消息数目
                             status VARCHAR(10) ,   --是否成功 applying 申请中，success，fail
                             PRIMARY KEY (user_groups_id)  -- 用户和群聊的关联是唯一的
);

CREATE TABLE leave_message (
                             leave_message_id bigserial,
                             user_id INT,  -- 用户的唯一标识符
                             from_user_id INT,  -- 群聊的唯一标识符
                             message_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, --对应用户未读消息数目
                             title VARCHAR(10) ,
                             content text,
                            --是否成功 applying 申请中，success，fail
                             PRIMARY KEY (leave_message_id)  -- 用户和群聊的关联是唯一的
);

-- 在 users 表上的索引
CREATE UNIQUE INDEX idx_users_user_name ON users (user_name);
CREATE INDEX idx_users_nick_name ON users (nick_name);
CREATE UNIQUE INDEX idx_users_phone ON users (phone);
CREATE INDEX idx_users_created_time ON users (created_time);
CREATE INDEX idx_users_updated_time ON users (updated_time);

-- 在 private_messages 表上的索引
CREATE INDEX idx_private_messages_sender_id ON private_messages (sender_id);
CREATE INDEX idx_private_messages_receiver_id ON private_messages (receiver_id);
CREATE INDEX idx_private_messages_message_time ON private_messages (message_time);

-- 在 group_messages 表上的索引
CREATE INDEX idx_group_messages_sender_id ON group_messages (sender_id);
CREATE INDEX idx_group_messages_group_id ON group_messages (group_id);
CREATE INDEX idx_group_messages_message_time ON group_messages (message_time);

-- 在 groups 表上的索引
CREATE UNIQUE INDEX idx_groups_group_name ON groups (group_name);

-- 在 groups 表上的索引
CREATE INDEX idx_friend ON friends (user_id1);
