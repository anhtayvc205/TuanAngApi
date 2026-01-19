
package me.keran;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.net.InetSocketAddress;

public class HttpService {

    private final PlayerDataService dataService;
    private HttpServer server;
    private final Gson gson = new Gson();

    public HttpService(KeranPlugin plugin, PlayerDataService dataService) {
        this.dataService = dataService;
    }

    public void start() {
        try {
            int port = KeranPlugin.getPlugin(KeranPlugin.class).getConfig().getInt("port", 25637);
            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/player", e -> {
                String name = e.getRequestURI().getQuery().replace("name=", "");
                OfflinePlayer p = Bukkit.getOfflinePlayer(name);
                String json = gson.toJson(dataService.getData(p));
                e.sendResponseHeaders(200, json.getBytes().length);
                e.getResponseBody().write(json.getBytes());
                e.close();
            });

            new Thread(server::start).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) server.stop(0);
    }
}
