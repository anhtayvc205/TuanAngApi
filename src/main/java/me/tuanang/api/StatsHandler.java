package me.tuanang.api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) {
        try {
            String[] split = ex.getRequestURI().getPath().split("/");
            if (split.length < 3) {
                send(ex, "{\"error\":\"no player\"}");
                return;
            }

            String name = split[2].toLowerCase();

            if (!TuanAngApi.instance.cache.exists(name)) {
                send(ex, "{\"error\":\"Người chơi chưa từng vào server\"}");
                return;
            }

            var d = TuanAngApi.instance.cache.get(name);

            boolean online = Bukkit.getPlayerExact(name) != null;
            long now = System.currentTimeMillis();
            long diff = (now - d.lastSeen) / 1000;

            JsonObject j = new JsonObject();
            j.addProperty("name", name);
            j.addProperty("break", d.breakBlock);
            j.addProperty("place", d.placeBlock);
            j.addProperty("mobkill", d.mobKill);
            j.addProperty("death", d.death);
            j.addProperty("playtime", d.playtime);
            j.addProperty("lastSeen",
                    online ? "đang online" : diff + " giây trước");

            send(ex, j.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(HttpExchange ex, String body) throws Exception {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(200, data.length);
        OutputStream os = ex.getResponseBody();
        os.write(data);
        os.close();
    }
}
