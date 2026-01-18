package me.tuanang.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.UUID;

public class BlockListener implements Listener {

    public static HashMap<UUID, Integer> placed = new HashMap<>();
    public static HashMap<UUID, Integer> broken = new HashMap<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        placed.merge(e.getPlayer().getUniqueId(), 1, Integer::sum);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        broken.merge(e.getPlayer().getUniqueId(), 1, Integer::sum);
    }
}
