drop table if exists member;

create table member (
    id bigint auto_increment,
    nickname varchar(255),
    email varchar(255),
    joindate timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key (id)
);