CREATE SCHEMA IF NOT EXISTS tasklist;

CREATE TABLE IF NOT EXISTS users
(
    id bigserial primary key,
    name varchar(255) not null,
    username varchar(255) not null unique,
    password varchar(255) not null
);

ALTER SEQUENCE users_id_seq RESTART WITH 3;

CREATE TABLE IF NOT EXISTS tasks
(
    id bigserial primary key,
    title varchar(255) not null,
    description text null,
    status varchar(255) not null,
    expiration_date timestamp null
);

ALTER SEQUENCE tasks_id_seq RESTART WITH 5;

CREATE TABLE IF NOT EXISTS users_tasks
(
    user_id bigint,
    task_id bigint,
    primary key(user_id, task_id),
    constraint fk_users_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_users_tasks_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint,
    role varchar(255),
    primary key (user_id, role),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action
)

