package me.tuanang.api;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    public ConcurrentHashMap<String, PlayerData> data = new ConcurrentHashMap<>();

    public static class PlayerData {
        public long joinTime;
        public long playtime;
        public int breakBlock, placeBlock, kill, death;
        public long lastSeen;
    }
}
