package me.shreyasayyengar.friendsextended.util;

import me.shreyasayyengar.friendsextended.FriendsPlugin;

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
}
