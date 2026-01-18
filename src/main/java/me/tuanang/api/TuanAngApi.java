package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    public static TuanAngApi instance;

    @Override
    public void onEnable() {
        instance = this;

        int port = 25673;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/stats", new StatsHandler());
            server.setExecutor(null);
            server.start();

            getLogger().info("API started on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }
}
