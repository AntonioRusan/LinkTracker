--liquibase formatted sql
--changeset antoniorusan:1
create table link
(
    id              bigint generated always as identity,
    url             text not null,

    last_check_time timestamp with time zone,
    updated_at      timestamp with time zone,

    primary key (id),
    unique (url)
);

create table chat
(
    id bigint primary key
);

create table chat_link
(
    chat_id bigint not null references chat (id),
    link_id bigint not null references link (id),
    primary key (chat_id, link_id)
);
