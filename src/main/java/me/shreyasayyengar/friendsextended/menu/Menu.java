package me.shreyasayyengar.friendsextended.menu;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class Menu implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final Map<Integer, MenuItem> items;
    public Consumer<InventoryCloseEvent> onClose = event -> {
    };
    private Plugin plugin;

    public Menu(int size, @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));
        this.items = Maps.newHashMap();
    }

    public void register(@NotNull Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Map<Integer, MenuItem> getItems() {
        return items;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Menu) {
            final Menu menu = (Menu) holder;
            final int slot = event.getSlot();
            if (menu.getItems().containsKey(slot)) {
                event.setCancelled(true);
                menu.getItems().get(slot).getMenuClick().onClick(((Player) event.getWhoClicked()), inventory.getItem(slot), slot);
            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if ((inventory.getHolder() instanceof Menu)) {
            if (onClose != null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> ((Menu) inventory.getHolder()).onClose.accept(event), 20L);
            }
        }
    }

}
