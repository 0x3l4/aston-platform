package org.aston.integration.dao;

import org.aston.dao.UserDaoImpl;
import org.aston.model.User;
import org.aston.util.HibernateUtilTest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoIT {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test");

    private static UserDaoImpl userDao;

    @BeforeAll
    static void setup() throws Exception {
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USER", postgres.getUsername());
        System.setProperty("DB_PASS", postgres.getPassword());

        SessionFactory factory = HibernateUtilTest.getSessionFactory();

        userDao = new UserDaoImpl(factory);
    }


    @Test
    @Order(1)
    void testSaveUser() {
        User user = new User("John", "john@example.com", 25);

        userDao.save(user);

        assertNotNull(user.getId(), "ID должен быть проставлен Hibernate");
    }

    @Test
    @Order(2)
    void testGetUser() {
        User user = new User("Anna", "anna@test.com", 30);
        userDao.save(user);

        Optional<User> result = userDao.get(user.getId());

        assertTrue(result.isPresent());
        assertEquals("Anna", result.get().getName());
    }

    @Test
    @Order(3)
    void testGetAllUsers() {
        List<User> users = userDao.getAll();

        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        User user = new User("Old", "old@a.com", 20);
        userDao.save(user);

        user.setName("New Name");
        userDao.update(user);

        Optional<User> updated = userDao.get(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("New Name", updated.get().getName());
    }

    @Test
    @Order(5)
    void testDeleteUser() {
        User user = new User("ToDelete", "del@test.com", 50);
        userDao.save(user);

        userDao.delete(user);

        Optional<User> deleted = userDao.get(user.getId());
        assertTrue(deleted.isEmpty());
    }
}

