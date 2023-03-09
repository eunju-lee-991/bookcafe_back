--drop table if exists member;
--drop table if exists review;
--drop table if exists like_review;

create table member (
    id bigint auto_increment,
    nickname varchar(50),
    email varchar(200),
    profileImageUrl varchar(500),
    joindate timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key (id)
);

create table review (
    reviewId bigint auto_increment,
     memberId bigint,
    title varchar(200),
    contents varchar(3000),
    isbn bigint,
    booktitle varchar(200),
    createdDate timestamp DEFAULT CURRENT_TIMESTAMP,
    updatedDate timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key (reviewId)
);

CREATE TABLE like_review (
  likeid bigint auto_increment,
  reviewid bigint NOT NULL,
  memberid bigint NOT NULL,
  clickDate timestamp DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(REVIEWID, MEMBERID)
);