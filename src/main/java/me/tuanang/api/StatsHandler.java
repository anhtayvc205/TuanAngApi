package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    private final JavaPlugin plugin;

    public StatsHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        String path = ex.getRequestURI().getPath();

        String key = plugin.getConfig().getString("key");

        if (query == null || !query.contains("key=" + key)) {
            send(ex, 403, "{\"error\":\"invalid key\"}");
            return;
        }

        String name = path.replace("/stats/", "");
        Player p = Bukkit.getPlayerExact(name);

        if (p == null) {
            send(ex, 404, "{\"error\":\"player offline\"}");
            return;
        }

        long ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long seconds = ticks / 20;

        int mined = p.getStatistic(Statistic.MINE_BLOCK);
        int placed = p.getStatistic(Statistic.PLACE_BLOCK);
        int kills = p.getStatistic(Statistic.MOB_KILLS);
        int deaths = p.getStatistic(Statistic.DEATHS);

        String json =
        "{"
        + "\"player\":\"" + p.getName() + "\","
        + "\"uuid\":\"" + p.getUniqueId() + "\","
        + "\"online\":true,"
        + "\"playtime\":{"
            + "\"ticks\":" + ticks + ","
            + "\"seconds\":" + seconds + ","
            + "\"time\":\"" + Util.formatTime(seconds) + "\""
        + "},"
        + "\"stats\":{"
            + "\"blocks_mined\":" + mined + ","
            + "\"blocks_placed\":" + placed + ","
            + "\"mob_kills\":" + kills + ","
            + "\"deaths\":" + deaths
        + "}"
        + "}";

        send(ex, 200, json);
    }

    private void send(HttpExchange ex, int code, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, data.length);
        ex.getResponseBody().write(data);
        ex.close();
    }
}
