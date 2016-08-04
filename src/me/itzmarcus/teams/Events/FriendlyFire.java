package me.itzmarcus.teams.Events;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by marcus on 24-06-2016.
 */
public class FriendlyFire implements Listener {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));

    @EventHandler
    public void attack(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            Player shooter = (Player) arrow.getShooter();
            Player hitted = (Player) e.getEntity();
            if(!teamManager.checkIfPlayerHasATeam(shooter.getName())) {
                return;
            }
            if(teamManager.getPlayerTeam(shooter.getName()).equals(teamManager.getPlayerTeam(hitted.getName()))) {
                e.setCancelled(true);
            }
        }
        Player attacker = (Player) e.getDamager();
        Player hitted = (Player) e.getEntity();
        if(!teamManager.checkIfPlayerHasATeam(attacker.getName())) {
            return;
        }
        if(teamManager.getPlayerTeam(attacker.getName()).equals(teamManager.getPlayerTeam(hitted.getName()))) {
            e.setCancelled(true);
        }
    }
}
