package me.tuanang.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;

public class StatsCache {

    private final JavaPlugin plugin;
    private final File file;
    public Map<UUID, PlayerStats> data = new HashMap<>();

    public StatsCache(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "stats.json");
        plugin.getDataFolder().mkdirs();
    }

    public PlayerStats get(UUID uuid) {
        return data.computeIfAbsent(uuid, k -> new PlayerStats());
    }

    public void load() {
        try {
            if (!file.exists()) return;
            String json = new String(Files.readAllBytes(file.toPath()));
            data = JsonUtil.fromJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (FileWriter w = new FileWriter(file)) {
            w.write(JsonUtil.toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class PlayerStats {
        public long breakBlock = 0;
        public long placeBlock = 0;
        public long kill = 0;
        public long death = 0;
        public long playtime = 0;
        public long lastSeen = System.currentTimeMillis();
    }
}
