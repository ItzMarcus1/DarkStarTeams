package me.itzmarcus.teams;

import me.itzmarcus.teams.Events.FriendlyFire;
import me.itzmarcus.teams.Events.TeamKillsAndDeaths;
import me.itzmarcus.teams.Events.chatListener;
import me.itzmarcus.teams.Inventory.InventoryManager;
import me.itzmarcus.teams.Inventory.TeamShopEvents;
import me.itzmarcus.teams.Kommandoer.createTeam;
import me.itzmarcus.teams.Utils.MyConfig;
import me.itzmarcus.teams.Utils.MyConfigManager;
import me.itzmarcus.teams.Utils.Team;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by marcus on 18-04-2016.
 */
public class Core extends JavaPlugin implements Listener {

    private void newEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void newCommand(String command, CommandExecutor commandExecutor) {
        getCommand(command).setExecutor(commandExecutor);
    }
    public static Economy econ = null;

    Team manager = null;
    TeamKillsAndDeaths missions;
    MyConfigManager Invmanager;
    public MyConfig teamInventory;
    public void onEnable() {
        if (!setupEconomy() ) {
            Bukkit.getServer().getConsoleSender().sendMessage("[DarkStarTeams] DETTE PLUGIN SKAL BRUGE ESSENTIALS!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Invmanager = new MyConfigManager(this);
        teamInventory = Invmanager.getNewConfig("inventory.yml");
        missions = new TeamKillsAndDeaths();
        manager = new Team(this);
        saveDefaultConfig();
        newCommand("team", new createTeam());
        newEvent(new InventoryManager());
        newEvent(new chatListener());
        newEvent(new FriendlyFire());
        newEvent(new TeamKillsAndDeaths());
        newEvent(new TeamShopEvents());
        manager.loadTeams();
        TeamKillsAndDeaths.missions.clear();
        int kills = 100;
        int money = 125;
        for(int i=0;i<15;i++) {
            if(kills < 1500) {
                TeamKillsAndDeaths.missions.put(kills, money);
                kills = kills + 100;
                money = money + 15;
            } else {
                kills = 100;
                money = 125;
            }
        }
    }

    public void onDisable() {
        Team.saveTeams();
        manager = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
