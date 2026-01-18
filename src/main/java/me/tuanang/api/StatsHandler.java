package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StatsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange ex) {
        try {
            String[] sp = ex.getRequestURI().getPath().split("/");
            if (sp.length < 3) {
                write(ex, "{\"error\":\"no_player\"}");
                return;
            }

            String name = sp[2].toLowerCase();
            PlayerCache c = PlayerCache.cache.get(name);

            if (c == null) {
                write(ex, "{\"error\":\"never_joined\"}");
                return;
            }

            JSONObject j = new JSONObject();
            j.put("name", c.name);
            j.put("break", c.blockBreak);
            j.put("place", c.blockPlace);
            j.put("kill", c.kill);
            j.put("death", c.death);
            j.put("playtime", formatTime(c.playtime));
            j.put("lastSeen", c.online ? "Đang online" : ago(c.lastSeen));

            write(ex, j.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write(HttpExchange ex, String s) throws Exception {
        ex.sendResponseHeaders(200, s.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(s.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private String formatTime(long s) {
        long h = s / 3600;
        long m = (s % 3600) / 60;
        long sec = s % 60;
        return h + "h" + m + "p" + sec + "s";
    }

    private String ago(long t) {
        long diff = (System.currentTimeMillis()/1000) - t;
        if (diff < 60) return diff + " giây trước";
        if (diff < 3600) return (diff/60) + " phút trước";
        return (diff/3600) + " giờ trước";
    }
}
