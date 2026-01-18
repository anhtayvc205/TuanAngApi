package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public final class TuanAngApi extends JavaPlugin {

    private HttpServer server;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");
        String key = getConfig().getString("key");

        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/stats", new StatsHandler(this, key));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();

            getLogger().info("API chạy tại port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (server != null) server.stop(0);
    }
}
