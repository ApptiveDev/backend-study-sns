drop table if exists users;
drop table if exists comments;
drop table if exists posts;
drop table if exists members;
drop table if exists member_follows;
drop table if exists posts_likes;
drop table if exists comments_likes;



create table if not exists members
(
    id         BIGINT auto_increment primary key,
    nickname   varchar(255) not null unique,
    created_at TIMESTAMP not null,
    updated_at TIMESTAMP  not null,
    version BIGINT default 0
);

create table if not exists member_follows
(
    id BIGINT PRIMARY KEY ,
    follower_id BIGINT not null,
    following_id BIGINT not null,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
    foreign key (follower_id) references members (id) on delete cascade,
    foreign key (following_id) references members (id) on delete cascade
);


create table if not exists posts
(
    id         BIGINT auto_increment primary key,
    content    TEXT              not null,
    like_count BIGINT    default 0,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
#     user_name CHARACTER VARYING(255)              not null,
    member_id BIGINT,
    version BIGINT default 0,
    foreign key (member_id) references members(id) on delete cascade
);

-- create index if not exists posts_created_index ON POSTS(CREATED_AT);
create index posts_created_index on posts (created_at DESC, id DESC);



create table if not exists posts_likes (
    id BIGINT PRIMARY KEY,
    post_id BIGINT,
    member_id BIGINT,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
    constraint u_member_post unique (post_id, member_id),
    foreign key (post_id) references posts(id) on delete cascade,
    foreign key (member_id) references members(id) on delete cascade
);


create table if not exists comments
(
    id       BIGINT auto_increment
        primary key,
    content    TEXT              not null,
    like_count BIGINT    default 0,
    post_id    BIGINT                              not null,
    created_at TIMESTAMP  not null,
    updated_at TIMESTAMP  not null ,
    member_id BIGINT,
    version BIGINT default 0,
    constraint FK_COMMENTS_TO_POSTS
        foreign key (post_id) references posts (id) on delete cascade,
    foreign key (member_id) references members(id) on delete cascade
);

create index comments_created_index ON comments(created_at DESC, id DESC);

create table if not exists comments_likes (
      id BIGINT PRIMARY KEY,
       comment_id BIGINT,
       member_id BIGINT,
       created_at TIMESTAMP  not null,
       updated_at TIMESTAMP  not null ,
        constraint u_member_comment unique (comment_id, member_id),
       foreign key (comment_id) references comments(id) on delete cascade,
       foreign key (member_id) references members(id) on delete cascade
);
