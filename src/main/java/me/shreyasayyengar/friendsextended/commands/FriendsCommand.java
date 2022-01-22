package me.shreyasayyengar.friendsextended.commands;

import me.shreyasayyengar.friendsextended.menu.MenuUtils;
import me.shreyasayyengar.friendsextended.objects.FriendProfile;
import me.shreyasayyengar.friendsextended.util.ConfigManager;
import me.shreyasayyengar.friendsextended.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class FriendsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    FriendProfile targetProfile;

                    if (!Utility.hasEntry(target.getUniqueId())) {
                        player.sendMessage(Utility.colourise("&cThat player has never joined the server!"));
                        return false;
                    }

                    targetProfile = new FriendProfile(target.getUniqueId());

                    try {
                        switch (targetProfile.checkValidFriendRequest(player.getUniqueId())) {

                            case DISABLED_FRIEND_REQUEST -> player.sendMessage(ConfigManager.getSystemMessage("friend-requests-disabled"));
                            case ALREADY_FRIENDS -> player.sendMessage(ConfigManager.getSystemMessage("already-friends"));
                            case APPROVED -> {
                                targetProfile.addFriendRequest(player.getUniqueId());
                                player.sendMessage(ConfigManager.getSystemMessage("request-sent"));
                            }

                        }
                    } catch (SQLException x) {
                        x.printStackTrace();
                    }
                }
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("forceadd")) {
                try {
                    new FriendProfile(player.getUniqueId()).addFriend(UUID.randomUUID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                MenuUtils.openMenu(new FriendProfile(player.getUniqueId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else sender.sendMessage(Utility.colourise("&4You cannot use this from the console!"));

        return false;
    }
}
