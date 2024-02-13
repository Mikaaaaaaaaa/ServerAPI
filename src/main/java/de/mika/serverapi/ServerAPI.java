package de.mika.serverapi;

import de.mika.serverapi.commands.BanCommand;
import de.mika.serverapi.database.ServerDatabase;
import de.mika.serverapi.events.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("rank").setExecutor(new RankCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ServerDatabase.getInstance().close();
        ServerDatabase.getInstance().drop();
    }
}
