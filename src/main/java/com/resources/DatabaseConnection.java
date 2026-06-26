package com.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    private static String url;
    private static String user;
    private static String password;
    
    // =============================================================
    // STATIC BLOCK — DITO ANG BAGUHIN PARA SA RAILWAY
    // =============================================================
    static {
        // ===== SUBUKIN MUNA ANG RAILWAY ENVIRONMENT VARIABLES =====
        String railwayUrl = System.getenv("JDBC_DATABASE_URL");
        String railwayUser = System.getenv("JDBC_DATABASE_USERNAME");
        String railwayPassword = System.getenv("JDBC_DATABASE_PASSWORD");
        
        if (railwayUrl != null && !railwayUrl.isEmpty()) {
            // ✅ RAILWAY MODE — gamitin ang environment variables
            url = railwayUrl;
            user = railwayUser;
            password = railwayPassword;
            System.out.println("🚀 Running on Railway");
        } else {
            // ✅ LOCAL MODE — gamitin ang local database
            url = "jdbc:mysql://localhost:3306/laundry_db";
            user = "root";
            password = "";  // Ilagay ang password mo kung meron
            System.out.println("💻 Running locally");
        }
        
        // ===== PRINT CONNECTION DETAILS =====
        System.out.println("=========================================");
        System.out.println("📌 Database Connection Details:");
        System.out.println("   URL: " + url);
        System.out.println("   User: " + user);
        System.out.println("=========================================");
        
        // ===== LOAD MYSQL DRIVER =====
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        }
    }
    
    // =============================================================
    // GET CONNECTION — WALANG BAGUHIN DITO
    // =============================================================
    public static Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database connected!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return connection;
    }
    
    // =============================================================
    // CLOSE CONNECTION — WALANG BAGUHIN DITO
    // =============================================================
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✅ Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}