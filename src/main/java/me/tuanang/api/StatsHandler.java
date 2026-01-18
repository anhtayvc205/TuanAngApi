package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) {
        try {
            String path = ex.getRequestURI().getPath(); 
            // /stats/hoang09s
            String[] split = path.split("/");
            if (split.length < 3) {
                send(ex, "{\"error\":\"missing player\"}");
                return;
            }

            String name = split[2];
            OfflinePlayer off = Bukkit.getOfflinePlayer(name);
            UUID uuid = off.getUniqueId();

            boolean online = off.isOnline();
            Player p = online ? off.getPlayer() : null;

            JSONObject json = new JSONObject();
            json.put("player", name);
            json.put("uuid", uuid.toString());
            json.put("status", online ? "online" : "offline");

            json.put("kills", off.getStatistic(Statistic.PLAYER_KILLS));
            json.put("deaths", off.getStatistic(Statistic.DEATHS));
            json.put("playtime", off.getStatistic(Statistic.PLAY_ONE_MINUTE));

            json.put("blocks_placed", BlockListener.placed.getOrDefault(uuid, 0));
            json.put("blocks_broken", BlockListener.broken.getOrDefault(uuid, 0));

            json.put("last_seen", off.getLastSeen());
            json.put("world", online ? p.getWorld().getName() : "offline");

            send(ex, json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            try {
                send(ex, "{\"error\":\"internal\"}");
            } catch (Exception ignored) {}
        }
    }

    private void send(HttpExchange ex, String data) throws Exception {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(200, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
