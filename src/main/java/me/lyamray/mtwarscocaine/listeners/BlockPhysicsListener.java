package me.lyamray.mtwarscocaine.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.getSourceBlock().getType() == Material.KELP
                || event.getSourceBlock().getType() == Material.KELP_PLANT) {
            event.setCancelled(true);
        }
    }
}
