package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerData d = PlayerCache.get(e.getPlayer().getName());
        d.online = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!d.online) {
                    cancel();
                    return;
                }
                d.playtime++;
            }
        }.runTaskTimerAsynchronously(TuanAngApi.getInstance(), 20, 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerData d = PlayerCache.get(e.getPlayer().getName());
        d.online = false;
        d.lastSeen = System.currentTimeMillis();
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        PlayerCache.get(e.getPlayer().getName()).blockBreak++;
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        PlayerCache.get(e.getPlayer().getName()).blockPlace++;
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        PlayerCache.get(e.getEntity().getName()).death++;
    }
}
