package me.shreyasayyengar.friendsextended.util;

import me.shreyasayyengar.friendsextended.FriendsPlugin;
import org.bukkit.ChatColor;

public class ConfigManager {

    private static FriendsPlugin main;

    public static void init(FriendsPlugin main) {
        ConfigManager.main = main;
        main.getConfig().options().configuration();
        main.saveDefaultConfig();
    }

    public static Object getSQL(String data) {
        return main.getConfig().get("mysql." + data);
    }

    public static String getSystemMessage(String label) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("system-messages." + label));
    }
}
