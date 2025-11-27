package org.aston.service;

import org.aston.dao.UserDao;
import org.aston.dao.UserDaoImpl;
import org.aston.exception.DaoException;
import org.aston.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao = new UserDaoImpl();

    public void addUser(String name, String email, int age) {
        User user = new User(name, email, age);

        try {
            userDao.save(user);
            logger.info("Business: saved user with id {}", user.getId());

        } catch (DaoException ex) {
            logger.error("Business: failed to add user with id {}", user.getId(), ex);
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userDao.getAll();

            if (users.size() > 0)
                logger.info("Business: fetched all users ({})", users.size());
            else
                logger.warn("Business: no user fetched ");

            return users;
        } catch (DaoException ex) {
            logger.error("Business: failed to fetch all users", ex);
            return null;
        }
    }

    public User getUserById(Long id) {
        try {
            Optional<User> userOpt = userDao.get(id);

            if (userOpt.isEmpty()) {
                logger.warn("Business: no user fetched with id {}", id);
                return null;
            }

            User user = userOpt.get();
            logger.info("Business: fetched user with id {}", id);

            return user;
        } catch (DaoException ex) {
            logger.error("Business: failed to fetch user with id {}", id, ex);
            return null;
        }
    }

    public boolean updateUser(Long id, String name, String email, int age) {
        try {
            Optional<User> userOpt = userDao.get(id);
            if (userOpt.isEmpty()) {
                logger.warn("Business: no user to update with id {}", id);
                return false;
            }

            User user = userOpt.get();

            if (name != null && !name.isBlank()) user.setName(name);
            if (email != null && !email.isBlank()) user.setEmail(email);
            if (age > 0) user.setAge(age);

            userDao.update(user);
            logger.info("Business: updated user with id {}", user.getId());

            return true;
        } catch (DaoException ex) {
            logger.error("Business: failed to update user with id {}", id, ex);
            return false;
        }
    }

    public boolean deleteUser(Long id) {
        try {
            Optional<User> userOpt = userDao.get(id);

            if (userOpt.isEmpty()) {
                logger.warn("Business: no user to delete with id {}", id);
                return false;
            }

            userDao.delete(userOpt.get());
            logger.info("Business: deleted user with id {}", id);

            return true;
        } catch (DaoException ex) {
            logger.error("Business: failed to delete user with id {}", id, ex);
            return false;
        }
    }
}