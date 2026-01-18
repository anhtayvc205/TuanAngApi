package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) {
        try {
            URI uri = ex.getRequestURI();
            String name = uri.getPath().replace("/stats/", "");

            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            UUID uuid = p.getUniqueId();

            var s = plugin.cache.get(uuid);

            boolean online = p.isOnline();

            String json = """
            {
              "name":"%s",
              "status":"%s",
              "break":%d,
              "place":%d,
              "kill":%d,
              "death":%d,
              "playtime":"%s",
              "lastSeen":%d
            }
            """.formatted(
                    name,
                    online ? "online" : "offline",
                    s.breakBlock,
                    s.placeBlock,
                    s.kill,
                    s.death,
                    formatTime(s.playtime),
                    s.lastSeen
            );

            ex.sendResponseHeaders(200, json.length());
            OutputStream os = ex.getResponseBody();
            os.write(json.getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTime(long ticks) {
        long sec = ticks / 20;
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return h + "h" + m + "p" + s + "s";
    }
}
