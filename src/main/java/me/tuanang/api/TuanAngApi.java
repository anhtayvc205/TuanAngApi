
package me.tuanang.api;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class TuanAngApi extends JavaPlugin {

    private final Gson gson = new Gson();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int port = getConfig().getInt("port");
        String key = getConfig().getString("key");

        Spark.ipAddress("0.0.0.0");
        Spark.port(port);

        Spark.get("/stats/:name", (req, res) -> {
            if (!key.equals(req.queryParams("key"))) {
                res.status(401);
                return "{\"error\":\"invalid_key\"}";
            }

            String name = req.params(":name");

            CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();

            Bukkit.getScheduler().runTask(this, () -> {
                Map<String, Object> data = new HashMap<>();
                OfflinePlayer off = Bukkit.getOfflinePlayer(name);

                data.put("name", off.getName());
                data.put("uuid", off.getUniqueId().toString());
                data.put("online", off.isOnline());

                if (off.isOnline()) {
                    var p = off.getPlayer();
                    data.put("blocks_mined", p.getStatistic(org.bukkit.Statistic.MINE_BLOCK));
                    data.put("deaths", p.getStatistic(org.bukkit.Statistic.DEATHS));
                    data.put("play_time", p.getStatistic(org.bukkit.Statistic.PLAY_ONE_MINUTE) / 20);
                }

                future.complete(data);
            });

            res.type("application/json");
            return gson.toJson(future.get());
        });

        getLogger().info("API started on port " + port);
    }

    @Override
    public void onDisable() {
        Spark.stop();
    }
}
