package me.tuanang.api;

import org.bukkit.plugin.java.JavaPlugin;

public class TuanAngApi extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");
        String key = getConfig().getString("key");
        String bind = getConfig().getString("bind");

        getLogger().info("API chạy port " + port);

        new StatsHandler(this, port, key, bind).start();
    }
}
