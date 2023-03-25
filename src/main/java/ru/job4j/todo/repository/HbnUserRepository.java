package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnUserRepository implements UserRepository {
    private static final Logger LOG = LogManager.getLogger(HbnUserRepository.class.getName());
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> rsl = Optional.empty();
        try {
            crudRepository.run((session -> session.save(user)));
            rsl = Optional.of(user);
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("FROM User WHERE login = :flogin AND password = :fpassword",
                User.class, Map.of("flogin", login, "fpassword", password)
        );
    }
}