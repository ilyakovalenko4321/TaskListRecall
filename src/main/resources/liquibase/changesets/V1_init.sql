CREATE TABLE IF NOT EXISTS users
(
    id           bigserial primary key,
    name         varchar(255) not null,
    username     varchar(255) not null unique,
    email        varchar(255) not null unique,
    phone_number varchar(255) unique,
    password     varchar(255) not null
);

CREATE TABLE IF NOT EXISTS unconfirmed_users
(

    user_id              bigint       not null,
    expiration_data      timestamp    not null,
    registration_attempt int          not null,
    access_key           varchar(255) not null,
    primary key (user_id),
    constraint fk_unconfirmed_users_users foreign key (user_id) references users (id) on delete cascade on update no action

);

ALTER SEQUENCE users_id_seq RESTART WITH 4;

CREATE TABLE IF NOT EXISTS tasks
(
    id              bigserial primary key,
    title           varchar(255) not null,
    description     text         null,
    status          varchar(255) not null,
    expiration_date timestamp    null
);

ALTER SEQUENCE tasks_id_seq RESTART WITH 5;

CREATE TABLE IF NOT EXISTS users_tasks
(
    user_id bigint not null,
    task_id bigint not null,
    primary key (user_id, task_id),
    constraint fk_users_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_users_tasks_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action
);

CREATE TABLE IF NOT EXISTS tasks_images
(
    task_id bigint       not null,
    image   varchar(255) not null,
    constraint fk_tasks_images_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);
