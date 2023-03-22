package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnUserRepository implements UserRepository {
    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> rsl = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            rsl = Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> rsl = Optional.empty();
        var session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery("FROM User WHERE login = :flogin AND password = :fpassword", User.class)
                    .setParameter("flogin", login)
                    .setParameter("fpassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return rsl;
    }
}