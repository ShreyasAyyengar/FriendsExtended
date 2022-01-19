package me.shreyasayyengar.friendsextended;


import me.shreyasayyengar.friendsextended.objects.database.MySQL;
import me.shreyasayyengar.friendsextended.util.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

// https://imgur.com/a/vzq5lSQ

public final class FriendsPlugin extends JavaPlugin {

    private MySQL database;

    public static FriendsPlugin getInstance() {
        return JavaPlugin.getPlugin(FriendsPlugin.class);
    }

    @Override
    public void onEnable() {

        registerCommands();
        registerEvents();
        try {
            initMySQL();
        } catch (SQLException e) {
            getLogger().severe("There was a problem creating MySQL tables");
            e.printStackTrace();
        }
        ConfigManager.init(this);
    }

    private void initMySQL() throws SQLException {
        database = new MySQL(
                (String) ConfigManager.getSQL("username"),
                (String) ConfigManager.getSQL("password"),
                (String) ConfigManager.getSQL("database"),
                (String) ConfigManager.getSQL("host"),
                (int) ConfigManager.getSQL("port"));

        database.preparedStatement("create table friends_info(" +
                "    uuid                  varchar(36)           null," +
                "    friends               longtext              null," +
                "    is_accepting          boolean default true  null," +
                "    requests              longtext              null," +
                "    is_receiving_messages boolean default true  null," +
                "    jumping               boolean default false null," +
                "    show_offline          boolean default false  null" +
                ");").executeUpdate();
    }

    private void registerEvents() {

    }

    private void registerCommands() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MySQL getDatabase() {
        return database;
    }
}
