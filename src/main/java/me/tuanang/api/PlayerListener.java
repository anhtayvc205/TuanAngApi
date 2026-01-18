package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final TuanAngApi plugin;

    public PlayerListener(TuanAngApi plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        plugin.joinTime.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        var id = e.getPlayer().getUniqueId();
        long joined = plugin.joinTime.getOrDefault(id, System.currentTimeMillis());
        long play = (System.currentTimeMillis() - joined) / 1000;

        PlayerData d = plugin.db.get(id);
        d.playtime += play;
        d.lastSeen = System.currentTimeMillis() / 1000;
        plugin.db.save(id, d);
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        plugin.db.get(e.getPlayer().getUniqueId()).place++;
    }

    @EventHandler
    public void breakB(BlockBreakEvent e) {
        plugin.db.get(e.getPlayer().getUniqueId()).breaks++;
    }
}
