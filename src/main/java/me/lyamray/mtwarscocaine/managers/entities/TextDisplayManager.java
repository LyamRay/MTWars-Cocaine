package me.lyamray.mtwarscocaine.managers.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import me.lyamray.mtwarscocaine.utils.Keys;
import me.lyamray.mtwarscocaine.utils.PersistentDataContainerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextDisplayManager {

    @Getter
    private static final TextDisplayManager instance = new TextDisplayManager();

    public TextDisplay createEntity(Location location, UUID uuid, PlantValues plantValues, String text) {
        return location.getWorld().spawn(location, TextDisplay.class, entity -> {
            entity.setBillboard(Display.Billboard.CENTER);
            entity.text(Component.text(text));
            entity.getPersistentDataContainer().set(Keys.PLANT_ID, PersistentDataType.STRING, uuid.toString());
            PersistentDataContainerUtil.toGson(plantValues, entity);
        });
    }

    public void editEntity(TextDisplay entity, String text) {
        entity.text(Component.text(text + "s"));
    }

    public void deleteEntity(TextDisplay textDisplay) {
        textDisplay.remove();
    }
}
