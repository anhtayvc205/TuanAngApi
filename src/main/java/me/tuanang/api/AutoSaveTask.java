package me.tuanang.api;

import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {
    private final TuanAngApi plugin;

    public AutoSaveTask(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.cache.data.forEach((k, v) -> v.playtime += 10);
        Utils.save(plugin);
    }
}
