package me.tuanang.api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatsHandler implements HttpHandler {

    private final String apiKey;

    public StatsHandler(String key) {
        this.apiKey = key;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        Map<String, String> q = queryToMap(ex.getRequestURI().getQuery());

        if (!apiKey.equals(q.get("key"))) {
            ex.sendResponseHeaders(403, 0);
            ex.close();
            return;
        }

        String name = q.get("name");
        Player p = Bukkit.getPlayerExact(name);

        JsonObject json = new JsonObject();

        if (p == null) {
            json.addProperty("error", "player offline");
        } else {
            json.addProperty("name", p.getName());
            json.addProperty("uuid", p.getUniqueId().toString());
            json.addProperty("online", true);

            json.addProperty("blocks_mined", p.getStatistic(Statistic.MINE_BLOCK));
            json.addProperty("blocks_placed", p.getStatistic(Statistic.USE_ITEM));
            json.addProperty("mob_kills", p.getStatistic(Statistic.MOB_KILLS));
            json.addProperty("deaths", p.getStatistic(Statistic.DEATHS));

            long ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
            json.addProperty("playtime", ticks / 20);
        }

        byte[] res = json.toString().getBytes();
        ex.sendResponseHeaders(200, res.length);
        ex.getResponseBody().write(res);
        ex.close();
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        for (String s : query.split("&")) {
            String[] p = s.split("=");
            if (p.length == 2) map.put(p[0], p[1]);
        }
        return map;
    }
}
