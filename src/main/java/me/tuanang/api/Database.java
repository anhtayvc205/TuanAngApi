package me.tuanang.api;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    private final Connection conn;
    public final ConcurrentHashMap<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public Database(String path) throws Exception {
        conn = DriverManager.getConnection("jdbc:sqlite:" + path);
        init();
    }

    private void init() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS stats (
                    uuid TEXT PRIMARY KEY,
                    playtime INTEGER,
                    blocks_place INTEGER,
                    blocks_break INTEGER,
                    last_seen INTEGER
                )
            """);
        }
    }

    public PlayerData get(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM stats WHERE uuid=?")) {
                ps.setString(1, u.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new PlayerData(
                            rs.getLong("playtime"),
                            rs.getInt("blocks_place"),
                            rs.getInt("blocks_break"),
                            rs.getLong("last_seen")
                    );
                }
            } catch (Exception ignored) {}
            return new PlayerData();
        });
    }

    public void save(UUID uuid, PlayerData d) {
        try (PreparedStatement ps = conn.prepareStatement("""
            INSERT OR REPLACE INTO stats VALUES (?,?,?,?,?)
