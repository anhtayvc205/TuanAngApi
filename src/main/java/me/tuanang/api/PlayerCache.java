public class PlayerCache {
    private static final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public static PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public static void put(UUID uuid, PlayerData data) {
        cache.put(uuid, data);
    }

    public static Collection<PlayerData> values() {
        return cache.values();
    }
}
