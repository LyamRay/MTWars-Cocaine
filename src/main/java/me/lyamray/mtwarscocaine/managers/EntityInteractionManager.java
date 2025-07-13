package me.lyamray.mtwarscocaine.managers;

import me.lyamray.mtwarscocaine.MTWarsCocaine;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class EntityInteractionManager {

    public void createEntity(Float height, Float width, Location location, Player player, UUID uuid) {

        final float defaultSize = 1.0f;
        float finalHeight = (height != null && height != defaultSize) ? height : defaultSize;
        float finalWidth = (width != null && width != defaultSize) ? width : defaultSize;

        location.getWorld().spawn(location, Interaction.class, entity -> {
            entity.setInteractionHeight(finalHeight);
            entity.setInteractionWidth(finalWidth);
            entity.setMetadata("uuid", new FixedMetadataValue(MTWarsCocaine.getInstance(), uuid));
        });
    }
}