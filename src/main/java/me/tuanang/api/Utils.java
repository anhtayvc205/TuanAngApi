package me.tuanang.api;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class Utils {

    private static final File FILE = new File("plugins/TuanAngApi/stats.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(TuanAngApi plugin) {
        FILE.getParentFile().mkdirs();
        try (Writer w = new FileWriter(FILE)) {
            GSON.toJson(plugin.cache.data, w);
        } catch (Exception ignored) {}
    }

    public static void load(TuanAngApi plugin) {
        if (!FILE.exists()) return;
        try {
            Reader r = new FileReader(FILE);
            plugin.cache.data = GSON.fromJson(r, plugin.cache.data.getClass());
        } catch (Exception ignored) {}
    }

    public static String time(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return h+"h"+m+"m"+s+"s";
    }

    public static String lastSeen(long t) {
        long diff = (System.currentTimeMillis() - t) / 1000;
        if (diff < 10) return "đang online";
        if (diff < 60) return diff+" giây trước";
        if (diff < 3600) return diff/60+" phút trước";
        return diff/3600+" giờ trước";
    }
}
