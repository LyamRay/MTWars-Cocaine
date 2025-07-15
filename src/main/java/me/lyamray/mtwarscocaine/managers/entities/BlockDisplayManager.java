package me.lyamray.mtwarscocaine.managers.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockDisplayManager {

    @Getter
    private static final BlockDisplayManager instance = new BlockDisplayManager();

    public void createBlockDisplayEntity(Entity entity, PlantValues plantValues) {

        final BlockData kelp = Bukkit.createBlockData(Material.KELP);
        final BlockData kelpPlant = Bukkit.createBlockData(Material.KELP_PLANT);

        final Location location = plantValues.getLocation().getBlock().getLocation();
        final UUID uuid = plantValues.getUuid();

        switch (plantValues.getState()) {
            case "planted" -> {
                deleteEntitiesByUUID(entity, uuid);
                spawnEntity(kelpPlant, location, uuid, plantValues);
            }

            case "growing" -> {
                deleteEntitiesByUUID(entity, uuid);
                spawnEntityInColumn(location, uuid, plantValues, kelpPlant, kelp);
                EntityInteractionManager.getInstance().createEntity(1.5f, 0.5f,
                        location.toCenterLocation().add(0, -0.5, 0), uuid, plantValues);
            }

            case "grown" -> {
                deleteEntitiesByUUID(entity, uuid);
                spawnEntityInColumn(location, uuid, plantValues, kelpPlant, kelpPlant, kelp);
                EntityInteractionManager.getInstance().createEntity(2.5f, 0.5f,
                        location.toCenterLocation().add(0, -0.5, 0), uuid, plantValues);
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
            entity.getPersistentDataContainer().set(Keys.PLANT_ID, PersistentDataType.STRING, uuid.toString());
            PersistentDataContainerUtil.toGson(plantValues, entity);
        });
    }

    public void deleteEntitiesByUUID(Entity entity, UUID uuid) {
        entity.getWorld().getEntities().stream()
                .filter(entity1 -> {
                    var container = entity1.getPersistentDataContainer();
                    return PersistentDataContainerUtil.has(container, Keys.PLANT_ID, PersistentDataType.STRING) &&
                            uuid.toString().equals(PersistentDataContainerUtil.get(container, Keys.PLANT_ID, PersistentDataType.STRING));
                })
                .forEach(Entity::remove);
    }
}