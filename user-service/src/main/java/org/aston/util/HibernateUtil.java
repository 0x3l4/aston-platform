package org.aston.util;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Dotenv dotenv = Dotenv.load();

            String dbUrl = dotenv.get("DB_URL");
            String dbUser = dotenv.get("DB_USER");
            String dbPass = dotenv.get("DB_PASS");

            createDatabaseIfNotExists(dbUrl, dbUser, dbPass);

            Configuration cfg = new Configuration();
            cfg.configure("hibernate.cfg.xml");

            cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            cfg.setProperty("hibernate.connection.url", dbUrl);
            cfg.setProperty("hibernate.connection.username", dbUser);
            cfg.setProperty("hibernate.connection.password", dbPass);

            return cfg.buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException("Hibernate initialization error: " + e.getMessage(), e);
        }
    }

    private static void createDatabaseIfNotExists(String dbUrl, String user, String pass) {
        try {
            String[] parts = dbUrl.split("/");
            String dbName = parts[parts.length - 1];

            String adminUrl = dbUrl.replace(dbName, "postgres");

            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(adminUrl, user, pass);
                 Statement stmt = conn.createStatement()) {

                ResultSet rs = stmt.executeQuery(
                        "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");

                if (!rs.next()) {
                    stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Database check/create error: " + e.getMessage(), e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

