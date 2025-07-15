package me.lyamray.mtwarscocaine.database;

import lombok.Getter;
import lombok.Setter;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Database {

    private final Connection connection;

    @Getter
    @Setter
    private Map<UUID, String> plantMap = new HashMap<>();

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS plants (
                    uuid TEXT PRIMARY KEY,
                    plantvalue TEXT NOT NULL
                );
            """);
        }
    }

    public void loadPlants() throws SQLException {
        plantMap.clear();
        String sql = "SELECT uuid, plantvalue FROM plants";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String plantValueJson = rs.getString("plantvalue");
                plantMap.put(uuid, plantValueJson);
            }
        }
    }

    public void savePlants() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM plants");
        }

        String sql = "INSERT INTO plants (uuid, plantvalue) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Map.Entry<UUID, String> entry : plantMap.entrySet()) {
                ps.setString(1, entry.getKey().toString());
                ps.setString(2, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void addPlant(PlantValues plantValues) {
        plantMap.put(plantValues.getUuid(), PersistentDataContainerUtil.toJsonString(plantValues));
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
