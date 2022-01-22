package me.shreyasayyengar.friendsextended.menu;

import me.shreyasayyengar.friendsextended.FriendsPlugin;
import me.shreyasayyengar.friendsextended.menu.interfaces.Menu;
import me.shreyasayyengar.friendsextended.menu.interfaces.MenuDisplay;
import me.shreyasayyengar.friendsextended.objects.FriendProfile;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MenuUtils {

    public static void openMenu(FriendProfile profile) throws SQLException {

        MenuDisplay.DisplayBuilder menu = MenuDisplay.create("&aFriends Menu!");
        List<ItemStack> itemStacks = profile.getProfiles();
        int size = itemStacks.size();

        for (int i = 0; i < size; i++) {

            menu.set(i, itemStacks.get(i), (player, itemStack, slot) -> {
                if (itemStack.itemStack().getItemMeta().getLocalizedName().startsWith("friendprofile")) {
                    openOptionsMenu(UUID.fromString(itemStack.itemStack().getItemMeta().getLocalizedName().split("\\.")[1]));
                }
            });

        }

        FriendsPlugin.getInstance().getMenuManager().openMenu(Menu.create(menu.build()), Bukkit.getPlayer(profile.getProfileHolder()), 54);
    }

    public static void openOptionsMenu(UUID uuid) {
        System.out.println("yes" + uuid);
    }
}
