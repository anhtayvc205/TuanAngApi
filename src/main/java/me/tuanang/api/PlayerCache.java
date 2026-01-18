package me.tuanang.api;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {

    public static class Data {
        public long breakBlock;
        public long placeBlock;
        public long mobKill;
        public long death;
        public long playtime;
        public long lastSeen;
    }

    private final ConcurrentHashMap<String, Data> cache = new ConcurrentHashMap<>();

    public Data get(String name) {
        return cache.computeIfAbsent(name.toLowerCase(), k -> new Data());
    }

    public boolean exists(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    // ✅ THÊM HÀM NÀY (FIX BUILD)
    public Collection<Data> all() {
        return cache.values();
    }
}
