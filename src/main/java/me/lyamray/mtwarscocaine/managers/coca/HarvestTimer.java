package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.entities.TextDisplayManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HarvestTimer {

    @Getter
    private static final HarvestTimer instance = new HarvestTimer();

    public void handleTimer(TextDisplay textDisplay, String state, Player player, PlantValues plantValue) {
        final int totalTicks = 15 * 20;

        new BukkitRunnable() {
            int ticksRemaining = totalTicks;

            @Override
            public void run() {
                if (ticksRemaining <= 0) {
                    TextDisplayManager.getInstance().deleteEntity(textDisplay);
                    HarvestManager.getInstance().succesfullyHarvested(player, plantValue,state);
                    cancel();
                    return;
                }

                double secondsRemaining = ticksRemaining / 20.0;
                String timeText = String.format(Locale.US, "%.1f", secondsRemaining);

                TextDisplayManager.getInstance().editEntity(textDisplay, timeText);

                ticksRemaining--;
            }
        }.runTaskTimer(MTWarsCocaine.getInstance(), 0L, 1L);
    }
}
