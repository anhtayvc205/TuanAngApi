package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    private static TuanAngApi instance;

    public static TuanAngApi getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        AutoSaveTask.start();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler());
            server.start();
            getLogger().info("API started :25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        PlayerCache.saveAll();
    }
}
