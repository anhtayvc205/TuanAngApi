package me.tuanang.api;

public class PlayerData {
    public long playtime = 0;
    public int place = 0;
    public int breaks = 0;
    public long lastSeen = 0;

    public PlayerData() {}

    public PlayerData(long p, int pl, int br, long ls) {
        playtime = p;
        place = pl;
        breaks = br;
        lastSeen = ls;
    }
}
