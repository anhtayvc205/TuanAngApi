package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class TuanAngApi extends JavaPlugin {

    public Database db;
    public HashMap<UUID, Long> joinTime = new HashMap<>();

    @Override
    public void onEnable() {

        db = new Database();
        db.connect();

        getServer().getPluginManager().registerEvents(
                new PlayerListener(this), this
        );

        int port = 25673;

        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress("0.0.0.0", port), 0
            );

            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();

            getLogger().info("API started on 0.0.0.0:" + port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
