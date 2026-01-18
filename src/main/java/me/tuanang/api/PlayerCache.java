package me.tuanang.api;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

    public static class Data {
        public long breakBlock, placeBlock, mobKill, death, playtime;
        public long lastSeen;
    }

    private final ConcurrentHashMap<String, Data> cache = new ConcurrentHashMap<>();

    public Data get(String name) {
        return cache.computeIfAbsent(name.toLowerCase(), k -> new Data());
    }

    public boolean exists(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    public void saveAll() {
        // giữ trong RAM (hosting reset = reset data)
        // nếu muốn: ghi file json ở đây
    }
}
