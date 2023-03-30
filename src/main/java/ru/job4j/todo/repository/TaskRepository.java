package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<Task> save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean setIsDone(int id);

    Optional<Task> findById(int id);

    List<Task> findByIsDone(boolean done);

    List<Task> findAll();
}