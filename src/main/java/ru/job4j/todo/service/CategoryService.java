package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findById(List<Integer> list);

    List<Category> findAll();
}