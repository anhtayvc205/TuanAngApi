
package me.keran;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerDataService service;

    public PlayerListener(PlayerDataService s) {
        this.service = s;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        service.getData(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        service.getData(e.getPlayer());
    }
}
