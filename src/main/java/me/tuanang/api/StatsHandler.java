package me.tuanang.api;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class StatsHandler {

    private final JavaPlugin plugin;
    private final int port;
    private final String key;
    private final String bind;

    public StatsHandler(JavaPlugin plugin, int port, String key, String bind) {
        this.plugin = plugin;
        this.port = port;
        this.key = key;
        this.bind = bind;
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(bind, port), 0
            );

            server.createContext("/stats", exchange -> {
                String query = exchange.getRequestURI().getQuery();
                if (query == null || !query.contains("key=")) {
                    send(exchange, "{\"error\":\"missing key\"}");
                    return;
                }

                if (!query.contains("key=" + key)) {
                    send(exchange, "{\"error\":\"invalid key\"}");
                    return;
                }

                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length < 3) {
                    send(exchange, "{\"error\":\"missing player\"}");
                    return;
                }

                String name = path[2];

                Bukkit.getScheduler().runTask(plugin, () -> {
                    OfflinePlayer off = Bukkit.getOfflinePlayer(name);
                    JSONObject json = new JSONObject();

                    json.put("player", name);
                    json.put("uuid", off.getUniqueId().toString());

                    Player online = off.getPlayer();
                    boolean isOnline = online != null;

                    json.put("online", isOnline);

                    if (isOnline) {
                        json.put("health", online.getHealth());
                        json.put("food", online.getFoodLevel());
                        json.put("world", online.getWorld().getName());
                    } else {
                        json.put("health", null);
                        json.put("food", null);
                        json.put("world", null);
                    }

                    json.put("playtime", off.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20);
                    json.put("deaths", off.getStatistic(Statistic.DEATHS));
                    json.put("kills", off.getStatistic(Statistic.PLAYER_KILLS));

                    send(exchange, json.toString(2));
                });
            });

            server.start();
            plugin.getLogger().info("API OK http://IP:" + port + "/stats/<player>?key=xxx");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(com.sun.net.httpserver.HttpExchange ex, String body) {
        try {
            byte[] data = body.getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(200, data.length);
            ex.getResponseBody().write(data);
            ex.close();
        } catch (Exception ignored) {}
    }
}
