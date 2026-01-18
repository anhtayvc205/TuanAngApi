package me.tuanang.api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    private final Plugin plugin;
    private final String key;

    public StatsHandler(Plugin plugin, String key) {
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
        OfflinePlayer p = Bukkit.getOfflinePlayer(name);

        if (!p.hasPlayedBefore()) {
            send(ex, 404, "{\"error\":\"player not found\"}");
            return;
        }

        UUID uuid = p.getUniqueId();
        File statsFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                "stats/" + uuid + ".json");

        if (!statsFile.exists()) {
            send(ex, 404, "{\"error\":\"no stats\"}");
            return;
        }

        JsonObject root = JsonParser.parseReader(new FileReader(statsFile)).getAsJsonObject();
        JsonObject stats = root.getAsJsonObject("stats");
        JsonObject custom = stats.getAsJsonObject("minecraft:custom");
        JsonObject mined = stats.getAsJsonObject("minecraft:mined");
        JsonObject used = stats.getAsJsonObject("minecraft:used");

        int playtime = custom.get("minecraft:play_time").getAsInt() / 20;
        int deaths = custom.get("minecraft:deaths").getAsInt();
        int mobKills = custom.get("minecraft:mob_kills").getAsInt();

        int blocksBroken = sum(mined);
        int blocksPlaced = sum(used);

        String json = """
        {
          "name": "%s",
          "uuid": "%s",
          "blocks_broken": %d,
          "blocks_placed": %d,
          "mob_kills": %d,
          "deaths": %d,
          "playtime": %d,
          "online": %s
        }
        """.formatted(
                p.getName(),
                uuid,
                blocksBroken,
                blocksPlaced,
                mobKills,
                deaths,
                playtime,
                p.isOnline()
        );

        send(ex, 200, json);
    }

    private int sum(JsonObject obj) {
        if (obj == null) return 0;
        int total = 0;
        for (var e : obj.entrySet()) total += e.getValue().getAsInt();
        return total;
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(code, data.length);
        ex.getResponseBody().write(data);
        ex.close();
    }
}
