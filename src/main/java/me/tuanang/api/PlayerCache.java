public class PlayerCache {

    private final Map<UUID, PlayerData> map = new ConcurrentHashMap<>();
    private final Database db;

    public PlayerCache(Database db) {
        this.db = db;
    }

    public void onJoin(Player p) {
        map.put(p.getUniqueId(), db.load(p.getUniqueId(), p.getName()));
    }

    public void onQuit(Player p) {
        PlayerData d = map.remove(p.getUniqueId());
        d.lastSeen = System.currentTimeMillis();
        db.save(d);
    }

    public PlayerData get(String name) {
        return map.values().stream()
                .filter(d -> d.name.equalsIgnoreCase(name))
                .findFirst().orElse(db.loadByName(name));
    }

    public void flushAll() {
        map.values().forEach(db::save);
    }
}
