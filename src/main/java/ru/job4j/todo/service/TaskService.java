package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Optional<Task> save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean setIsDone(int id);

    Optional<Task> findById(int id);

    Collection<Task> findByIsDone(boolean done);

    Collection<Task> findAll();
}