drop table if exists member;

create table member (
    id bigint,
    nickname varchar(255),
    email varchar(255),
    primary key (id)
);