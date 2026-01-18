package me.tuanang.api;

import org.bukkit.plugin.java.JavaPlugin;

public class TuanAngApi extends JavaPlugin {

    private ApiServer server;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port", 25672);
        String key = getConfig().getString("key", "tuanang123");

        server = new ApiServer(this, port, key);
        server.start();

        getLogger().info("API chạy tại port " + port);
    }

    @Override
    public void onDisable() {
        if (server != null) server.stop();
    }
}
