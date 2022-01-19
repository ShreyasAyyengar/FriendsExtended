package me.shreyasayyengar.friendsextended.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Main menu functionality
 */
public class MenuBuilder {

    private final Menu menu;

    /**
     * Basic Constructor
     *
     * @param size  {@link Integer}
     * @param title {@link String}
     */
    public MenuBuilder(int size, String title) {
        this(new Menu(size, title));
    }

    /**
     * Basic constructor
     *
     * @param menu {@link Menu}
     */
    public MenuBuilder(Menu menu) {
        this.menu = menu;
    }

    /**
     * Fill empty slots in the inventory with the item of your choice
     *
     * @param itemStack itemStack
     * @return {@link MenuBuilder}
     */
    public MenuBuilder fillEmpty(ItemStack itemStack) {
        Inventory inventory = menu.getInventory();
        int count = menu.getItems().size();
        int size = menu.getInventory().getSize() - 1;

        if (count >= size) return this;

        for (int slot = 0; slot < size + 1; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item != null || item.getType() != Material.AIR) continue;
            set(slot, itemStack);
        }

        return this;
    }

    /**
     * Set an item in the inventory without a click event
     *
     * @param slot      {@link Integer}
     * @param itemStack {@link ItemStack}
     * @return {@link MenuBuilder}
     */
    public MenuBuilder set(int slot, @NotNull ItemStack itemStack) {
        set(slot, itemStack, (player, item, $) -> {
        });
        return this;
    }

    /**
     * Set the item in the inventory with a click event
     *
     * @param slot      {@link Integer}
     * @param itemStack {@link ItemStack}
     * @param event     {@link MenuClick}
     * @return {@link MenuBuilder}
     */
    public MenuBuilder set(int slot, @NotNull ItemStack itemStack, @NotNull MenuClick event) {
        menu.getInventory().setItem(slot, itemStack);
        menu.getItems().put(slot, new MenuItem(itemStack, event));
        return this;
    }

    /**
     * Close functionality
     *
     * @param eventConsumer {@link Consumer<InventoryCloseEvent>}
     * @return {@link MenuBuilder}
     */
    public MenuBuilder close(Consumer<InventoryCloseEvent> eventConsumer) {
        menu.onClose = eventConsumer;
        return this;
    }

    /**
     * Returns initial instance of {@link Menu}
     *
     * @return {@link Menu}
     */
    public Menu create(@NotNull Plugin plugin) {
        menu.register(plugin);
        return menu;
    }
}
