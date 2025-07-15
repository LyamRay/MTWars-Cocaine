package me.lyamray.mtwarscocaine.commands;

import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.database.Database;
import me.lyamray.mtwarscocaine.managers.entities.BlockDisplayManager;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (strings.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /test <planted|growing|grown>");
            return true;
        }

        String state = strings[0].toLowerCase();
        if (!state.equals("planted") && !state.equals("growing") && !state.equals("grown")) {
            player.sendMessage(ChatColor.RED + "Invalid plant state. Use: planted, growing, or grown.");
            return true;
        }

        UUID uuid = UUID.randomUUID();
        Location location = player.getLocation();
        PlantValues plantValue = new PlantValues(
                uuid,
                location,
                state,
                false
        );

        MTWarsCocaine.getInstance().getDatabase().addPlant(plantValue);
        BlockDisplayManager.getInstance().createBlockDisplayEntity(player, plantValue);

        return true;
    }
}