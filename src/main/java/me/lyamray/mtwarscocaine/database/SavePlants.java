package me.lyamray.mtwarscocaine.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.listeners.coca.PlantClickListener;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.managers.entities.TextDisplayManager;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavePlants {

    @Getter
    private static final SavePlants instance = new SavePlants();

    public void savePlants() throws SQLException {
        MTWarsCocaine.getPlantValuesMap().clear();

        for (var world : Bukkit.getWorlds()) {
            processWorldEntities(world);
        }

        savePlantValuesToDatabase();
    }

    private void processWorldEntities(org.bukkit.World world) {
        for (Entity entity : world.getEntities()) {
            if (!hasPlantValue(entity)) continue;

            PlantValues plantValues = getPlantValuesFromEntity(entity);
            if (plantValues == null) {
                log.warn("Failed to parse PlantValues from entity: {}", entity);
                continue;
            }

            resetHarvestState(plantValues, entity);

            if (entity instanceof TextDisplay textDisplay) {
                deleteTextDisplayEntity(textDisplay);
            } else {
                log.warn("Entity with PLANT_VALUE is not a TextDisplay: {} ({})",
                        entity.getType(), entity.getUniqueId());
            }

            MTWarsCocaine.getPlantValuesMap().put(plantValues.getUuid(), plantValues);
        }
    }

    private boolean hasPlantValue(Entity entity) {
        return entity.getPersistentDataContainer().has(Keys.PLANT_VALUE, PersistentDataType.STRING);
    }

    private PlantValues getPlantValuesFromEntity(Entity entity) {
        return PersistentDataContainerUtil.fromGson(entity);
    }

    private void resetHarvestState(PlantValues plantValues, Entity entity) {
        PlantClickListener.getIsGathering().clear();
        PersistentDataContainerUtil.remove(entity.getPersistentDataContainer(), Keys.HARVESTER);
        plantValues.setIsBeingHarvested(false);
    }

    private void deleteTextDisplayEntity(TextDisplay textDisplay) {
        TextDisplayManager.getInstance().deleteEntity(textDisplay);
        log.info("Deleted TextDisplay entity: {}", textDisplay);
    }

    private void savePlantValuesToDatabase() throws SQLException {
        Map<UUID, String> jsonMap = new HashMap<>(MTWarsCocaine.getPlantValuesMap().size());

        for (Map.Entry<UUID, PlantValues> entry : MTWarsCocaine.getPlantValuesMap().entrySet()) {
            jsonMap.put(entry.getKey(), PersistentDataContainerUtil.toJsonString(entry.getValue()));
        }

        var database = MTWarsCocaine.getInstance().getDatabase();
        database.setPlantMap(jsonMap);
        database.savePlants();

        log.info("Saved {} plants to the database.", MTWarsCocaine.getPlantValuesMap().size());
    }
}
