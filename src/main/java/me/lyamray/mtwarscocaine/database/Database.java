package me.lyamray.mtwarscocaine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS players (
                            uuid TEXT PRIMARY KEY,
                        );
                    """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}