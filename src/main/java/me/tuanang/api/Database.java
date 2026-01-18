package me.tuanang.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {

    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:plugins/TuanAngApi/data.db"
            );

            try (Statement st = connection.createStatement()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(
            String uuid,
            String name,
            long playtime,
            int place,
            int breakBlock,
            long lastSeen
    ) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT OR REPLACE INTO player_stats " +
                            "(uuid,name,playtime,blocks_place,blocks_break,last_seen) " +
                            "VALUES (?,?,?,?,?,?)"
            );

            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setLong(3, playtime);
            ps.setInt(4, place);
            ps.setInt(5, breakBlock);
            ps.setLong(6, lastSeen);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
