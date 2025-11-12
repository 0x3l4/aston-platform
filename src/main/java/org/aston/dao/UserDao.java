package org.aston.dao;

import org.aston.model.User;
import org.aston.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Override
    public Optional<User> get(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<User> userOpt = Optional.ofNullable(session.get(User.class, id));

            if (userOpt.isEmpty())
                logger.info("No user with this id was found: {}", id);
            else
                logger.info("User was found: {}", userOpt.get());

            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null)
                transaction.rollback();

            e.printStackTrace();
        }
    }
}
