package me.lyamray.mtwarscocaine.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ItemStacks {

    public ItemStack cocaineLeaves(int amount) {
        String name = "<color:#61ffab>Cocaïne Blad</color>";
        String lore = "<gradient:#555856:#555856>Gebruik dit blad in een <color:#61ffab>speciaal</color> recept!<gradient>";
        return new ItemBuilder(Material.KELP, amount)
                .setName(name)
                .addLoreLine(lore)
                .toItemStack();
    }

    public ItemStack exit() {
        String name = "<gold>Close!</gold>";
        String lore = "<gradient:#ff9500:#fbff00>Click here to close this menu!</gradient>";

        return new ItemBuilder(Material.BARRIER, 0)
                .setName(name)
                .addLoreLine(lore)
                .toItemStack();
    }

    public ItemStack EmptyPane() {
        String name = " ";
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE,0)
                .setName(name)
                .toItemStack();
    }

    public ItemStack EmptyClosePane() {
        String name = " ";
        return new ItemBuilder(Material.RED_STAINED_GLASS_PANE,0)
                .setName(name)
                .toItemStack();
    }
}