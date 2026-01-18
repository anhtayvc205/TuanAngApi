package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    public static PlayerCache CACHE;

    @Override
    public void onEnable() {
        CACHE = new PlayerCache(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        // autosave 30s
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,
                () -> CACHE.saveAll(), 600L, 600L);

        // playtime realtime 10s
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            Bukkit.getOnlinePlayers().forEach(p ->
                    CACHE.addPlaytime(p.getUniqueId(), 10)
            );
        }, 200L, 200L);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();
            getLogger().info("API started :25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        CACHE.saveAll();
    }
}
