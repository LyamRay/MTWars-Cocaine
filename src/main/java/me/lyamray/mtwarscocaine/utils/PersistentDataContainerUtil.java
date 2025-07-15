package me.lyamray.mtwarscocaine.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import me.lyamray.mtwarscocaine.managers.coca.PlantValues;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@UtilityClass
public class PersistentDataContainerUtil {

    private static final Gson gson = new Gson();

    public <T> void set(PersistentDataContainer container, NamespacedKey key, PersistentDataType<T, T> type, T value) {
        container.set(key, type, value);
    }

    public <T> T get(PersistentDataContainer container, NamespacedKey key, PersistentDataType<T, T> type) {
        return container.get(key, type);
    }

    public <T> T getOrDefault(PersistentDataContainer container, NamespacedKey key, PersistentDataType<T, T> type, T def) {
        return container.has(key, type) ? container.get(key, type) : def;
    }

    public <T> boolean has(PersistentDataContainer container, NamespacedKey key, PersistentDataType<T, T> type) {
        return container.has(key, type);
    }

    public void remove(PersistentDataContainer container, NamespacedKey key) {
        container.remove(key);
    }



    public void toGson(PlantValues plantValue, Entity entity) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("u", plantValue.getUuid().toString());
        jsonObject.addProperty("w", plantValue.getLocation().getWorld().getName());
        jsonObject.addProperty("x", plantValue.getLocation().getX());
        jsonObject.addProperty("y", plantValue.getLocation().getY());
        jsonObject.addProperty("z", plantValue.getLocation().getZ());
        jsonObject.addProperty("s", plantValue.getState());
        jsonObject.addProperty("ish", plantValue.getIsBeingHarvested());

        entity.getPersistentDataContainer().set(Keys.PLANT_VALUE, PersistentDataType.STRING, gson.toJson(jsonObject));
    }

    public PlantValues fromGson(Entity entity) {
        String jsonStr = entity.getPersistentDataContainer().get(Keys.PLANT_VALUE, PersistentDataType.STRING);
        if (jsonStr == null) return null;

        JsonObject json = gson.fromJson(jsonStr, JsonObject.class);

        UUID uuid = UUID.fromString(json.get("u").getAsString());

        String world = json.get("w").getAsString();
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        Location location = new Location(Bukkit.getWorld(world), x, y, z);

        String state = json.get("s").getAsString();

        Boolean isBeingHarvested = json.has("ish") && !json.get("ish").isJsonNull()
                && json.get("ish").getAsBoolean();

        return new PlantValues(uuid, location, state, isBeingHarvested);
    }

    public PlantValues fromJsonString(String jsonStr) {
        JsonObject json = gson.fromJson(jsonStr, JsonObject.class);

        UUID uuid = UUID.fromString(json.get("u").getAsString());

        String worldName = json.get("w").getAsString();
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

        String state = json.get("s").getAsString();

        boolean isBeingHarvested = json.has("ish") && !json.get("ish").isJsonNull()
                && json.get("ish").getAsBoolean();

        return new PlantValues(uuid, location, state, isBeingHarvested);
    }

    public String toJsonString(PlantValues plantValue) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("u", plantValue.getUuid().toString());
        jsonObject.addProperty("w", plantValue.getLocation().getWorld().getName());
        jsonObject.addProperty("x", plantValue.getLocation().getX());
        jsonObject.addProperty("y", plantValue.getLocation().getY());
        jsonObject.addProperty("z", plantValue.getLocation().getZ());
        jsonObject.addProperty("s", plantValue.getState());
        jsonObject.addProperty("ish", plantValue.getIsBeingHarvested());

        return gson.toJson(jsonObject);
    }
}