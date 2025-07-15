package me.lyamray.mtwarscocaine.managers.growtimes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.database.LoadPlants;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartCheckForGrowTime {

    @Getter
    private static final StartCheckForGrowTime instance = new StartCheckForGrowTime();

    @SneakyThrows
    public void startTimeCheckTask() {
        Bukkit.getScheduler().runTaskTimer(MTWarsCocaine.getInstance(), this::checkTimeAndLog, 0L, 1200L);
    }

    private void checkTimeAndLog() throws SQLException {
        MTWarsCocaine.setPlantsCanGrow(RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now()));
        if (RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())) {
            try {
                LoadPlants.getInstance().load();
            } catch (SQLException e) {
               throw new SQLException("Could not load the plants from the database." + e);
            }
        }

        String message = RandomGrowTime.getInstance().isWithinAnyRange(LocalTime.now())
                ? "Lyam zijn ballen zijn " + Math.PI + " cm in diameter!"
                : "Je bent maagd";
        Bukkit.broadcastMessage(message);
    }
}
