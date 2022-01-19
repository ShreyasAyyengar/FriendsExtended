package me.shreyasayyengar.friendsextended.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Basic interface that represents when a player clicks an item
 */
public interface MenuClick {

    /**
     * onClick Method
     *
     * @param player    {@link Player}
     * @param itemStack {@link ItemStack}
     */
    void onClick(Player player, ItemStack itemStack, int slot);
}
