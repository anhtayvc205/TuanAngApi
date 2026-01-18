package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.sql.*;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) {
        try {
            String name = ex.getRequestURI().getPath().replace("/stats/", "").toLowerCase();

            var cache = plugin.cache.data.get(name);

            if (cache == null) {
                ex.sendResponseHeaders(200, 0);
                ex.getResponseBody().write("""
                {"error":"Người chơi chưa từng vào server"}
                """.getBytes());
                ex.close();
                return;
            }

            String json = """
            {
              "name":"%s",
              "break":%d,
              "place":%d,
              "kill":%d,
              "death":%d,
              "playtime":%d,
              "lastSeen":%d
            }
            """.formatted(
                    name,
                    cache.breakBlock,
                    cache.placeBlock,
                    cache.kill,
                    cache.death,
                    cache.playtime,
                    cache.lastSeen
            );

            ex.getResponseHeaders().add("Content-Type","application/json");
            ex.sendResponseHeaders(200, json.length());
            OutputStream os = ex.getResponseBody();
            os.write(json.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
