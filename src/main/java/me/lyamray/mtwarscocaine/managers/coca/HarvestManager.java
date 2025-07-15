package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lyamray.mtwarscocaine.managers.entities.BlockDisplayManager;
import me.lyamray.mtwarscocaine.managers.entities.TextDisplayManager;
import me.lyamray.mtwarscocaine.utils.ChatColor;
import me.lyamray.mtwarscocaine.utils.ItemStacks;
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
                TextDisplayManager.getInstance().hideEntity(player, textDisplay);
                HarvestTimer.getInstance().handleTimer(textDisplay, state, player, plantValue);
                PlantRandomGrowTimer.getInstance().cancelTimer(uuid);
            }

            case "growing" -> {
                textLocation = baseLocation.toCenterLocation().add(0, 1.3, 0);
                textDisplay = TextDisplayManager.getInstance().createEntity(textLocation, uuid, plantValue, "10s");
                TextDisplayManager.getInstance().hideEntity(player, textDisplay);
                HarvestTimer.getInstance().handleTimer(textDisplay, state, player, plantValue);
                PlantRandomGrowTimer.getInstance().cancelTimer(uuid);
                player.sendMessage(ChatColor.color("<gradient:#555856:#555856>Let op! Deze plant zal minder bladeren " +
                        "geven dan een <color:#61ffab>volgroeide</color> plant!</gradient>"));
            }
        }
    }

    public void succesfullyHarvested(Player player, PlantValues plantValue, String state) {
        int leaves = 0;

        switch (state) {
            case "growing" -> {
                leaves = ThreadLocalRandom.current().nextInt(1, 3); // 2–4
                player.getInventory().addItem(ItemStacks.cocaineLeaves(leaves));
            }

            case "grown" -> {
                leaves = ThreadLocalRandom.current().nextInt(2, 5);   // 4–7
                player.getInventory().addItem(ItemStacks.cocaineLeaves(leaves));
            }
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

        PlantRandomGrowTimer.getInstance().cancelTimer(plantValue.getUuid());
        PlantRandomGrowTimer.getInstance().startTimer(plantValue.getUuid(), player, plantValue);
    }
}
