package me.tuanang.api;

public class Utils {

    public static String formatTime(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return h + "h" + m + "m" + s + "s";
    }

    public static String lastSeen(PlayerData d) {
        if (d.online) return "đang online";
        long diff = (System.currentTimeMillis() - d.lastSeen) / 1000;
        if (diff < 60) return diff + " giây trước";
        if (diff < 3600) return (diff / 60) + " phút trước";
        return (diff / 3600) + " giờ trước";
    }
}
