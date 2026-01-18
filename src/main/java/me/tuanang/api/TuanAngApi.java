package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    @Override
    public void onEnable() {
        int port = 25673;

        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(port), 0
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
