package me.tuanang.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public class Database {

    private final String url;

    public Database(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "data.db");
        url = "jdbc:sqlite:" + file.getAbsolutePath();

        try (Connection c = connect()) {
            c.createStatement().execute("""
            CREATE TABLE IF NOT EXISTS stats (
                name TEXT PRIMARY KEY,
                playtime LONG,
                breakBlock INT,
                placeBlock INT,
                kill INT,
                death INT,
                lastSeen LONG
            )
            """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void saveAll(PlayerCache cache) {
        try (Connection c = connect()) {
            for (var e : cache.data.entrySet()) {
                var p = e.getValue();
                PreparedStatement ps = c.prepareStatement("""
                INSERT INTO stats VALUES(?,?,?,?,?,?,?)
                ON CONFLICT(name) DO UPDATE SET
                playtime=?, breakBlock=?, placeBlock=?, kill=?, death=?, lastSeen=?
                """);

                ps.setString(1, e.getKey());
                ps.setLong(2, p.playtime);
                ps.setInt(3, p.breakBlock);
                ps.setInt(4, p.placeBlock);
                ps.setInt(5, p.kill);
                ps.setInt(6, p.death);
                ps.setLong(7, p.lastSeen);

                ps.setLong(8, p.playtime);
                ps.setInt(9, p.breakBlock);
                ps.setInt(10, p.placeBlock);
                ps.setInt(11, p.kill);
                ps.setInt(12, p.death);
                ps.setLong(13, p.lastSeen);

                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
