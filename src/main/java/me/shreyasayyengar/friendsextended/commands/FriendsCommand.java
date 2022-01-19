package me.shreyasayyengar.friendsextended.commands;

import me.shreyasayyengar.friendsextended.menu.MenuUtils;
import me.shreyasayyengar.friendsextended.objects.FriendProfile;
import me.shreyasayyengar.friendsextended.util.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            FriendProfile profile = new FriendProfile(player.getUniqueId());


        } else sender.sendMessage(Utility.colourise("&4You cannot use this from the console!"));

        return false;
    }
}
