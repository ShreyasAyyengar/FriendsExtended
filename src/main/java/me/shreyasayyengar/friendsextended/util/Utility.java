package me.shreyasayyengar.friendsextended.util;

import me.shreyasayyengar.friendsextended.FriendsPlugin;
import me.shreyasayyengar.friendsextended.objects.FriendProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Utility {

    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean hasEntry(UUID uuid) {
        try {
            ResultSet resultSet = FriendsPlugin.getInstance().getDatabase().preparedStatement("select count(uuid) from friends_info where uuid = '" + uuid + "';").executeQuery();
            resultSet.next();
            return resultSet.getInt(1) != 0;
        } catch (Exception x) {
            Bukkit.getLogger().severe("Failed to create data for UUID-" + uuid + " | Please contact for support");
        }

        return false;
    }


    public static String formatDateTime() {
        String s = Timestamp.valueOf(LocalDateTime.now()).toString();
        s.substring(0, s.length() - 7).replace(" ", " | ");

        return s;
    }
}
