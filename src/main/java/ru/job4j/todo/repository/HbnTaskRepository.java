package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        var session = sf.openSession();
        int rsl = 0;
        try {
            session.beginTransaction();
            rsl = session.createQuery("DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl > 0;
    }

    @Override
    public boolean update(Task task) {
        var session = sf.openSession();
        boolean rsl = false;
        try {
            session.beginTransaction();
            session.merge(task);
            session.getTransaction().commit();
            rsl = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> rsl = Optional.empty();
        var session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery("FROM Task WHERE id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Collection<Task> findByIsDone(boolean done) {
        List<Task> rsl = new ArrayList<>();
        var session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery("FROM Task WHERE done = :fdone", Task.class)
                    .setParameter("fdone", done)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Collection<Task> findAll() {
        List<Task> rsl = new ArrayList<>();
        var session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery("FROM Task", Task.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }
}