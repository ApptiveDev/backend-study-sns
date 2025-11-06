# drop index comments_created_index ON COMMENTS;
# drop index posts_created_index ON POSTS;
drop table if exists users;
drop table if exists comments;
drop table if exists posts;

create table if not exists users
(
    id         BIGINT auto_increment primary key,
    nickname   varchar(255) not null unique,
    created_at TIMESTAMP not null,
    version BIGINT default 0
);



create table if not exists posts
(
    id         BIGINT auto_increment
        primary key,
    content    TEXT              not null,
    like_count BIGINT    default 0,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
    user_name CHARACTER VARYING(255)              not null,
    version BIGINT default 0
);

-- create index if not exists posts_created_index ON POSTS(CREATED_AT);
create index posts_created_index on posts (created_at DESC, id DESC);


create table if not exists comments
(
    id       BIGINT auto_increment
        primary key,
    content    TEXT              not null,
    like_count BIGINT    default 0,
    post_id    BIGINT                              not null,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
    user_name  CHARACTER VARYING(255)              not null,
    version BIGINT default 0,
    constraint FK_COMMENTS_TO_POSTS
        foreign key (post_id) references posts (id) on delete cascade
);

create index comments_created_index ON comments(created_at DESC, id DESC);