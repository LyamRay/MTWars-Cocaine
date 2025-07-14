package me.lyamray.mtwarscocaine.managers.coca;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HarvestManager {

    @Getter
    private static final HarvestManager instance = new HarvestManager();

    @Getter
    @Setter
    private static HashMap<UUID, Boolean> gathering = new HashMap<>();

    public void harvestPlant(Player player, PlantValues plantValue) {

    }

    private void getState() {

    }

    private void handleTimer() {

    }
}
