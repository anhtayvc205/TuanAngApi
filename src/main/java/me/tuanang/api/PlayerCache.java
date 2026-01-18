package me.tuanang.api;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    private static final ConcurrentHashMap<String, PlayerData> cache = new ConcurrentHashMap<>();

    public static void init() {}

    public static PlayerData get(String name) {
        return cache.computeIfAbsent(name.toLowerCase(), k -> {
            PlayerData d = new PlayerData();
            d.name = name;
            d.lastSeen = System.currentTimeMillis();
            return d;
        });
    }

    public static ConcurrentHashMap<String, PlayerData> all() {
        return cache;
    }
}
