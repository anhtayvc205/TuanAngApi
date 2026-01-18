package me.tuanang.api;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        // autosave cache
        PlayerCache.saveAll();
        Bukkit.getLogger().info("[TuanAngApi] Auto saved player cache");
    }

    public static void start() {
        new AutoSaveTask().runTaskTimerAsynchronously(
                TuanAngApi.getInstance(),
                20L * 60,
                20L * 60
        );
    }
}
