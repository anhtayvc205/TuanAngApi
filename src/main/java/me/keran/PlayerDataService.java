package me.keran;

import com.google.gson.Gson;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;

public class PlayerDataService {

    private final KeranPlugin plugin;
    private final Gson gson = new Gson();

    public PlayerDataService(KeranPlugin plugin) {
        this.plugin = plugin;
    }

    // ===================================================
    // MAIN GET DATA
    // ===================================================
    public PlayerData getData(OfflinePlayer p) {

        // ===== ONLINE =====
        if (p.isOnline()) {
            PlayerData d = collectOnline(p.getPlayer());
            save(p, d);
            return d;
        }

        // ===== OFFLINE =====
        PlayerData d = load(p);
        if (d == null) {
            d = new PlayerData();
            d.uuid = p.getUniqueId().toString();
            d.name = p.getName();
            d.online = false;
            d.firstJoin = System.currentTimeMillis();
            d.lastSeen = System.currentTimeMillis();
        }

        d.online = false;
        return d;
    }

    // ===================================================
    // COLLECT ONLINE DATA (CHUẨN)
    // ===================================================
    private PlayerData collectOnline(Player p) {

        PlayerData d = new PlayerData();

        d.uuid = p.getUniqueId().toString();
        d.name = p.getName();
        d.online = true;

        // ===== STATS =====
        d.mobKills = PlaceholderAPI.setPlaceholders(p, "%statistic_mob_kills%");
        d.deaths = PlaceholderAPI.setPlaceholders(p, "%statistic_deaths%");
        d.blockPlace = PlaceholderAPI.setPlaceholders(p, "%statistic_blocks_placed%");
        d.blockBreak = PlaceholderAPI.setPlaceholders(p, "%statistic_mine_block%");

        // ===== PLAYTIME (CHUẨN – KHÔNG TĂNG KHI OFFLINE) =====
        long ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long seconds = ticks / 20;

        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;

        d.playtime = h + "h" + m + "m" + s + "s";

        // ===== LAST SEEN =====
        d.lastSeen = System.currentTimeMillis();

        // ===== FIRST JOIN (GIỮ NGUYÊN) =====
        PlayerData old = load(p);
        if (old != null && old.firstJoin > 0) {
            d.firstJoin = old.firstJoin;
        } else {
            d.firstJoin = System.currentTimeMillis();
        }

        return d;
    }

    // ===================================================
    // SAVE
    // ===================================================
    private void save(OfflinePlayer p, PlayerData d) {
        try {
            File folder = new File(plugin.getDataFolder(), "players");
            if (!folder.exists()) folder.mkdirs();

            File f = new File(folder, p.getUniqueId() + ".json");
            Files.writeString(f.toPath(), gson.toJson(d));
        } catch (Exception ignored) {}
    }

    // ===================================================
    // LOAD
    // ===================================================
    private PlayerData load(OfflinePlayer p) {
        try {
            File f = new File(plugin.getDataFolder(), "players/" + p.getUniqueId() + ".json");
            if (!f.exists()) return null;
            return gson.fromJson(Files.readString(f.toPath()), PlayerData.class);
        } catch (Exception e) {
            return null;
        }
    }
}
