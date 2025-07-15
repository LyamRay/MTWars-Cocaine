package me.lyamray.mtwarscocaine.managers.growtimes;

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
        MTWarsCocaine.setPlantsCanGrow(RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now()));
        if (RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())) {
            MTWarsCocaine.getInstance().load
        }
        String message = RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())
                ? "Lyam zijn ballen zijn " + Math.PI + " cm in diameter!" : "Je bent maagd";
        Bukkit.broadcastMessage(message);
    }
}
