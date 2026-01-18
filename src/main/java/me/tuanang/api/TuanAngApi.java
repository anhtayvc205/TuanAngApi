package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    public static TuanAngApi instance;
    public PlayerCache cache;

    @Override
    public void onEnable() {
        instance = this;
        cache = new PlayerCache();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        startApi(25673);

        // autosave mỗi 10s
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,
                cache::saveAll, 200L, 200L);
    }

    private void startApi(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/stats", new StatsHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();

            getLogger().info("API started on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
