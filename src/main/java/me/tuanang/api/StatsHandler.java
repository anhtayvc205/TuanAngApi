package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) {
        try {
            String path = ex.getRequestURI().getPath();
            String[] split = path.split("/");
            if (split.length < 3) {
                send(ex, "{\"error\":\"missing player\"}");
                return;
            }

            String name = split[2];
            OfflinePlayer off = Bukkit.getOfflinePlayer(name);
            if (!off.hasPlayedBefore() && !off.isOnline()) {
                send(ex, "{\"error\":\"player not found\"}");
                return;
            }

            UUID uuid = off.getUniqueId();
            boolean online = off.isOnline();

            JSONObject json = new JSONObject();
            json.put("player", name);
            json.put("uuid", uuid.toString());
            json.put("online", online);

            if (online) {
                Player p = off.getPlayer();
                json.put("health", p.getHealth());
                json.put("food", p.getFoodLevel());
            } else {
                json.put("last_seen", off.getLastPlayed() / 1000);
            }

            // ====== READ STATS FILE (online + offline đều dùng) ======
            File f = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                    "stats/" + uuid + ".json");

            if (f.exists()) {
                String content = Files.readString(f.toPath());
                JSONObject stats = new JSONObject(content).getJSONObject("stats");

                json.put("playtime",
                        stats.getJSONObject("minecraft:custom")
                                .optInt("minecraft:play_time", 0));

                json.put("deaths",
                        stats.getJSONObject("minecraft:custom")
                                .optInt("minecraft:deaths", 0));

                json.put("blocks_mined",
                        stats.getJSONObject("minecraft:mined").length());

                json.put("blocks_placed",
                        stats.getJSONObject("minecraft:used").length());
            }

            send(ex, json.toString(2));

        } catch (Exception e) {
            try {
                send(ex, "{\"error\":\"" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    private void send(HttpExchange ex, String body) throws Exception {
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(200, body.getBytes().length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
                                    }
