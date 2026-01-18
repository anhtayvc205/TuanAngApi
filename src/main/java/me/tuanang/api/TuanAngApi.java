package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    public Database db;
    public PlayerCache cache;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        db = new Database(this);
        cache = new PlayerCache();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        startApi();

        // autosave 10s
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,
                () -> db.saveAll(cache),
                200L, 200L);

        getLogger().info("TuanAngApi enabled");
    }

    private void startApi() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
