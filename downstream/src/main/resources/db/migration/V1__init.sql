create table if not exists downstream_entity
(
    id   serial primary key,
    message text not null
);