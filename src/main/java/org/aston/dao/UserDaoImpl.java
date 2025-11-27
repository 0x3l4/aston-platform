package org.aston.dao;

import org.aston.exception.DaoException;
import org.aston.model.User;
import org.aston.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public Optional<User> get(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<User> userOpt = Optional.ofNullable(session.get(User.class, id));

            logger.debug("fetched user with id {}", id);

            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception ex) {
            logger.error("Error fetching user with id={}", id, ex);
            throw new DaoException("Error fetching user", ex);
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();

            logger.debug("fetched all users ({})", users.size());

            return users;
        } catch (Exception ex) {
            logger.error("Error fetching all users", ex);
            throw new DaoException("Error fetching all users", ex);
        }
    }

    @Override
    public void save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.debug("saved user {}", user);
        }
        catch (Exception ex) {
            if (transaction != null)
                transaction.rollback();

            logger.error("Error saving user {}", user, ex);
            throw new DaoException("Error saving user", ex);
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.debug("updated user {}", user);
        }
        catch (Exception ex) {
            if (transaction != null)
                transaction.rollback();

            logger.error("Error updating user {}", user, ex);
            throw new DaoException("Error updating user", ex);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();

            logger.debug("deleted user {}", user);
        }
        catch (Exception ex) {
            if (transaction != null)
                transaction.rollback();

            logger.error("Error deleting user {}", user, ex);
            throw new DaoException("Error deleting user", ex);
        }
    }
}
