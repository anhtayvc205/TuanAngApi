package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String name = ex.getRequestURI().getPath().replace("/stats/", "");
        PlayerData d = PlayerCache.get(name);

        JSONObject j = new JSONObject();
        j.put("name", d.name);
        j.put("break", d.blockBreak);
        j.put("place", d.blockPlace);
        j.put("kill", d.kill);
        j.put("death", d.death);
        j.put("playtime", Utils.formatTime(d.playtime));
        j.put("lastSeen", d.online ? "Đang online" : Utils.lastSeen(d.lastSeen));

        byte[] out = j.toString().getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(200, out.length);
        ex.getResponseBody().write(out);
        ex.close();
    }
}
