package me.lyamray.mtwarscocaine.utils;

import lombok.experimental.UtilityClass;
import me.lyamray.mtwarscocaine.MTWarsCocaine;
import org.bukkit.NamespacedKey;

@UtilityClass
public class Keys {

    public final NamespacedKey PLANT_ID = new NamespacedKey(MTWarsCocaine.getInstance(), "plant_id");
    public final NamespacedKey PLANT_VALUE = new NamespacedKey(MTWarsCocaine.getInstance(), "plant_value");
    public final NamespacedKey HARVESTER = new NamespacedKey(MTWarsCocaine.getInstance(), "harvester");
}