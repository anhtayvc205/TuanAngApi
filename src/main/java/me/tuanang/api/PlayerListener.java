package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var d = TuanAngApi.instance.cache.get(e.getPlayer().getName());
        d.lastSeen = System.currentTimeMillis();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var d = TuanAngApi.instance.cache.get(e.getPlayer().getName());
        d.lastSeen = System.currentTimeMillis();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        TuanAngApi.instance.cache.get(e.getPlayer().getName()).breakBlock++;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        TuanAngApi.instance.cache.get(e.getPlayer().getName()).placeBlock++;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        TuanAngApi.instance.cache.get(e.getEntity().getKiller().getName()).mobKill++;
    }
}
