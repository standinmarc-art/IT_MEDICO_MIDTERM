package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {
    // Try with absolute path if needed: "jdbc:sqlite:./scenario.db"
    private static final String DB_URL = "jdbc:sqlite:scenario.db";

    public static Connection getConnection() {
        try {
            // Explicitly load driver — fixes "No suitable driver"
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(DB_URL);

            // Enable foreign keys
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQLite JDBC Driver not found! Check pom.xml & reload Maven.");
            return null;
        } catch (Exception e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS assets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                device_name TEXT NOT NULL,
                assigned_to TEXT NOT NULL,
                serial_no TEXT NOT NULL UNIQUE,
                condition TEXT NOT NULL CHECK(condition IN ('NEW', 'GOOD', 'REPAIR_NEEDED'))
            );
            """;

        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.err.println("❌ Cannot initialize DB — connection is null");
                return;
            }
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("✅ Database ready");
            }
        } catch (Exception e) {
            System.err.println("❌ DB init error: " + e.getMessage());
        }
    }
}