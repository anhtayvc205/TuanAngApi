package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange ex) {
        try {
            String name = ex.getRequestURI().getPath().replace("/stats/", "");
            PlayerData d = PlayerCache.get(name);

            JSONObject json = new JSONObject();
            json.put("name", d.name);
            json.put("break", d.breakBlock);
            json.put("place", d.placeBlock);
            json.put("kill", d.kill);
            json.put("death", d.death);
            json.put("playtime", Utils.formatTime(d.playtime));
            json.put("lastSeen", Utils.lastSeen(d));

            byte[] resp = json.toString().getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            ex.sendResponseHeaders(200, resp.length);
            OutputStream os = ex.getResponseBody();
            os.write(resp);
            os.close();
        } catch (Exception ignored) {}
    }
}
