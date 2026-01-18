package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    public static TuanAngApi instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        // auto +10s playtime
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            PlayerCache.cache.values().forEach(PlayerCache::tick);
        }, 200L, 200L); // 10s

        // HTTP API
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler());
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            getLogger().info("API started :25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
