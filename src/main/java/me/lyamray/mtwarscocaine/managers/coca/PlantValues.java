package me.lyamray.mtwarscocaine.managers.coca;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlantValues {

    UUID uuid;
    Location location;
    String state;

}
