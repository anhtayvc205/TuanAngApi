package me.tuanang.api;

public class AutoSaveTask implements Runnable {
    @Override
    public void run() {
        long now = System.currentTimeMillis();
        for (PlayerData d : PlayerCache.all().values()) {
            if (d.online) {
                d.playtime += 10; // +10s
                d.lastSeen = now;
            }
        }
    }
}
