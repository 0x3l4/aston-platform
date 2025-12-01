package org.aston.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtilTest.class);

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            String url = System.getProperty("DB_URL");
            String user = System.getProperty("DB_USER");
            String pass = System.getProperty("DB_PASS");

            Configuration cfg = new Configuration();
            cfg.configure("hibernate.cfg.xml");

            cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            cfg.setProperty("hibernate.connection.url", url);
            cfg.setProperty("hibernate.connection.username", user);
            cfg.setProperty("hibernate.connection.password", pass);

            return cfg.buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException("Hibernate Test initialization error: " + e.getMessage(), e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
