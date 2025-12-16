package org.aston.unit.service;

import org.aston.dao.UserDao;
import org.aston.exception.DaoException;
import org.aston.model.User;
import org.aston.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    private User testUser;

    @BeforeEach
    void setup() throws Exception {
        userService = new UserService();

        Field field = UserService.class.getDeclaredField("userDao");
        field.setAccessible(true);
        field.set(userService, userDao);

        testUser = new User("John", "john@example.com", 30);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testAddUserSuccessful() throws DaoException {
        doNothing().when(userDao).save(any(User.class));

        assertDoesNotThrow(() ->
                userService.addUser("John", "john@example.com", 30)
        );

        verify(userDao, times(1)).save(any(User.class));
    }


    @Test
    void testAddUserDaoException() throws Exception {
        doThrow(new DaoException("DB error", null)).when(userDao).save(any(User.class));

        assertDoesNotThrow(() -> userService.addUser("John", "john@example.com", 30));

        verify(userDao).save(any(User.class));
    }

    @Test
    void testGetAllUsersSuccessful() throws DaoException {
        when(userDao.getAll()).thenReturn(List.of(testUser));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userDao).getAll();
    }

    @Test
    void testGetAllUsersDaoException() throws DaoException {
        when(userDao.getAll()).thenThrow(new DaoException("fail", null));

        List<User> result = userService.getAllUsers();

        assertNull(result);
        verify(userDao).getAll();
    }

    @Test
    void testGetUserByIdFound() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(userDao).get(1L);
    }

    @Test
    void testGetUserByIdNotFound() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);

        assertNull(result);
        verify(userDao).get(1L);
    }

    @Test
    void testGetUserByIdDaoException() throws DaoException {
        when(userDao.get(1L)).thenThrow(new DaoException("error", null));

        User result = userService.getUserById(1L);

        assertNull(result);
        verify(userDao).get(1L);
    }

    @Test
    void testUpdateUserSuccessful() throws Exception {
        when(userDao.get(1L)).thenReturn(Optional.of(testUser));

        boolean updated = userService.updateUser(1L, "NewName", "new@mail.com", 33);

        assertTrue(updated);
        verify(userDao).update(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.empty());

        boolean updated = userService.updateUser(1L, "A", "b", 10);

        assertFalse(updated);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    void testUpdateUserDaoException() throws Exception {
        when(userDao.get(1L)).thenReturn(Optional.of(testUser));
        doThrow(new DaoException("error", null)).when(userDao).update(any(User.class));

        boolean updated = userService.updateUser(1L, "A", "b", 10);

        assertFalse(updated);
        verify(userDao).update(any(User.class));
    }

    @Test
    void testDeleteUserSuccessful() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.of(testUser));

        boolean deleted = userService.deleteUser(1L);

        assertTrue(deleted);
        verify(userDao).delete(testUser);
    }

    @Test
    void testDeleteUserNotFound() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUser(1L);

        assertFalse(deleted);
        verify(userDao, never()).delete(any());
    }

    @Test
    void testDeleteUserDaoException() throws DaoException {
        when(userDao.get(1L)).thenReturn(Optional.of(testUser));
        doThrow(new DaoException("error", null)).when(userDao).delete(testUser);

        boolean deleted = userService.deleteUser(1L);

        assertFalse(deleted);
        verify(userDao).delete(testUser);
    }
}
