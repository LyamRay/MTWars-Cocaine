package me.lyamray.mtwarscocaine;

import lombok.Getter;
import me.lyamray.mtwarscocaine.commands.TestCMD;
import me.lyamray.mtwarscocaine.database.Database;
import me.lyamray.mtwarscocaine.listeners.BlockClickListener;
import me.lyamray.mtwarscocaine.listeners.BlockPhysicsListener;
import me.lyamray.mtwarscocaine.listeners.coca.PlantClickListener;
import me.lyamray.mtwarscocaine.listeners.lab.DecoratedPotClickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;

public final class MTWarsCocaine extends JavaPlugin {

    @Getter
    private static MTWarsCocaine instance;

    @Getter
    private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        registerListeners();
        checkFolderAndTryConnect();
        getCommand("test").setExecutor(new TestCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            database.closeConnection();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to disable the database properly! " + exception);
        }
        getLogger().finest("The plugin has shutdown successfully!");
    }

    private void checkFolderAndTryConnect() {
        Path dataFolderPath = getDataFolder().toPath();

        try {

            if (Files.notExists(dataFolderPath)) {
                Files.createDirectories(dataFolderPath);
            }

            Path dbPath = dataFolderPath.resolve("MTWars-Cocaine.db");
            database = new Database(dbPath.toString());
            getLogger().info("Connected successfully to the database!");

        } catch (IOException e) {
            getLogger().severe("Failed to create the data folder: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);

        } catch (SQLException e) {
            getLogger().severe("Failed to connect to the local database: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerListeners() {
        Arrays.asList(
                new DecoratedPotClickListener(),
                new PlantClickListener(),
                new BlockClickListener(),
                new BlockPhysicsListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
