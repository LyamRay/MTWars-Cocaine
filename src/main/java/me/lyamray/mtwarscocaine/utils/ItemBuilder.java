package me.lyamray.mtwarscocaine.utils;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material).add(amount);
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(ChatColor.color(name).decoration(TextDecoration.ITALIC, false));
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setLore(new ArrayList<>());
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        this.addLoreLine(ChatColor.color(line).decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public void addLoreLine(Component line) {
        ItemMeta im = itemStack.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.lore());
        lore.add(line);
        im.lore(lore);itemStack.setItemMeta(im);
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemStack toItemStack() {
        return itemStack.clone();
    }
}
