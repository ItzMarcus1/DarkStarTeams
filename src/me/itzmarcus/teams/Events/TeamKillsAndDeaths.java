package me.itzmarcus.teams.Events;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by marcus on 24-06-2016.
 */
public class TeamKillsAndDeaths implements Listener {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));

    public static HashMap<Integer, Integer> missions = new HashMap<>(); // Kills, Reward

    public void addMissions() {
        missions.clear();
        int kills = 100;
        int money = 125;
        for(int i=0;i<15;i++) {
            if(kills < 1500) {
                missions.put(kills, money);
                kills = kills + 100;
                money = money + 15;
            }
        }
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        addMissions();
        Player killer = e.getEntity().getKiller();
        Player death = e.getEntity();
        if(teamManager.checkIfPlayerHasATeam(killer.getName())) {
            teamManager.increaseTeamKills(teamManager.getPlayerTeam(killer.getName()));
            for(Integer x : missions.keySet()) {
                if (teamManager.getTeamKills(teamManager.getPlayerTeam(killer.getName())) == x) {
                    EconomyResponse killerR = Core.econ.depositPlayer(killer, missions.get(x));
                    if(killerR.transactionSuccess()) {
                        killer.sendMessage("§8[§3DarkStarPvP§8] §aDit Team har lige klaret en Mission §7- §cFå " + x + " kills §8(§3Belønning: §6" + missions.get(x) + "$§8)");
                    }
                    for(String s : teamManager.getTeamMembers(teamManager.getPlayerTeam(killer.getName()))) {
                        EconomyResponse r = Core.econ.depositPlayer(Bukkit.getPlayer(s), missions.get(x));
                        if(r.transactionSuccess()) {
                            Player p = Bukkit.getServer().getPlayer(s);
                            if(p != null) {
                                p.sendMessage("§8[§3DarkStarPvP§8] §aDit Team har lige klaret en Mission §7- §cFå " + x + " kills §8(§3Belønning: §6" + missions.get(x) + "$§8)");
                            }
                        }
                    }
                }
            }
        }
        if(teamManager.checkIfPlayerHasATeam(death.getName())) {
            teamManager.increaseTeamDeaths(teamManager.getPlayerTeam(death.getName()));
        }
    }
}
