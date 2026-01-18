package me.tuanang.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.OutputStream;
import java.util.UUID;

public class StatsHandler implements HttpHandler {

    private final TuanAngApi plugin;

    public StatsHandler(TuanAngApi p){this.plugin=p;}

    @Override
    public void handle(HttpExchange ex){
        try{
            String name = ex.getRequestURI().getPath().replace("/stats/","");
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);

            if(!plugin.CACHE.exists(p.getUniqueId())){
                write(ex,"{\"error\":\"Người chơi chưa từng vào server\"}");
                return;
            }

            UUID u = p.getUniqueId();
            long last = plugin.CACHE.getLong(u,"lastSeen");

            String seen = p.isOnline()
                    ? "Đang online"
                    : TimeUtil.ago(last);

            String json = "{"+
                    "\"name\":\""+name+"\","+
                    "\"lastSeen\":\""+seen+"\","+
                    "\"break\":"+plugin.CACHE.getLong(u,"break")+","+
                    "\"place\":"+plugin.CACHE.getLong(u,"place")+","+
                    "\"kill\":"+plugin.CACHE.getLong(u,"kill")+","+
                    "\"death\":"+plugin.CACHE.getLong(u,"death")+","+
                    "\"playtime\":\""+TimeUtil.format(plugin.CACHE.getPlaytime(u))+"\""+
                    "}";

            write(ex,json);

        }catch(Exception e){
            try{write(ex,"{\"error\":\"internal\"}");}catch(Exception ignored){}
        }
    }

    private void write(HttpExchange ex,String s)throws Exception{
        ex.sendResponseHeaders(200,s.getBytes().length);
        OutputStream os=ex.getResponseBody();
        os.write(s.getBytes());
        os.close();
    }
}
