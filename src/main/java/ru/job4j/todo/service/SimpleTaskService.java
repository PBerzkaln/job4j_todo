package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean setIsDone(int id) {
        return taskRepository.setIsDone(id);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Collection<Task> findByIsDone(boolean done) {
        return taskRepository.findByIsDone(done);
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }
}