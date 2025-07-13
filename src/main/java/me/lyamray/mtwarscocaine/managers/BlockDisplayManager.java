package me.lyamray.mtwarscocaine.managers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockDisplayManager {

    @Getter
    private static final BlockDisplayManager instance = new BlockDisplayManager();

    public void createBlockDisplayEntity(Player player, PlantValues plantValues) {

        final BlockData kelp = Bukkit.createBlockData(Material.KELP);
        final BlockData kelpPlant = Bukkit.createBlockData(Material.KELP_PLANT);

        final Location location = plantValues.getLocation().getBlock().getLocation();
        final UUID uuid = plantValues.getUuid();

        switch (plantValues.getState()) {
            case "planted" -> {
                deleteEntitiesByUUID(player, uuid);
                spawnEntity(kelpPlant, location, uuid, plantValues);
            }

            case "growing" -> {
                deleteEntitiesByUUID(player, uuid);
                spawnEntityInColumn(location, uuid, plantValues,kelpPlant, kelp);
            }

            case "grown" -> {
                deleteEntitiesByUUID(player, uuid);
                spawnEntityInColumn(location, uuid, plantValues,kelpPlant, kelpPlant, kelp);
            }
        }
    }

    private void spawnEntityInColumn(Location baseLocation, UUID uuid, PlantValues plantValues, BlockData... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            spawnEntity(blocks[i], baseLocation.clone().add(0, i, 0), uuid, plantValues);
        }
    }

    private void spawnEntity(BlockData blockData, Location location, UUID uuid, PlantValues plantValues) {
        location.getWorld().spawn(location, BlockDisplay.class, entity -> {
            entity.setBlock(blockData);
            entity.setRotation(0f, 0f);
            entity.setMetadata("plant_id", new FixedMetadataValue(MTWarsCocaine.getInstance(), uuid));
            entity.setMetadata("plantvalue", new FixedMetadataValue(MTWarsCocaine.getInstance(), plantValues));
        });
    }

    public void deleteEntitiesByUUID(Player player, UUID uuid) {
        player.getWorld().getEntities().stream()
                .filter(entity -> entity.hasMetadata("plant_id"))
                .filter(entity -> entity.getMetadata("plant_id").stream()
                        .anyMatch(meta -> meta.getOwningPlugin().equals(MTWarsCocaine.getInstance()) &&
                                uuid.equals(meta.value())))
                .forEach(Entity::remove);
    }
}