package me.tuanang.api;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private Connection conn;

    public void connect() {
        try {
            File folder = new File("plugins/TuanAngApi");
            if (!folder.exists()) folder.mkdirs(); // <<< QUAN TRỌNG

            String url = "jdbc:sqlite:" + folder.getPath() + "/data.db";
            conn = DriverManager.getConnection(url);

            Statement st = conn.createStatement();
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS player_stats (" +
                            "uuid TEXT PRIMARY KEY," +
                            "name TEXT," +
                            "playtime INTEGER," +
                            "blocks_place INTEGER," +
                            "blocks_break INTEGER," +
                            "last_seen INTEGER" +
                            ")"
            );
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void savePlayer(String uuid, String name, long playtime, int place, int brk, long lastSeen) {
        try {
            var ps = conn.prepareStatement(
                    "INSERT OR REPLACE INTO player_stats VALUES (?,?,?,?,?,?)"
            );
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setLong(3, playtime);
            ps.setInt(4, place);
            ps.setInt(5, brk);
            ps.setLong(6, lastSeen);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
