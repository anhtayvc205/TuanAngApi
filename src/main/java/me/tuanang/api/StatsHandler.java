package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;
    private final String apiKey;

    public StatsHandler(TuanAngApi plugin, String apiKey) {
        this.plugin = plugin;
        this.apiKey = apiKey;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.contains("key=" + apiKey)) {
                send(exchange, 403, "{\"error\":\"invalid key\"}");
                return;
            }

            String[] path = exchange.getRequestURI().getPath().split("/");
            if (path.length < 3) {
                send(exchange, 400, "{\"error\":\"missing player\"}");
                return;
            }

            String name = path[2];
            Player p = Bukkit.getPlayerExact(name);

            if (p == null) {
                send(exchange, 200, "{\"error\":\"player offline\"}");
                return;
            }

            String json =
                    "{\n" +
                    "  \"player\": \"" + p.getName() + "\",\n" +
                    "  \"uuid\": \"" + p.getUniqueId() + "\",\n" +
                    "  \"health\": " + p.getHealth() + ",\n" +
                    "  \"food\": " + p.getFoodLevel() + ",\n" +
                    "  \"playtime\": " + (p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) + ",\n" +
                    "  \"deaths\": " + p.getStatistic(Statistic.DEATHS) + ",\n" +
                    "  \"kills\": " + p.getStatistic(Statistic.MOB_KILLS) + "\n" +
                    "}";

            send(exchange, 200, json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(HttpExchange ex, int code, String body) throws Exception {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
