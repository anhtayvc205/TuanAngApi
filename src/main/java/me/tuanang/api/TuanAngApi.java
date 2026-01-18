package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");
        String bind = getConfig().getString("bind");

        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(bind, port), 0);

            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(null);
            server.start();

            getLogger().info("API started at " + bind + ":" + port);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
