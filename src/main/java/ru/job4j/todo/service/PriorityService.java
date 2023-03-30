package ru.job4j.todo.service;

import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Optional;

public interface PriorityService {
    Optional<Priority> findById(int id);


    List<Priority> findAll();
}