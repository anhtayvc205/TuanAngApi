package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    private final JavaPlugin plugin;

    public StatsHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] args = path.split("/");

            if (args.length < 3) {
                send(exchange, "{\"error\":\"missing player\"}");
                return;
            }

            String name = args[2];
            OfflinePlayer off = Bukkit.getOfflinePlayer(name);
            UUID uuid = off.getUniqueId();

            boolean online = off.isOnline();
            Player p = online ? off.getPlayer() : null;

            JSONObject json = new JSONObject();
            json.put("player", name);
            json.put("uuid", uuid.toString());
            json.put("status", online ? "online" : "offline");

            json.put("playtime", off.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20);
            json.put("kills", off.getStatistic(Statistic.PLAYER_KILLS));
            json.put("deaths", off.getStatistic(Statistic.DEATHS));

            json.put("blocksPlaced", off.getStatistic(Statistic.USE_ITEM));
            json.put("blocksBroken", off.getStatistic(Statistic.MINE_BLOCK));

            long lastSeen = online
                    ? System.currentTimeMillis()
                    : off.getLastSeen();

            json.put("lastSeen", lastSeen);

            send(exchange, json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            try {
                send(exchange, "{\"error\":\"internal\"}");
            } catch (Exception ignored) {}
        }
    }

    private void send(HttpExchange ex, String data) throws Exception {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(200, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
