
create table if not exists USERS
(
    ID         BIGINT auto_increment
        primary key,
    NICKNAME   CHARACTER VARYING(255)              not null
        unique,
    CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null
);



create table if not exists POSTS
(
    ID         BIGINT auto_increment
        primary key,
    CONTENT    CHARACTER LARGE OBJECT              not null,
    LIKE_COUNT BIGINT    default 0,
    CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
    UPDATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    USER_NAME  CHARACTER VARYING(255)              not null
);

-- create index if not exists posts_created_index ON POSTS(CREATED_AT);
create index if not exists posts_created_index on posts (created_at DESC, id DESC);


    create table if not exists COMMENTS
(
    ID         BIGINT auto_increment
        primary key,
    CONTENT    CHARACTER LARGE OBJECT              not null,
    LIKE_COUNT BIGINT    default 0,
    POST_ID    BIGINT                              not null,
    CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
    UPDATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    USER_NAME  CHARACTER VARYING(255)              not null,
    constraint FK_COMMENTS_TO_POSTS
        foreign key (POST_ID) references POSTS
            on delete cascade
);

create index if not exists comments_created_index ON COMMENTS(created_at DESC, id DESC);