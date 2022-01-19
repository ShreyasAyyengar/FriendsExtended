package me.shreyasayyengar.friendsextended.objects;

import me.shreyasayyengar.friendsextended.FriendsPlugin;
import me.shreyasayyengar.friendsextended.exceptions.NoFriendsException;
import me.shreyasayyengar.friendsextended.menu.MenuBuilder;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendProfile {

    private final UUID profileHolder;

    public FriendProfile(UUID profileHolder) {
        this.profileHolder = profileHolder;
    }

    public UUID getProfileHolder() {
        return profileHolder;
    }

    // <Friends Manager> ------------------------------

    public List<UUID> getFriends() throws SQLException {
        return Stream.of(getData().getString("friends").split("\\.")).map(UUID::fromString).collect(Collectors.toList());
    }

    public void addFriend(UUID toAdd) throws SQLException {
        String friends = getData().getString("friends") + "." + toAdd.toString();
        FriendsPlugin.getInstance().getDatabase().preparedStatement(
                "update friends_info set friends = '" + friends + "' where uuid = '" + profileHolder + "';").executeUpdate();
    }

    public void removeFriend(UUID toRemove) throws SQLException {
        String friends = getData().getString("friends").replace("." + toRemove.toString(), "");
        FriendsPlugin.getInstance().getDatabase().preparedStatement(
                "update friends_info set friends = '" + friends + "' where uuid = '" + profileHolder + "';"
        ).executeUpdate();
    }

    // <Filtered Friends> -----------------------------

    public List<UUID> getOfflineFriends() throws SQLException {
        return getFriends().stream()
                .filter(uuid -> !Bukkit.getPlayer(uuid).isOnline())
                .collect(Collectors.toList());
    }

    public List<UUID> getOnlineFriends() throws SQLException {
        return getFriends().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid).isOnline())
                .collect(Collectors.toList());
    }

    // <Friend Requests> -------------------------------

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
                "update friends_info set requests = '" + removed + "' where uuid '" + profileHolder + "';"
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

    public boolean isAcceptingRequests() throws SQLException {
        return getData().getBoolean("is_accepting");
    }

    // <Friend Misc Methods> ---------------------------

    public boolean isFriend(UUID uuid) throws SQLException {
        return getFriends().contains(uuid);
    }

    public void openInventory() {

    }

    // <Friend Data Methods> ---------------------------
    private ResultSet getData() throws SQLException {
        ResultSet resultSet = FriendsPlugin.getInstance().getDatabase().preparedStatement("select * from friends_info where uuid = '" + profileHolder + "'").executeQuery();
        resultSet.next();
        return resultSet;
    }

    public static void createData(UUID uuid) {
        try {
            ResultSet resultSet = FriendsPlugin.getInstance().getDatabase().preparedStatement("select count(uuid) from friends_info where uuid = '" + uuid + "';").executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                FriendsPlugin.getInstance().getDatabase().preparedStatement(
                        "insert into friends_info (uuid, friends, is_accepting, requests, is_receiving_messages, jumping, show_offline) " +
                                "values ('" + uuid + "', default, default, default, default, default, default);"
                ).executeUpdate();
            }
        } catch (Exception x) {
            Bukkit.getLogger().severe("Failed to create data for UUID-" + uuid + " | Please contact for support");
        }
    }

}
