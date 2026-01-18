package me.tuanang.api;

import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        plugin.cache.get(e.getPlayer().getUniqueId()).breakBlock++;
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        plugin.cache.get(e.getPlayer().getUniqueId()).placeBlock++;
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        plugin.cache.get(e.getEntity().getUniqueId()).death++;
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            plugin.cache.get(e.getEntity().getKiller().getUniqueId()).kill++;
        }
    }

    @EventHandler
    public void playtime(PlayerMoveEvent e) {
        plugin.cache.get(e.getPlayer().getUniqueId()).playtime++;
        plugin.cache.get(e.getPlayer().getUniqueId()).lastSeen = System.currentTimeMillis();
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        plugin.cache.get(e.getPlayer().getUniqueId()).lastSeen = System.currentTimeMillis();
    }
}
