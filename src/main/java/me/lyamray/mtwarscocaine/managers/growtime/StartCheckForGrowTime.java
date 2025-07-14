package me.lyamray.mtwarscocaine.managers.growtime;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import org.bukkit.Bukkit;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartCheckForGrowTime {

    @Getter
    private static final StartCheckForGrowTime instance = new StartCheckForGrowTime();

    public void startTimeCheckTask() {
        Bukkit.getScheduler().runTaskTimer(MTWarsCocaine.getInstance(), this::checkTimeAndLog, 0L, 1200L);
    }

    private void checkTimeAndLog() {
        if (RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())) {
            MTWarsCocaine.getInstance().getLogger().info("Hey, het is tijd!");
        }
    }
}
