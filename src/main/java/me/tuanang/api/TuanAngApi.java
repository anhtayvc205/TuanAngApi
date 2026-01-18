@Override
public void onEnable() {
    Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    new AutoSaveTask().runTaskTimer(this, 20L, 20L); // mỗi giây
}
