package me.tuanang.api;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerCache {

    private final File file;
    private final YamlConfiguration data;
    private final Map<UUID, Map<String, Object>> ram = new HashMap<>();

    public PlayerCache(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (Exception ignored) {}
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public Object get(UUID u, String k) {
        return ram.getOrDefault(u, Map.of()).getOrDefault(k,
                data.get(u.toString()+"."+k, 0));
    }

    public long getLong(UUID u, String k) {
        return ((Number)get(u,k)).longValue();
    }

    public void update(UUID u, String k, Object v) {
        ram.computeIfAbsent(u, x->new HashMap<>()).put(k,v);
        data.set(u.toString()+"."+k, v);
    }

    public void addPlaytime(UUID u, long sec) {
        update(u,"playtime", getLong(u,"playtime")+sec);
    }

    public long getPlaytime(UUID u){
        return getLong(u,"playtime");
    }

    public void saveAll() {
        try { data.save(file); } catch (IOException ignored) {}
    }

    public boolean exists(UUID u){
        return data.contains(u.toString());
    }
                            }
