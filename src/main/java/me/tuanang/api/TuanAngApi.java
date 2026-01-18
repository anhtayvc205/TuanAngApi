package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class TuanAngApi extends JavaPlugin {

    public PlayerCache cache;
    public StatsHandler api;

    @Override
    public void onEnable() {
        cache = new PlayerCache();

        Utils.load(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        new AutoSaveTask(this).runTaskTimerAsynchronously(this, 200L, 200L);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            api = new StatsHandler(this);
            server.createContext("/stats", api);
            server.start();
            getLogger().info("API started :25673");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Utils.save(this);
    }
}
