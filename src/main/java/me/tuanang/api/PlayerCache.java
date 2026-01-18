package me.tuanang.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

    public static Map<String, PlayerCache> cache = new ConcurrentHashMap<>();

    public String name;
    public boolean online;
    public long playtime; // seconds
    public long lastSeen; // unix
    public int blockPlace;
    public int blockBreak;
    public int kill;
    public int death;

    public PlayerCache(String name) {
        this.name = name;
    }

    public void tick() {
        if (online) {
            playtime += 10;
        }
    }

    public static PlayerCache get(String name) {
        return cache.computeIfAbsent(name.toLowerCase(), PlayerCache::new);
    }
}
