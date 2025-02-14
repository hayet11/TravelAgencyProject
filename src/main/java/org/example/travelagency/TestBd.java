package org.example.travelagency;

import Utils.DataSource;
import java.sql.Connection;

public class TestBd {
    public static void main(String[] args) {
        // Get database connection instance
        DataSource dataSource = DataSource.getInstance();
        Connection connection = dataSource.getConn();

        // Check if the connection is successful
        if (connection != null) {
            System.out.println("Database connection established successfully!");
        } else {
            System.out.println("Failed to establish a database connection.");
        }
    }
}
