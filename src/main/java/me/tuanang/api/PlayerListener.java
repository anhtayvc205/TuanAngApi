public class PlayerData {
    public UUID uuid;
    public String name;

    public int breakBlock;
    public int placeBlock;
    public int kill;
    public int death;

    public long playtime; // seconds
    public long lastSeen; // millis

    public String formatPlaytime() {
        long s = playtime;
        long h = s / 3600;
        long m = (s % 3600) / 60;
        long sec = s % 60;

        if (h > 0) return h + "h" + m + "m" + sec + "s";
        if (m > 0) return m + "m" + sec + "s";
        return sec + "s";
    }

    public String lastSeenText() {
        long diff = (System.currentTimeMillis() - lastSeen) / 1000;
        if (diff < 60) return diff + " giây trước";
        if (diff < 3600) return diff / 60 + " phút trước";
        if (diff < 86400) return diff / 3600 + " giờ trước";
        return diff / 86400 + " ngày trước";
    }
}
