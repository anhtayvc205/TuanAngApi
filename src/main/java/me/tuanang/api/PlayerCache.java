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

    // ✅ STATIC CACHE
    private static final ConcurrentHashMap<String, Data> cache = new ConcurrentHashMap<>();

    public static Data get(String name) {
        return cache.computeIfAbsent(name.toLowerCase(), k -> new Data());
    }

    public static boolean exists(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    // ✅ FIX LỖI all()
    public static Collection<Data> all() {
        return cache.values();
    }

    // ✅ FIX LỖI saveAll()
    public static void saveAll() {
        for (Data d : cache.values()) {
            // TODO: lưu DB / file / json nếu bạn muốn
        }
    }
}
