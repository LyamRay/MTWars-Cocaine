package me.lyamray.mtwarscocaine;

import lombok.Getter;
import lombok.Setter;
import me.lyamray.mtwarscocaine.commands.TestCMD;
import me.lyamray.mtwarscocaine.database.Database;
import me.lyamray.mtwarscocaine.listeners.BlockClickListener;
import me.lyamray.mtwarscocaine.listeners.coca.PlantClickListener;
import me.lyamray.mtwarscocaine.listeners.lab.DecoratedPotClickListener;
import me.lyamray.mtwarscocaine.managers.growtimes.RandomGrowTime;
import me.lyamray.mtwarscocaine.managers.growtimes.StartCheckForGrowTime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Arrays;

public final class MTWarsCocaine extends JavaPlugin {

    @Getter
    private static MTWarsCocaine instance;

    @Getter
    @Setter
    private static Boolean plantsCanGrow;

    @Getter
    private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        registerListeners();
        connectDatabase();

        RandomGrowTime.getInstance().generate();
        StartCheckForGrowTime.getInstance().startTimeCheckTask();

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

    private void connectDatabase() {
        try {
            if (!getDataFolder().exists()) {
                if (!getDataFolder().mkdirs()) {
                    getLogger().warning("Failed to create the data folder and database! Shutting down..");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }

            database = new Database(getDataFolder().getAbsolutePath() + "/MTWars-Cocaine.db");
            getLogger().info("Connected successfully to the database!");

        } catch (SQLException exception) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException("Failed to connect to the database. " + exception);
        }
    }

    public void registerListeners() {
        Arrays.asList(
                new DecoratedPotClickListener(),
                new PlantClickListener(),
                new BlockClickListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
