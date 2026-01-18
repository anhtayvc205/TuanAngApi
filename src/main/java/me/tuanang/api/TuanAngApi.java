public class TuanAngApi extends JavaPlugin {

    public static TuanAngApi instance;
    public Database db;
    public PlayerCache cache;

    @Override
    public void onEnable() {
        instance = this;

        db = new Database(this);
        cache = new PlayerCache(db);

        getServer().getPluginManager().registerEvents(
                new PlayerListener(this), this
        );

        startHttp();
        startAutoSave();
    }

    void startHttp() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(25673), 0);
            server.createContext("/stats", new StatsHandler(this));
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startAutoSave() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(
                this,
                () -> cache.flushAll(),
                200L, 200L // 10s
        );
    }
}
