package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.JsonObject;

import java.io.OutputStream;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange e) {
        try {
            String name = e.getRequestURI().getPath().replace("/stats/", "");
            PlayerData d = plugin.cache.data.get(name.toLowerCase());

            JsonObject o = new JsonObject();
            if (d == null) {
                o.addProperty("error", "Người chơi chưa từng vào server");
            } else {
                o.addProperty("name", name);
                o.addProperty("break", d.blockBreak);
                o.addProperty("place", d.blockPlace);
                o.addProperty("kill", d.kill);
                o.addProperty("death", d.death);
                o.addProperty("playtime", Utils.time(d.playtime));
                o.addProperty("lastSeen", Utils.lastSeen(d.lastSeen));
            }

            byte[] res = o.toString().getBytes();
            e.sendResponseHeaders(200, res.length);
            OutputStream os = e.getResponseBody();
            os.write(res);
            os.close();
        } catch (Exception ignored) {}
    }
}
