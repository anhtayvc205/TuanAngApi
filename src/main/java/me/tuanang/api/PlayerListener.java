package me.tuanang.api;

import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerData d = PlayerCache.get(e.getPlayer().getName());
        d.online = true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerData d = PlayerCache.get(e.getPlayer().getName());
        d.online = false;
        d.lastSeen = System.currentTimeMillis();
    }
}
