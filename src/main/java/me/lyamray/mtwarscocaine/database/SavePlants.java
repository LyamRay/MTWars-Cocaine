package me.lyamray.mtwarscocaine.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import me.lyamray.mtwarscocaine.managers.coca.PlantRandomGrowTime;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Bukkit;
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

        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
            if (entity.getPersistentDataContainer().has(Keys.PLANT_VALUE, PersistentDataType.STRING)) {
                PlantValues plantValues = PersistentDataContainerUtil.fromGson(entity);
                if (plantValues != null) MTWarsCocaine.getPlantValuesMap().put(plantValues.getUuid(), plantValues);
            }
        }));

        Map<UUID, String> jsonMap = new HashMap<>();
        MTWarsCocaine.getPlantValuesMap().forEach((uuid, plantValues) -> {
            jsonMap.put(uuid, PersistentDataContainerUtil.toJsonString(plantValues));
        });

        MTWarsCocaine.getInstance().getDatabase().setPlantMap(jsonMap);
        MTWarsCocaine.getInstance().getDatabase().savePlants();

        log.info("Saved {} plants to the database.", MTWarsCocaine.getPlantValuesMap().size());
    }
}
