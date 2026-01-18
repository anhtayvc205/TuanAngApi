package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    public PlayerCache cache;

    @Override
    public void onEnable() {
        cache = new PlayerCache(this);

        // Auto refresh mỗi 10s
        getServer().getScheduler().runTaskTimer(
                this,
                () -> cache.refreshOnlinePlayers(),
                200L, 200L
        );

        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress("0.0.0.0", 25673), 0
            );
            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();

            getLogger().info("API chạy tại :25673");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
