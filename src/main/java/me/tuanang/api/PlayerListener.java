package me.tuanang.api;

import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        plugin.cache.get(e.getPlayer().getName());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        plugin.cache.get(e.getPlayer().getName()).lastSeen = System.currentTimeMillis();
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        plugin.cache.get(e.getPlayer().getName()).blockBreak++;
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        plugin.cache.get(e.getPlayer().getName()).blockPlace++;
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        plugin.cache.get(e.getEntity().getName()).death++;
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            plugin.cache.get(e.getEntity().getKiller().getName()).kill++;
        }
    }
}
