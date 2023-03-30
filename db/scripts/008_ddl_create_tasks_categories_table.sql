CREATE TABLE if not exists tasks_categories
(
    id          serial PRIMARY KEY,
    task_id     int not null REFERENCES tasks (id),
    category_id int not null REFERENCES categories (id)
);