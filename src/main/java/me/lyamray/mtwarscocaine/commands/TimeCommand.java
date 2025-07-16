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
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        RandomGrowTime.getInstance().getRanges()
                .add(new RandomGrowTime.TimeRange(LocalTime.now(), LocalTime.now().plusHours(1)));
        player.sendMessage(me.lyamray.mtwarscocaine.utils.ChatColor.color("Het is tijd om te plukken! //DEBUG"));

        return true;
    }
}
