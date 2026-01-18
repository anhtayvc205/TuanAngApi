package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class StatsHttpServer {

    private HttpServer server;

    public StatsHttpServer(int port, String key) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/stats", new StatsHandler(key));
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
