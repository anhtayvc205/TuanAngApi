package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange ex) {
        try {
            String path = ex.getRequestURI().getPath();
            String name = path.replace("/stats/", "");

            OfflinePlayer p = Bukkit.getOfflinePlayer(name);

            PreparedStatement ps = plugin.db.getConnection().prepareStatement(
                    "SELECT * FROM player_stats WHERE uuid=?"
            );
            ps.setString(1, p.getUniqueId().toString());

            ResultSet rs = ps.executeQuery();

            String json;

            if (rs.next()) {
                json = "{"
                        + "\"player\":\"" + name + "\","
                        + "\"online\":" + p.isOnline() + ","
                        + "\"playtime\":" + rs.getLong("playtime") + ","
                        + "\"blocks_place\":" + rs.getInt("blocks_place") + ","
                        + "\"blocks_break\":" + rs.getInt("blocks_break") + ","
                        + "\"last_seen\":" + rs.getLong("last_seen")
                        + "}";
            } else {
                json = "{\"error\":\"not_found\"}";
            }

            ex.sendResponseHeaders(200, json.getBytes().length);
            OutputStream os = ex.getResponseBody();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();

        } catch (Exception e) {
            try {
                String err = "{\"error\":\"internal\"}";
                ex.sendResponseHeaders(500, err.length());
                ex.getResponseBody().write(err.getBytes());
                ex.getResponseBody().close();
            } catch (Exception ignored) {}
        }
    }
}
