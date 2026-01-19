
package me.keran;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class PremiumChecker {
    private static final ConcurrentHashMap<String, Boolean> CACHE = new ConcurrentHashMap<>();

    public static boolean check(String name) {
        if (CACHE.containsKey(name)) return CACHE.get(name);
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setConnectTimeout(5000);
            boolean premium = c.getResponseCode() == 200;
            CACHE.put(name, premium);
            return premium;
        } catch (Exception e) {
            return false;
        }
    }
}
