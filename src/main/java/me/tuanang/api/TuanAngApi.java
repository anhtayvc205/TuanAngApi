package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    @Override
    public void onEnable() {
        int port = 25673; // nhớ mở port này trong HyperCore

        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress("127.0.0.1", port), 0
            );

            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();

            getLogger().info("API started on port " + port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
