package me.tuanang.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;
    private final Gson gson = new Gson();

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) {
        try {
            URI uri = ex.getRequestURI();
            String[] parts = uri.getPath().split("/");
            if (parts.length < 3) {
                send(ex, error("missing player"));
                return;
            }

            String playerName = parts[2];
            String key = getQuery(uri.getQuery(), "key");

            if (!plugin.getConfig().getString("key").equals(key)) {
                send(ex, error("invalid key"));
                return;
            }

            OfflinePlayer off = Bukkit.getOfflinePlayer(playerName);
            Player online = off.getPlayer();

            Map<String, Object> data = new HashMap<>();
            data.put("player", playerName);
            data.put("uuid", off.getUniqueId().toString());

            if (online != null) {
                data.put("online", true);
                data.put("world", online.getWorld().getName());
            } else {
                data.put("online", false);
                data.put("lastSeen", off.getLastPlayed());
            }

            data.put("blocksPlaced", off.getStatistic(Statistic.USE_ITEM));
            data.put("blocksBroken", off.getStatistic(Statistic.MINE_BLOCK));

            send(ex, gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(HttpExchange ex, String json) throws Exception {
        ex.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }

    private String error(String msg) {
        return gson.toJson(Map.of("error", msg));
    }

    private String getQuery(String query, String key) {
        if (query == null) return null;
        for (String s : query.split("&")) {
            String[] p = s.split("=");
            if (p[0].equals(key)) return p.length > 1 ? p[1] : null;
        }
        return null;
    }
}
