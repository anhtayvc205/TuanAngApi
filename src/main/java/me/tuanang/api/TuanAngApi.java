package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TuanAngApi extends JavaPlugin {

    private HttpServer httpServer;

    @Override
    public void onEnable() {
        int port = 25673;

        try {
            // ⚠️ BẮT BUỘC bind 0.0.0.0 cho hosting
            InetSocketAddress addr = new InetSocketAddress("0.0.0.0", port);
            httpServer = HttpServer.create(addr, 0);

            httpServer.createContext("/stats", new StatsHandler(this));

            // thread pool riêng (không đụng main thread MC)
            httpServer.setExecutor(Executors.newCachedThreadPool());
            httpServer.start();

            getLogger().info("§a[TuanAngApi] API started at 0.0.0.0:" + port);

        } catch (Exception e) {
            getLogger().severe("§c[TuanAngApi] FAILED to start API");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (httpServer != null) {
            httpServer.stop(0);
            getLogger().info("§c[TuanAngApi] API stopped");
        }
    }
}
