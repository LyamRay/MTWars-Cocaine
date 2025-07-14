package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lyamray.mtwarscocaine.managers.entities.BlockDisplayManager;
import me.lyamray.mtwarscocaine.managers.entities.TextDisplayManager;
import me.lyamray.mtwarscocaine.utils.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HarvestManager {

    @Getter
    private static final HarvestManager instance = new HarvestManager();

    @Getter
    @Setter
    private static HashMap<UUID, Boolean> gathering = new HashMap<>();

    public void harvestPlant(Player player, PlantValues plantValue) {
        final Location baseLocation = plantValue.getLocation().getBlock().getLocation();
        final UUID uuid = plantValue.getUuid();
        final String state = plantValue.getState();

        Location textLocation;
        TextDisplay textDisplay;

        switch (state) {
            case "grown" -> {
                textLocation = baseLocation.toCenterLocation().add(0, 2.3, 0);
                textDisplay = TextDisplayManager.getInstance().createEntity(textLocation, uuid, plantValue, "10s");
                HarvestTimer.getInstance().handleTimer(textDisplay, state, player, plantValue);

                player.sendMessage(ChatColor.color("<gray>Je bent gestart met het plukken van een " +
                        "<green>volgroeide<green> <gray>cocaïne plant!<gray>"));
            }

            case "growing" -> {
                textLocation = baseLocation.toCenterLocation().add(0, 1.3, 0);
                textDisplay = TextDisplayManager.getInstance().createEntity(textLocation, uuid, plantValue, "10s");
                HarvestTimer.getInstance().handleTimer(textDisplay, state, player, plantValue);

                player.sendMessage(ChatColor.color("<gray>Je bent gestart met het plukken van een " +
                        "<green>niet-volgroeide<green> <gray>cocaïne plant!<gray>"));
                player.sendMessage(ChatColor.color("<gray>Let op! Deze plant zal minder bladeren geven " +
                        "dan een <green>volgroeide<green> <gray>plant!<gray>"));
            }
        }
    }

    public void succesfullyHarvested(Player player, PlantValues plantValue, String state) {
        int leaves = 0;

        switch (state) {
            case "growing" -> leaves = ThreadLocalRandom.current().nextInt(2, 5); // 2–4
            case "grown" -> leaves = ThreadLocalRandom.current().nextInt(4, 8);   // 4–7
        }

        cleanUp(player, plantValue);
        replantPlant(player, plantValue);
    }

    private void cleanUp(Player player, PlantValues plantValue) {
        BlockDisplayManager.getInstance().deleteEntitiesByUUID(player, plantValue.getUuid());
    }

    private void replantPlant(Player player, PlantValues plantValue) {
        plantValue.setIsBeingHarvested(false);
        plantValue.setState("planted");
        BlockDisplayManager.getInstance().createBlockDisplayEntity(player, plantValue);
        PlantRandomGrowTime.getInstance().growTimer(player, plantValue);
    }
}
