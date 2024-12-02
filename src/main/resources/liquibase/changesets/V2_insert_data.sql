insert into users (id, name, username, email, password)
values (1,'testUser1', 'testUser1', 'testUser1@gmail.com', '$2a$12$BuDvjtTcrO6ED.UTkaHCU.ESEPBHICAl/nUg3m8EM6udPZref.rae'),
       (2,'testUser2', 'testUser2', 'testUser2@gmail.com', '$2a$12$4CgcsI/hWzI37YQe0IBtTu8uPHHN/aBipT8jrbZBvdY2FmogIl4.e'),
       (3,'testUser3', 'testUser3', 'testUser3@gmail.com', '$2a$12$Wr2k254mf19wdzUbapuPyOs1rI.xP/D1dY7e5r.z6wX9wvlKTHg8C');

insert into tasks(id, title, description, status, expiration_date)
values (1,'Buy cheese', null, 'TODO', '2025-01-24 12:00:00'),
       (2,'Do homework', 'Math, Phisics, Literature', 'IN_PROGRESS', '20205-01-31 00:00:00'),
       (3,'Clean rooms', null, 'DONE', null),
       (4, 'Call Mike', 'Ask about meeting', 'TODO', '2025-02-01 00:00:00');

insert into users_tasks(user_id, task_id)
values (2, 1),
       (2, 2),
       (2, 3),
       (1, 4);

insert into users_roles(user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_BLOCKED')