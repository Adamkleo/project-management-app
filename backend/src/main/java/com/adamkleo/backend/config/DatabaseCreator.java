package com.adamkleo.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseCreator implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        String url = env.getProperty("spring.datasource.url"); // includes db name
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        // Strip database name from URL
        assert url != null;
        String baseUrl = url.replaceAll("/[^/]+(\\?.*)?$", "/");

        try (Connection conn = DriverManager.getConnection(baseUrl, username, password);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'PRACTICA'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE PRACTICA CHARACTER SET latin1");
                System.out.println("Created database PRACTICA");
            } else {
                System.out.println("Database PRACTICA already exists");
            }
        }
    }
}
