package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerCache.init();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new AutoSaveTask(), 200L, 200L);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler());
            server.start();
            getLogger().info("API started :25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
