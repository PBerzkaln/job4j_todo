package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {
    private static final Logger LOG = LogManager.getLogger(HbnUserRepository.class.getName());
    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> save(Task task) {
        Optional<Task> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(task)));
            rsl = Optional.of(task);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl = false;
        try {
            crudRepository.run("DELETE Task WHERE id = :fId",
                    Map.of("fId", id)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean update(Task task) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.merge(task));
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public boolean setIsDone(int id) {
        boolean rsl = false;
        try {
            crudRepository.run("UPDATE Task SET done = :fdone WHERE id = :fId",
                    Map.of("fId", id, "fdone", Boolean.TRUE)
            );
            rsl = true;
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("FROM Task f JOIN FETCH f.priority WHERE f.id = :fId",
                Task.class, Map.of("fId", id)
        );
    }

    @Override
    public List<Task> findByIsDone(boolean done) {
        return crudRepository.query("FROM Task f JOIN FETCH f.priority WHERE f.done = :fdone",
                Task.class, Map.of("fdone", done)
        );
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query(
                "FROM Task f JOIN FETCH f.priority", Task.class);
    }
}