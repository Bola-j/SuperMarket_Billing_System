package com.supermarket.pos.util;

/**
 * DatabaseTest - Unit test for DatabaseManager
 * Tests database connection and table creation
 */
public class DatabaseTest {

    /**
     * Test database operations
     */
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("Database Manager Test Suite");
        System.out.println("═══════════════════════════════════════\n");

        try {
            // Test 1: Initialize database and create tables
            System.out.println("TEST 1: Initialize Database");
            System.out.println("───────────────────────────────────────");
            DatabaseManager.initializeDatabase();
            System.out.println();

            // Test 2: Test connection
            System.out.println("TEST 2: Connection Test");
            System.out.println("───────────────────────────────────────");
            boolean connectionTest = DatabaseManager.testConnection();
            System.out.println();

            // Test 3: Close connection
            System.out.println("TEST 3: Close Connection");
            System.out.println("───────────────────────────────────────");
            DatabaseManager.close();
            System.out.println();

            System.out.println("═══════════════════════════════════════");
            System.out.println("All tests completed!");
            System.out.println("Database file: " + DatabaseManager.getDatabasePath());
            System.out.println("═══════════════════════════════════════");

        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
