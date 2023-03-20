package ru.job4j.todo.repository;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HbnTaskRepositoryTest {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder()
            .configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY)
            .buildMetadata().buildSessionFactory();
    private static final TaskRepository TASK_REPOSITORY = new HbnTaskRepository(SF);

    @AfterEach
    public void clearRegister() {
        var session = SF.openSession();
        session.beginTransaction();
        session.createQuery("DELETE Task")
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenSaveAndGetSame() {
        var task = new Task(0, true, "Некое описание", LocalDateTime.now());
        TASK_REPOSITORY.save(task);
        var session = SF.openSession();
        session.beginTransaction();
        var taskFromBD = session.createQuery("FROM Task", Task.class)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        assertThat(taskFromBD).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenDeleteByIDAndGetEmpty() {
        var task = new Task(0, true, "Некое описание", LocalDateTime.now());
        TASK_REPOSITORY.save(task);
        boolean rsl = TASK_REPOSITORY.deleteById(task.getId());
        var session = SF.openSession();
        session.beginTransaction();
        Optional<Task> taskFromBD = session.createQuery("FROM Task", Task.class)
                .uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        assertThat(taskFromBD).usingRecursiveComparison().isEqualTo(empty());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenSaveAndGetUpdate() {
        var task = new Task(0, true, "Некое описание", LocalDateTime.now());
        TASK_REPOSITORY.save(task);
        task.setDescription("Новое описание");
        TASK_REPOSITORY.update(task);
        var session = SF.openSession();
        session.beginTransaction();
        Optional<Task> taskFromBD = session.createQuery("FROM Task", Task.class)
                .uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        assertThat(taskFromBD.get().getDescription()).isEqualTo("Новое описание");
    }

    @Test
    public void whenSaveAndGetById() {
        var task = new Task(0, true, "Некое описание", LocalDateTime.now());
        TASK_REPOSITORY.save(task);
        var rsl = TASK_REPOSITORY.findById(task.getId()).get();
        assertThat(rsl).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenSaveAndGetByIsDone() {
        var task1 = new Task(0, true, "Некое описание1", LocalDateTime.now());
        var task2 = new Task(1, false, "Некое описание2", LocalDateTime.now());
        var task3 = new Task(2, true, "Некое описание3", LocalDateTime.now());
        TASK_REPOSITORY.save(task1);
        TASK_REPOSITORY.save(task2);
        TASK_REPOSITORY.save(task3);
        var rsl = TASK_REPOSITORY.findByIsDone(true);
        assertThat(rsl).usingRecursiveComparison().isEqualTo(List.of(task1, task3));
    }

    @Test
    public void whenSaveSomeAndFindAll() {
        var task1 = new Task(0, true, "Некое описание1", LocalDateTime.now());
        var task2 = new Task(1, false, "Некое описание2", LocalDateTime.now());
        var task3 = new Task(2, true, "Некое описание3", LocalDateTime.now());
        TASK_REPOSITORY.save(task1);
        TASK_REPOSITORY.save(task2);
        TASK_REPOSITORY.save(task3);
        var rsl = TASK_REPOSITORY.findAll();
        assertThat(rsl).usingRecursiveComparison().isEqualTo(List.of(task1, task2, task3));
    }
}