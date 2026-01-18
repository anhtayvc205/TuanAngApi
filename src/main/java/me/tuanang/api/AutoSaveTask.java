package me.tuanang.api;

import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        PlayerCache.saveAll();
    }

    public static void start() {
        new AutoSaveTask().runTaskTimerAsynchronously(
                TuanAngApi.getInstance(), 20 * 60, 20 * 60
        );
    }
}
