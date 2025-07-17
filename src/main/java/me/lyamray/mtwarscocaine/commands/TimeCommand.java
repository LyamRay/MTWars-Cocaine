package me.lyamray.mtwarscocaine.commands;

import me.lyamray.mtwarscocaine.managers.growtimes.RandomGrowTime;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;


public class TimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        LocalTime start = LocalTime.now().minusMinutes(1);
        LocalTime end = LocalTime.now().plusHours(1);
        RandomGrowTime.getInstance().getRanges()
                .add(new RandomGrowTime.TimeRange(start, end));

        player.sendMessage(ChatColor.GREEN + "Time range added from " + start + " to " + end);
        return true;
    }
}
