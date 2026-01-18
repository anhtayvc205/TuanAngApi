package me.tuanang.api;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u, "lastSeen", System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u, "lastSeen", System.currentTimeMillis());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u, "place",
                plugin.CACHE.getLong(u, "place") + 1);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u, "break",
                plugin.CACHE.getLong(u, "break") + 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UUID u = e.getEntity().getUniqueId();
        plugin.CACHE.update(u, "death",
                plugin.CACHE.getLong(u, "death") + 1);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        UUID u = e.getEntity().getKiller().getUniqueId();
        plugin.CACHE.update(u, "kill",
                plugin.CACHE.getLong(u, "kill") + 1);
    }
}
