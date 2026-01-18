package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) {
        try {
            String[] p = ex.getRequestURI().getPath().split("/");
            if (p.length < 3) {
                send(ex, "{\"error\":\"missing_player\"}");
                return;
            }

            OfflinePlayer pl = Bukkit.getOfflinePlayer(p[2]);
            PlayerData d = plugin.db.get(pl.getUniqueId());

            boolean online = pl.isOnline();

            String json = "{\n" +
                    " \"player\":\"" + p[2] + "\",\n" +
                    " \"status\":\"" + (online ? "online" : "offline") + "\",\n" +
                    " \"playtime\":" + d.playtime + ",\n" +
                    " \"blocks_place\":" + d.place + ",\n" +
                    " \"blocks_break\":" + d.breaks + ",\n" +
                    " \"lastSeen\":" + d.lastSeen + "\n" +
                    "}";

            send(ex, json);

        } catch (Exception e) {
            try { send(ex, "{\"error\":\"internal\"}"); } catch (Exception ignored) {}
        }
    }

    private void send(HttpExchange ex, String body) throws Exception {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(200, data.length);
        OutputStream os = ex.getResponseBody();
        os.write(data);
        os.close();
    }
}
