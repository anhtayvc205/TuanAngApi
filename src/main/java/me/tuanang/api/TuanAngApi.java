package me.tuanang.api;

import org.bukkit.plugin.java.JavaPlugin;

public class TuanAngApi extends JavaPlugin {

    private StatsHttpServer server;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");
        String key = getConfig().getString("key");

        server = new StatsHttpServer(port, key);
        server.start();

        getLogger().info("TuanAngApi started on port " + port);
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop();
        }
    }
}
