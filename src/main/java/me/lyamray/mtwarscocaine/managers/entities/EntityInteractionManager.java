package me.lyamray.mtwarscocaine.managers.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityInteractionManager {

    @Getter
    private static final EntityInteractionManager instance = new EntityInteractionManager();

    public void createEntity(Float height, Float width, Location location, UUID uuid, PlantValues plantValues) {

        final float defaultSize = 1.0f;
        float finalHeight = (height != null && height != defaultSize) ? height : defaultSize;
        float finalWidth = (width != null && width != defaultSize) ? width : defaultSize;

        location.getWorld().spawn(location, Interaction.class, entity -> {
            entity.setInteractionHeight(finalHeight);
            entity.setInteractionWidth(finalWidth);
            entity.getPersistentDataContainer().set(Keys.PLANT_ID, PersistentDataType.STRING, uuid.toString());
            PersistentDataContainerUtil.toGson(plantValues, entity);
        });
    }
}