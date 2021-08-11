create table question
(
    id bigint not null primary key,
    answer   text,
    question text,
    name text
);

create sequence question_seq;