package org.aston.service;

import org.aston.dao.Dao;
import org.aston.dao.UserDao;
import org.aston.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final Dao userDao = new UserDao();

    public void addUser(String name, String email, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.save(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public User getUserById(Long id) {
        Optional<User> userOpt = userDao.get(id);

        if (userOpt.isEmpty())
            return null;

        return userOpt.get();
    }

    public boolean updateUser(Long id, String name, String email, int age) {
        Optional<User> userOpt = userDao.get(id);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();

        if (name != null && !name.isBlank()) user.setName(name);
        if (email != null && !email.isBlank()) user.setEmail(email);
        if (age > 0) user.setAge(age);


        userDao.update(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOpt = userDao.get(id);

        if (userOpt.isEmpty())
            return false;

        userDao.delete(userOpt.get());
        return true;
    }
}