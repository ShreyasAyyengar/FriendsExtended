package me.shreyasayyengar.friendsextended.menu.interfaces;

import me.shreyasayyengar.friendsextended.menu.MenuItem;
import me.shreyasayyengar.friendsextended.menu.SimpleMenu;
import me.shreyasayyengar.friendsextended.menu.adapter.DisplayApplication;
import me.shreyasayyengar.friendsextended.menu.adapter.InventoryAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Base utility interface for {@link Menu}
 */
public interface Menu extends DisplayApplication, InventoryAdapter {

    static Menu create(@NotNull MenuDisplay display) {
        return new SimpleMenu(display);
    }

    /**
     * Removing soon
     *
     * @param player - {@link Player}
     */
    void open(@NotNull Player player, int size);

    /**
     * Get the {@link MenuItem} from the slot
     *
     * @param slot - index in the inventory where the item is set
     * @return {@link Optional<MenuItem>}
     */
    @NotNull Optional<MenuItem> getItem(int slot);

    /**
     * Get the current display being outputted
     *
     * @return {@link MenuDisplay}
     */
    @NotNull MenuDisplay getDisplay();
}
