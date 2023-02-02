drop table if exists member;

create table member (
    id bigint auto_increment,
    name varchar(255),
    nickname varchar(255),
    email varchar(255),
    joindate timestamp default sysdate,
    primary key (id)
);