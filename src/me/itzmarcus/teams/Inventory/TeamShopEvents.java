package me.itzmarcus.teams.Inventory;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by marcus on 04-07-2016.
 */
public class TeamShopEvents implements Listener {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));

    @EventHandler
    public void click(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String team = teamManager.getPlayerTeam(p.getName());
        if(e.getInventory().getTitle().contains("Team Shop")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Team Speed")) {
                if(teamManager.getTeamPoints(team) < 10) {
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aDit Team har ikke nok Team Points til at købe dette.");
                } else {
                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aAlle i dit Team har nu §cSpeed I §ai 2 minutter.");
                    p.closeInventory();
                    teamManager.removeTeamPoints(team, 10);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 0));
                    for(String members : teamManager.getTeamMembers(team)) {
                        Player member = Bukkit.getPlayer(members);
                        if(member != null && member != p) {
                            member.sendMessage("§8[§3DarkStarPvP§8] §aEn spiller fra dit Team har købt §cSpeed I §atil alle i dit Team §8(§6" + p.getName() + "§8)");
                            member.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 0));
                        }
                    }
                }
            }

            if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Team Regen")) {
                if(teamManager.getTeamPoints(team) < 15) {
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aDit Team har ikke nok Team Points til at købe dette.");
                } else {
                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aAlle i dit Team har nu §cRegen II §ai 1 minut.");
                    p.closeInventory();
                    teamManager.removeTeamPoints(team, 15);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 1));
                    for(String members : teamManager.getTeamMembers(team)) {
                        Player member = Bukkit.getPlayer(members);
                        if(member != null && member != p) {
                            member.sendMessage("§8[§3DarkStarPvP§8] §aEn spiller fra dit Team har købt §cRegen II §atil alle i dit Team §8(§6" + p.getName() + "§8)");
                            member.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 1));
                        }
                    }
                }
            }

            if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Team Fire")) {
                if(teamManager.getTeamPoints(team) < 10) {
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aDit Team har ikke nok Team Points til at købe dette.");
                } else {
                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aAlle i dit Team har nu §cFire Resistance I §ai 2 minutter.");
                    p.closeInventory();
                    teamManager.removeTeamPoints(team, 10);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 2, 0));
                    for(String members : teamManager.getTeamMembers(team)) {
                        Player member = Bukkit.getPlayer(members);
                        if(member != null && member != p) {
                            member.sendMessage("§8[§3DarkStarPvP§8] §aEn spiller fra dit Team har købt §cFire Resistance I §atil alle i dit Team §8(§6" + p.getName() + "§8)");
                            member.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 2, 0));
                        }
                    }
                }
            }

            if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Køb Points")) {
                if(Core.econ.getBalance(p) < 500) {
                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aDu har ikke råd til at købe dette.");
                    return;
                }
                EconomyResponse r = Core.econ.withdrawPlayer(p, 500);
                if(r.transactionSuccess()) {
                    p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
                    p.sendMessage("§8[§3DarkStarPvP§8] §aDu har købt §c10 §aTeam Points til dit Team.");
                    teamManager.giveTeamPoints(team, 10);
                    for(String members : teamManager.getTeamMembers(team)) {
                        Player member = Bukkit.getPlayer(members);
                        if(member != null && member != p) {
                            member.sendMessage("§8[§3DarkStarPvP§8] §aEn spiller fra dit Team har købt §c10 Team Points §atil dit Team §8(§6" + p.getName() + "§8)");
                        }
                    }
                }
            }
        }
    }
}
