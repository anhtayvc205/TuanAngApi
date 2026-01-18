package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    private final Plugin plugin;

    public StatsHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String path = exchange.getRequestURI().getPath();
                String[] split = path.split("/");

                if (split.length < 3) {
                    send(exchange, "{\"error\":\"missing player\"}");
                    return;
                }

                String name = split[2];
                OfflinePlayer p = Bukkit.getOfflinePlayer(name);

                if (!p.hasPlayedBefore() && !p.isOnline()) {
                    send(exchange, "{\"error\":\"player not found\"}");
                    return;
                }

                boolean online = p.isOnline();
                Player player = online ? p.getPlayer() : null;

                long lastSeen = p.getLastPlayed();
                long playtime = p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
                int deaths = p.getStatistic(Statistic.DEATHS);
                int kills = p.getStatistic(Statistic.PLAYER_KILLS);

                String world = online ? player.getWorld().getName() : "offline";

                String json = "{\n" +
                        " \"player\": \"" + p.getName() + "\",\n" +
                        " \"uuid\": \"" + p.getUniqueId() + "\",\n" +
                        " \"online\": " + online + ",\n" +
                        " \"world\": \"" + world + "\",\n" +
                        " \"playtime\": " + playtime + ",\n" +
                        " \"kills\": " + kills + ",\n" +
                        " \"deaths\": " + deaths + ",\n" +
                        " \"lastSeen\": " + lastSeen + "\n" +
                        "}";

                send(exchange, json);

            } catch (Exception e) {
                try {
                    send(exchange, "{\"error\":\"internal error\"}");
                } catch (Exception ignored) {}
            }
        });
    }

    private void send(HttpExchange ex, String data) throws Exception {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(200, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
