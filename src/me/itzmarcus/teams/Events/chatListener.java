package me.itzmarcus.teams.Events;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by marcus on 22-06-2016.
 */
public class chatListener implements Listener {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
            String oldFormat = e.getFormat();
            e.setFormat("§8[" + teamManager.getPlayerPrefixTeam(e.getPlayer().getName()) + "§8] §r" + oldFormat); // Line 20
    }
}
