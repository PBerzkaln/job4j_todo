package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> save(Task task) {
        try {
            crudRepository.run(session -> session.save(task));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(task);
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crudRepository.run("DELETE Task WHERE id = :fId",
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Task task) {
        try {
            crudRepository.run(session -> session.merge(task));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean setIsDone(int id) {
        try {
            crudRepository.run("UPDATE Task SET done = :fdone WHERE id = :fId",
                    Map.of("fId", id, "fdone", Boolean.TRUE)
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("FROM Task WHERE id = :fId",
                Task.class, Map.of("fId", id)
        );
    }

    @Override
    public List<Task> findByIsDone(boolean done) {
        return crudRepository.query("FROM Task WHERE done = :fdone",
                Task.class, Map.of("fdone", done)
        );
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("FROM Task", Task.class);
    }
}