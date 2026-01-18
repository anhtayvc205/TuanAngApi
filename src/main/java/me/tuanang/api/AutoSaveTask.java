public class AutoSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerCache.get(p.getUniqueId());
            if (data != null) {
                data.playtime++; // chỉ online mới tăng
                data.lastSeen = System.currentTimeMillis();
            }
        }
    }
}
