package me.tuanang.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class JsonUtil {
    static Gson g = new Gson();
    static Type type = new TypeToken<Map<UUID, StatsCache.PlayerStats>>(){}.getType();

    public static String toJson(Map<UUID, StatsCache.PlayerStats> m) {
        return g.toJson(m);
    }

    public static Map<UUID, StatsCache.PlayerStats> fromJson(String s) {
        return g.fromJson(s, type);
    }
}
