
package me.keran;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class KeranPlugin extends JavaPlugin {

    private PlayerDataService dataService;
    private HttpService httpService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getDataFolder().mkdirs();
        new File(getDataFolder(), "players").mkdirs();

        dataService = new PlayerDataService(this);
        httpService = new HttpService(this, dataService);
        httpService.start();

        getServer().getPluginManager().registerEvents(
                new PlayerListener(dataService), this
        );
    }

    @Override
    public void onDisable() {
        if (httpService != null) httpService.stop();
    }
}
