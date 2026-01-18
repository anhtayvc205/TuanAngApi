@EventHandler
public void onJoin(PlayerJoinEvent e) {
    plugin.cache.onJoin(e.getPlayer());
}

@EventHandler
public void onQuit(PlayerQuitEvent e) {
    plugin.cache.onQuit(e.getPlayer());
}

@EventHandler
public void onBreak(BlockBreakEvent e) {
    plugin.cache.addBreak(e.getPlayer().getUniqueId());
}

@EventHandler
public void onPlace(BlockPlaceEvent e) {
    plugin.cache.addPlace(e.getPlayer().getUniqueId());
}

@EventHandler
public void onDeath(PlayerDeathEvent e) {
    plugin.cache.addDeath(e.getEntity().getUniqueId());
    if (e.getEntity().getKiller() != null)
        plugin.cache.addKill(e.getEntity().getKiller().getUniqueId());
}
