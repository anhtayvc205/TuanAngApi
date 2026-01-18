package me.tuanang.api;

import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.Player;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    private PlayerCache.PlayerData get(Player p) {
        return plugin.cache.data.computeIfAbsent(
                p.getName().toLowerCase(),
                k -> new PlayerCache.PlayerData()
        );
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        var d = get(e.getPlayer());
        d.joinTime = System.currentTimeMillis();
        d.lastSeen = 0;
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        var d = get(e.getPlayer());
        d.playtime += (System.currentTimeMillis() - d.joinTime) / 1000;
        d.lastSeen = System.currentTimeMillis() / 1000;
    }

    @EventHandler public void breakB(BlockBreakEvent e){ get(e.getPlayer()).breakBlock++; }
    @EventHandler public void placeB(BlockPlaceEvent e){ get(e.getPlayer()).placeBlock++; }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        get(e.getEntity()).death++;
        if (e.getEntity().getKiller() != null)
            get(e.getEntity().getKiller()).kill++;
    }
}
