package me.lyamray.mtwarscocaine.managers.growtimes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.database.LoadPlants;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartCheckForGrowTime {

    @Getter
    private static final StartCheckForGrowTime instance = new StartCheckForGrowTime();

    private boolean isLoaded = false;

    public void startTimeCheckTask() {
        Bukkit.getScheduler().runTaskTimer(MTWarsCocaine.getInstance(), this::checkTimeAndLog, 0L, 1200L);
    }

    private void checkTimeAndLog() {
        MTWarsCocaine.setPlantsCanGrow(RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now()));

        if (RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())) {
            if (isLoaded) return;
            try {
                isLoaded = true;
                sendTimeMessage();
                LoadPlants.getInstance().load();
            } catch (SQLException e) {
                MTWarsCocaine.getInstance().getLogger().severe("Kon planten niet laden uit de database: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            isLoaded = false;
        }
    }

    private void sendTimeMessage() {
        RandomMessages.getInstance().maybeBroadcastGrowTimeMessage();
    }
}
