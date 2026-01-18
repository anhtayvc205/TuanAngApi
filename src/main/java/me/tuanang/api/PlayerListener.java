package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        PlayerCache c = PlayerCache.get(e.getPlayer().getName());
        c.online = true;
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        PlayerCache c = PlayerCache.get(e.getPlayer().getName());
        c.online = false;
        c.lastSeen = System.currentTimeMillis() / 1000;
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        PlayerCache.get(e.getPlayer().getName()).blockPlace++;
    }

    @EventHandler
    public void breakb(BlockBreakEvent e) {
        PlayerCache.get(e.getPlayer().getName()).blockBreak++;
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        PlayerCache c = PlayerCache.get(e.getEntity().getName());
        c.death++;
        if (e.getEntity().getKiller() != null) {
            PlayerCache.get(e.getEntity().getKiller().getName()).kill++;
        }
    }
}
