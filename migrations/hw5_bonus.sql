--liquibase formatted sql
--changeset antoniorusan:2
create table github_link
(
    link_id                bigint not null references link (id),

    last_pull_request_date timestamp with time zone,

    primary key (link_id)
);

create table stackoverflow_link
(
    link_id          bigint not null references link (id),

    last_answer_date timestamp with time zone,

    primary key (link_id)
);
