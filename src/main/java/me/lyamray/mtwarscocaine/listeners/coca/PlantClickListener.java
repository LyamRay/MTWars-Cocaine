package me.lyamray.mtwarscocaine.listeners.coca;

import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;


public class PlantClickListener implements Listener {

    @EventHandler
    public void onUnknownEntityUse(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.INTERACTION) return;
        getIfIdExists(entity, player);
    }

    private void getIfIdExists(Entity entity, Player player) {
        if (!PersistentDataContainerUtil.has(entity.getPersistentDataContainer(),
                Keys.PLANT_ID, PersistentDataType.STRING)) return;

        checkIfIsBeingHarvested(entity, player);
    }

    private void checkIfIsBeingHarvested(Entity entity, Player player) {
        PlantValues plantValue = PersistentDataContainerUtil.fromGson(entity);
        if (plantValue == null) return;

        UUID playerId = player.getUniqueId();
        PersistentDataContainer container = entity.getPersistentDataContainer();

        UUID harvesterId = getHarvesterId(container);
        boolean isHarvested = Boolean.TRUE.equals(plantValue.getIsBeingHarvested());

        if (!isHarvested || harvesterId == null) {
            claimPlant(container, playerId, plantValue, entity);
            return;
        }

        sendHarvestMessage(player, playerId, harvesterId);
    }

    private UUID getHarvesterId(PersistentDataContainer container) {
        String harvesterStr = PersistentDataContainerUtil.get(container, Keys.HARVESTER, PersistentDataType.STRING);
        if (harvesterStr == null) return null;

        try {
            return UUID.fromString(harvesterStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void claimPlant(PersistentDataContainer container, UUID playerId, PlantValues plantValue, Entity entity) {
        PersistentDataContainerUtil.set(container, Keys.HARVESTER, PersistentDataType.STRING, playerId.toString());
        plantValue.setIsBeingHarvested(true);
        PersistentDataContainerUtil.toGson(plantValue, entity);
    }

    private void sendHarvestMessage(Player player, UUID playerId, UUID harvesterId) {
        String message = playerId.equals(harvesterId)
                ? "Jij bent deze plant al aan het plukken!"
                : "Deze plant wordt al door " + Optional.ofNullable(Bukkit.getOfflinePlayer(harvesterId).getName())
                .orElse("onbekend") + " geplukt!";

        player.sendMessage(message);
    }
}