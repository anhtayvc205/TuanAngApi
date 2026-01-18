package me.tuanang.api;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    public ConcurrentHashMap<String, PlayerData> data = new ConcurrentHashMap<>();

    public PlayerData get(String name) {
        return data.computeIfAbsent(name.toLowerCase(), k -> new PlayerData());
    }
}
