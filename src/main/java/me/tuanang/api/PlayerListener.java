package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
        plugin.joinTime.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        long join = plugin.joinTime.getOrDefault(uuid, System.currentTimeMillis());
        long playtime = (System.currentTimeMillis() - join) / 1000;

        plugin.db.savePlayer(
                uuid.toString(),
                e.getPlayer().getName(),
                playtime,
                0,
                0,
                System.currentTimeMillis()
        );

        plugin.joinTime.remove(uuid);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // sau này cộng block
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        // sau này cộng block
    }
}
