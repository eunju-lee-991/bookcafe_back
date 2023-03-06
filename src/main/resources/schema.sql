drop table if exists member;
drop table if exists review;

create table member (
    id bigint auto_increment,
    nickname varchar(255),
    email varchar(255),
    profileImageUrl varchar(500),
    joindate timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key (id)
);

create table review (
    reviewId bigint auto_increment,
    memberId bigint,
    title varchar(255),
    contents varchar(3000),
    isbn bigint,
    booktitle varchar(255),
    createdDate timestamp DEFAULT CURRENT_TIMESTAMP,
    updatedDate timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key (reviewId)
)