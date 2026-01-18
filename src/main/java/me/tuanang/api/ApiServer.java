package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class ApiServer {

    private HttpServer server;

    public ApiServer(TuanAngApi plugin, int port, String key) {
        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/stats", new StatsHandler(plugin, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
