package me.tuanang.api;

import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi p){this.plugin=p;}

    @EventHandler
    public void join(PlayerJoinEvent e){
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u,"lastSeen",System.currentTimeMillis());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e){
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u,"lastSeen",System.currentTimeMillis());
    }

    @EventHandler
    public void place(BlockPlaceEvent e){
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u,"place", plugin.CACHE.getLong(u,"place")+1);
    }

    @EventHandler
    public void breakB(BlockBreakEvent e){
        UUID u = e.getPlayer().getUniqueId();
        plugin.CACHE.update(u,"break", plugin.CACHE.getLong(u,"break")+1);
    }

    @EventHandler
    public void death(PlayerDeathEvent e){
        UUID u = e.getEntity().getUniqueId();
        plugin.CACHE.update(u,"death", plugin.CACHE.getLong(u,"death")+1);
    }

    @EventHandler
    public void kill(EntityDeathEvent e){
        if(e.getEntity().getKiller()==null) return;
        UUID u = e.getEntity().getKiller().getUniqueId();
        plugin.CACHE.update(u,"kill", plugin.CACHE.getLong(u,"kill")+1);
    }
}
