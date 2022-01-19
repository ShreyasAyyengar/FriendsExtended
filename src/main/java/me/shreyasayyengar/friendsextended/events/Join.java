package me.shreyasayyengar.friendsextended.events;

import me.shreyasayyengar.friendsextended.objects.FriendProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FriendProfile.createData(event.getPlayer().getUniqueId());
    }
}
