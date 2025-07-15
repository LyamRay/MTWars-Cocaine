package me.lyamray.mtwarscocaine;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lyamray.mtwarscocaine.commands.TestCMD;
import me.lyamray.mtwarscocaine.database.Database;
import me.lyamray.mtwarscocaine.database.LoadPlants;
import me.lyamray.mtwarscocaine.database.SavePlants;
import me.lyamray.mtwarscocaine.listeners.BlockClickListener;
import me.lyamray.mtwarscocaine.listeners.coca.PlantClickListener;
import me.lyamray.mtwarscocaine.listeners.lab.DecoratedPotClickListener;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.managers.growtimes.StartCheckForGrowTime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public final class MTWarsCocaine extends JavaPlugin {

    @Getter
    private static MTWarsCocaine instance;

    @Setter
    @Getter
    private static boolean plantsCanGrow;

    @Getter
    @Setter
    private Database database;

    @Getter
    @Setter
    private static Map<UUID, PlantValues> plantValuesMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        if (!setupDataFolder()) return;
        if (!setupDatabase()) return;

        StartCheckForGrowTime.getInstance().startTimeCheckTask();
        registerListeners();
        Bukkit.getScheduler().runTaskLater(this, this::loadPlantsSafely, 60L);

        getCommand("test").setExecutor(new TestCMD());

        log.info("MTWarsCocaine enabled.");
    }

    @Override
    public void onDisable() {
        try {
            SavePlants.getInstance().savePlants();
        } catch (SQLException e) {
            log.error("Error saving plants on shutdown", e);
        }

        if (database != null) {
            try {
                database.closeConnection();
            } catch (SQLException e) {
                log.error("Error closing database connection", e);
            }
        }

        log.info("MTWarsCocaine disabled.");
    }

    private boolean setupDataFolder() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            log.error("Could not create plugin data folder.");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private boolean setupDatabase() {
        try {
            database = new Database(getDataFolder().getAbsolutePath() + "/MTWars-Cocaine.db");
            log.info("Database connected successfully.");
            return true;
        } catch (SQLException e) {
            log.error("Failed to connect to database", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new DecoratedPotClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlantClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockClickListener(), this);
    }

    private void loadPlantsSafely() {
        try {
            LoadPlants.getInstance().load();
        } catch (SQLException e) {
            log.error("Failed to load plants", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
