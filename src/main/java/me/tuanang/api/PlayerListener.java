@EventHandler
public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    PlayerData data = PlayerCache.get(p.getUniqueId());

    if (data == null) {
        data = new PlayerData();
        data.uuid = p.getUniqueId();
        data.name = p.getName();
        PlayerCache.put(p.getUniqueId(), data);
    }

    data.lastSeen = System.currentTimeMillis();
}

@EventHandler
public void onQuit(PlayerQuitEvent e) {
    PlayerData data = PlayerCache.get(e.getPlayer().getUniqueId());
    if (data != null) {
        data.lastSeen = System.currentTimeMillis();
    }
}
