package me.shreyasayyengar.friendsextended.menu;

import org.bukkit.inventory.ItemStack;

public class MenuItem {

    private final ItemStack itemStack;
    private final MenuClick menuClick;

    /**
     * Basic Constructor
     *
     * @param itemStack {@link ItemStack}
     */
    public MenuItem(ItemStack itemStack) {
        this(itemStack, (player, item, slot) -> {
        });
    }

    /**
     * Basic Constructor
     *
     * @param itemStack {@link ItemStack}
     * @param menuClick {@link MenuClick}
     */
    public MenuItem(ItemStack itemStack, MenuClick menuClick) {
        this.itemStack = itemStack;
        this.menuClick = menuClick;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public MenuClick getMenuClick() {
        return menuClick;
    }
}
