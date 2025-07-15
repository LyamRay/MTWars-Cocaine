package me.lyamray.mtwarscocaine.listeners.coca;

import me.lyamray.mtwarscocaine.managers.coca.HarvestManager;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.ChatColor;
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

    private Player player;


    @EventHandler
    public void onUnknownEntityUse(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        this.player = player;

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
            claimPlant(container, playerId, entity, plantValue);
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

    private void claimPlant(PersistentDataContainer container, UUID playerId, Entity entity, PlantValues plantValues) {
        PersistentDataContainerUtil.set(container, Keys.HARVESTER, PersistentDataType.STRING, playerId.toString());
        plantValues.setIsBeingHarvested(true);
        PersistentDataContainerUtil.toGson(plantValues, entity);

        HarvestManager.getInstance().harvestPlant(player, plantValues);
    }

    private void sendHarvestMessage(Player player, UUID playerId, UUID harvesterId) {
        String message = playerId.equals(harvesterId)
                ? "<gradient:#555856:#555856>Je bent deze plant al aan het plukken!</gradient>"
                : "<gradient:#555856:#555856>Deze plant wordt al door <color:#61ffab>" +
                Optional.ofNullable(Bukkit.getOfflinePlayer(harvesterId).getName())
                .orElse("onbekend") + "</color> geplukt!</gradient>";

        player.sendMessage(ChatColor.color(message));
    }
}