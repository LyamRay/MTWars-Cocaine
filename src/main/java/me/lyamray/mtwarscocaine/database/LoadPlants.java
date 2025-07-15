package me.lyamray.mtwarscocaine.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.coca.PlantRandomGrowTime;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoadPlants {

    @Getter
    private static final LoadPlants instance = new LoadPlants();

    public void load() throws SQLException {
        Database database = MTWarsCocaine.getInstance().getDatabase();
        database.loadPlants();

        Map<UUID, String> plantMap = database.getPlantMap();
        Map<UUID, PlantValues> plantValuesMap = MTWarsCocaine.getPlantValuesMap();
        plantValuesMap.clear();

        for (var entry : plantMap.entrySet()) {
            UUID uuid = entry.getKey();
            String jsonString = entry.getValue();

            Entity entity = findEntityByPlantId(uuid);
            if (entity == null) {
                log.warn("No entity found for plant UUID {}", uuid);
                continue;
            }

            PlantValues plantValues = PersistentDataContainerUtil.fromJsonString(jsonString);
            PersistentDataContainerUtil.toGson(plantValues, entity);

            plantValuesMap.put(uuid, plantValues);

            PlantRandomGrowTime.getInstance().growTimer(entity, plantValues);
        }
    }

    private Entity findEntityByPlantId(UUID uuid) {
        String idString = uuid.toString();
        for (var world : Bukkit.getWorlds()) {
            for (var entity : world.getEntities()) {
                String storedId = PersistentDataContainerUtil.get(
                        entity.getPersistentDataContainer(),
                        Keys.PLANT_ID,
                        PersistentDataType.STRING
                );
                if (idString.equals(storedId)) {
                    return entity;
                }
            }
        }
        return null;
    }
}
