package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.entities.BlockDisplayManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlantRandomGrowTime {

    @Getter
    private static final PlantRandomGrowTime instance = new PlantRandomGrowTime();

    public void growTimer(Player player, PlantValues plantValue) {
        final int totalTicks = ThreadLocalRandom.current().nextInt(180, 300) * 20;
        UUID uuid = plantValue.getUuid();
        String state = nextState(plantValue);

        new BukkitRunnable() {
            int ticksRemaining = totalTicks;

            @Override
            public void run() {
                if (ticksRemaining <= 0) {
                    plantValue.setState(state);
                    BlockDisplayManager.getInstance().deleteEntitiesByUUID(player, uuid);
                    BlockDisplayManager.getInstance().createBlockDisplayEntity(player, plantValue);
                    PlantRandomGrowTime.getInstance().growTimer(player, plantValue);
                    cancel();
                    return;
                }

                ticksRemaining--;
            }
        }.runTaskTimer(MTWarsCocaine.getInstance(), 0L, 1L);
    }

    private String nextState(PlantValues plantValues) {
         return switch (plantValues.getState()) {
            case "planted" -> "growing";
            case "growing" -> "grown";
             default -> "planted";
        };
    }
}
