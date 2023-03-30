package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.List;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findById(List<Integer> list) {
        return categoryRepository.findById(list);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}