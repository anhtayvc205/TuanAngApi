package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;
    private final String key;

    public StatsHandler(TuanAngApi plugin, String key) {
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        if (query == null || !query.contains("key=" + key)) {
            send(ex, 403, "{\"error\":\"invalid key\"}");
            return;
        }

        String[] parts = ex.getRequestURI().getPath().split("/");
        if (parts.length < 3) {
            send(ex, 400, "{\"error\":\"missing player\"}");
            return;
        }

        String name = parts[2];
        AtomicReference<String> json = new AtomicReference<>();

        try {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                Player p = Bukkit.getPlayerExact(name);

                if (p == null) {
                    json.set("{\"error\":\"player offline\"}");
                } else {
                    json.set("""
                    {
                      "name": "%s",
                      "uuid": "%s",
                      "blocks_broken": %d,
                      "blocks_placed": %d,
                      "mob_kills": %d,
                      "deaths": %d,
                      "playtime": %d
                    }
                    """.formatted(
                            p.getName(),
                            p.getUniqueId(),
                            p.getStatistic(Statistic.MINE_BLOCK),
                            p.getStatistic(Statistic.USE_ITEM),
                            p.getStatistic(Statistic.MOB_KILLS),
                            p.getStatistic(Statistic.DEATHS),
                            p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20
                    ));
                }
                return null;
            }).get();

        } catch (Exception e) {
            json.set("{\"error\":\"internal\"}");
        }

        send(ex, 200, json.get());
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(code, data.length);
        ex.getResponseBody().write(data);
        ex.close();
    }
}
