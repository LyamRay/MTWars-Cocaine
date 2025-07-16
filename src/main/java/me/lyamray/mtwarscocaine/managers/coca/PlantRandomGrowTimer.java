package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.entities.BlockDisplayManager;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlantRandomGrowTimer {

    @Getter
    private static final PlantRandomGrowTimer instance = new PlantRandomGrowTimer();

    private final Map<UUID, BukkitRunnable> taskMap = new HashMap<>();

    public void startTimer(UUID uuid, Entity entity, PlantValues plantValue) {
        int randomTicks = ThreadLocalRandom.current().nextInt(180, 300) * 20;

        if (plantValue.isBeingHarvested || plantValue.getState().equalsIgnoreCase("grown") || !MTWarsCocaine.isPlantsCanGrow()) {
            return;
        }

        cancelTimer(uuid);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (plantValue.isBeingHarvested) {
                    this.cancel();
                }

                if (plantValue.getState().equalsIgnoreCase("grown")) {
                    cancelTimer(uuid);
                    return;
                }

                String nextState = nextState(plantValue);
                plantValue.setState(nextState);
                log.info("The plant {} has grown to the state {}!", uuid, nextState);
                BlockDisplayManager.getInstance().deleteEntitiesByUUID(entity, uuid);
                BlockDisplayManager.getInstance().createBlockDisplayEntity(entity, plantValue);

                if (nextState.equalsIgnoreCase("grown")) {
                    cancelTimer(uuid);
                }
            }
        };

        task.runTaskTimer(MTWarsCocaine.getInstance(), randomTicks, randomTicks);
        taskMap.put(uuid, task);
    }

    public void cancelTimer(UUID uuid) {
        BukkitRunnable task = taskMap.remove(uuid);
        if (task != null) {
            task.cancel();
        }
    }

    private String nextState(PlantValues plantValue) {
        return switch (plantValue.getState().toLowerCase()) {
            case "planted" -> "growing";
            case "growing", "grown" -> "grown";
            default -> "planted";
        };
    }
}
