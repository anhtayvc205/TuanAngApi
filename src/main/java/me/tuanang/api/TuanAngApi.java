package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    public StatsCache cache;

    @Override
    public void onEnable() {
        cache = new StatsCache(this);
        cache.load();

        // Listener
        getServer().getPluginManager().registerEvents(
                new PlayerListener(this), this
        );

        // Auto save mỗi 10 giây
        new AutoSaveTask(this).runTaskTimerAsynchronously(this, 200, 200);

        // HTTP API
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            getLogger().info("API running on 0.0.0.0:25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        cache.save();
    }
}
