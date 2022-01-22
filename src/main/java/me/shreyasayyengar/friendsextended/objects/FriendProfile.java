package me.shreyasayyengar.friendsextended.objects;

import me.shreyasayyengar.friendsextended.FriendsPlugin;
import me.shreyasayyengar.friendsextended.exceptions.NoFriendsException;
import me.shreyasayyengar.friendsextended.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendProfile {

    private final UUID profileHolder;

    public FriendProfile(UUID profileHolder) {
        this.profileHolder = profileHolder;
    }

    public static void createData(UUID uuid) {
        try {
            ResultSet resultSet = FriendsPlugin.getInstance().getDatabase().preparedStatement("select count(uuid) from friends_info where uuid = '" + uuid + "';").executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                FriendsPlugin.getInstance().getDatabase().preparedStatement(
                        "insert into friends_info (uuid, friends, is_accepting, requests, is_receiving_messages, jumping, show_offline, status, friends_since) " +
                                "values ('" + uuid + "', default, default, default, default, default, default, default, default);"
                ).executeUpdate();
            }
        } catch (Exception x) {
            Bukkit.getLogger().severe("Failed to create data for UUID-" + uuid + " | Please contact for support");
        }
    }

    public static String getFriendsSince(UUID friend, UUID toGet) throws SQLException {
        String[] split = new FriendProfile(friend).getFriendsSince().split(",");

        for (String s : split) {
            if (s.contains(toGet.toString())) {
                String cutUUID = s.substring(37);
                return cutUUID.substring(0, cutUUID.length() - 7).replace(" ", " | ");
            }
        }

        return null;
    }

    // <Friends Manager> ------------------------------

    public UUID getProfileHolder() {
        return profileHolder;
    }

    public List<UUID> getFriends() throws SQLException {
        return Stream.of(getData().getString("friends").split("\\.")).map(UUID::fromString).collect(Collectors.toList());
    }

    public void addFriend(UUID toAdd) throws SQLException {

        if (getData().getString("friends") == null) {
            FriendsPlugin.getInstance().getDatabase().preparedStatement(
                    "update friends_info set friends = '" + toAdd + "' where uuid = '" + profileHolder + "';")
                    .executeUpdate();
        } else {

            String friends = getData().getString("friends") + "." + toAdd.toString();
            FriendsPlugin.getInstance().getDatabase().preparedStatement(
                    "update friends_info set friends = '" + friends + "' where uuid = '" + profileHolder + "';"
            ).executeUpdate();
        }

        if (getData().getString("friends_since") == null) {
            String since = toAdd + ":" + Utility.formatDateTime();
            FriendsPlugin.getInstance().getDatabase().preparedStatement(
                    "update friends_info set friends_since = '" + since + "' where uuid = '" + profileHolder + "';").executeUpdate();
        } else {
            String friendsSince = getData().getString("friends_since") + "," + toAdd + ":" + Utility.formatDateTime();
            FriendsPlugin.getInstance().getDatabase().preparedStatement(
                    "update friends_info set friends_since = '" + friendsSince + "' where uuid = '" + profileHolder + "';").executeUpdate();
        }
    }

    // <Filtered Friends> -----------------------------

    public void removeFriend(UUID toRemove) throws SQLException {
        String friends = getData().getString("friends").replace("." + toRemove.toString(), "");
        FriendsPlugin.getInstance().getDatabase().preparedStatement(
                "update friends_info set friends = '" + friends + "' where uuid = '" + profileHolder + "';"
        ).executeUpdate();
    }

    public List<UUID> getOfflineFriends() throws SQLException {
        return getFriends().stream()
                .filter(uuid -> !Bukkit.getPlayer(uuid).isOnline())
                .collect(Collectors.toList());
    }

    // <Friend Requests> -------------------------------

    public List<UUID> getOnlineFriends() throws SQLException {
        return getFriends().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid).isOnline())
                .collect(Collectors.toList());
    }

    public List<UUID> getFriendRequests() throws SQLException, NoFriendsException {

        if (getData().getString("requests").isEmpty() || getData().getString("requests") == null) {
            throw new NoFriendsException("");
        }

        return Stream.of(getData().getString("requests").split("\\.")).map(UUID::fromString).collect(Collectors.toList());
    }

    public void addFriendRequest(UUID uuid) throws SQLException {
        String updated = getData().getString("requests") + "." + uuid.toString();

        FriendsPlugin.getInstance().getDatabase().preparedStatement(
                "update friends_info set requests = '" + updated + "' where uuid = '" + profileHolder + "';"
        ).executeUpdate();
    }

    public void removeFriendRequest(UUID uuid) throws SQLException {
        String removed = getData().getString("requests").replace("." + uuid.toString(), "");

        FriendsPlugin.getInstance().getDatabase().preparedStatement(
                "update friends_info set requests = '" + removed + "' where uuid ='" + profileHolder + "';"
        ).executeUpdate();

    }

    public FriendRequestStatus checkValidFriendRequest(UUID requester) throws SQLException {
        if (!isAcceptingRequests()) {
            return FriendRequestStatus.DISABLED_FRIEND_REQUEST;
        }

        if (isFriend(requester)) {
            return FriendRequestStatus.ALREADY_FRIENDS;
        }

        return FriendRequestStatus.APPROVED;
    }

    // <Friend Misc Methods> ---------------------------

    public boolean isAcceptingRequests() throws SQLException {
        return getData().getBoolean("is_accepting");
    }

    public boolean isFriend(UUID uuid) throws SQLException {
        return getFriends().contains(uuid);
    }

    public void openInventory() {

    }

    public String getStatus() throws SQLException {
        return getData().getString("status");
    }

    public void setStatus(String status) throws SQLException {
        FriendsPlugin.getInstance().getDatabase().preparedStatementBuilder(
                        "update friends_info set status = ? where uuid = '" + profileHolder + "';")
                .setString(status)
                .executeUpdate();
    }

    public String getFriendsSince() throws SQLException {
        return getData().getString("friends_since");
    }

    // <Friend Data Methods> ---------------------------
    private ResultSet getData() throws SQLException {
        ResultSet resultSet = FriendsPlugin.getInstance().getDatabase().preparedStatement("select * from friends_info where uuid = '" + profileHolder + "'").executeQuery();
        resultSet.next();
        return resultSet;
    }

    // <Friend ItemStacks and Inventory Methods> -------
    public List<ItemStack> getProfiles() throws SQLException {

        List<ItemStack> profiles = new ArrayList<>();
        for (UUID uuid : getFriends()) {

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta itemMeta = (SkullMeta) stack.getItemMeta();
            itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            itemMeta.setLocalizedName("friendprofile." + uuid);

            List<String> lore = new ArrayList<>();

            if (player.isOnline()) {
                itemMeta.setDisplayName(Utility.colourise("&a" + Bukkit.getOfflinePlayer(uuid).getName() + " (Online)"));
                lore.add(Utility.colourise(new FriendProfile(uuid).getStatus()));
                lore.add(Utility.colourise("&7World: " + ((Player) player).getWorld().getName()));
            } else {
                itemMeta.setDisplayName(Utility.colourise("&7" + Bukkit.getOfflinePlayer(uuid).getName() + " (Offline)"));
            }

            lore.add(Utility.colourise("&7Friends since: " + FriendProfile.getFriendsSince(this.getProfileHolder(), uuid)));

            itemMeta.setLore(lore);
            stack.setItemMeta(itemMeta);
            profiles.add(stack);
        }

        return profiles;
    }

}