package me.itzmarcus.teams.Inventory;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marcus on 24-06-2016.
 */
public class InventoryManager implements Listener {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));
    TeamShop teamShop = new TeamShop();

    public ItemStack newItem(Material material, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newSkullItem(String player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(player);
        skullMeta.setDisplayName("§e" + player);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public ItemStack newItem(Material material, int data, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material, 1, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /*
    , "§3Kills: §6" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName()))
                    , "§3Deaths: §6" + teamManager.getTeamDeaths(teamManager.getPlayerTeam(player.getName()))
                    , "§3KDR: §6" + String.format("%.2f", teamManager.getTeamKDR(teamManager.getPlayerTeam(player.getName())))
     */

    public void openMembersInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Team Members");

        int slotCounter = 20;
        for(int i=0;i<10;i++) {
            if(slotCounter == 25) { slotCounter = 29; }
            inv.setItem(slotCounter, newItem(Material.STAINED_GLASS_PANE, 14, "§cIngen Member", Arrays.asList("§7Invite spillere med §a/team invite <navn>")));
            slotCounter++;
        }
        slotCounter = 20;
        for(String s : teamManager.getTeamMembers(teamManager.getPlayerTeam(player.getName()))) {
            if(slotCounter == 25) { slotCounter = 29; }
            inv.setItem(slotCounter, newSkullItem(s));
            slotCounter++;
        }

        ItemStack leader = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta skullMeta = (SkullMeta) leader.getItemMeta();
        skullMeta.setOwner(teamManager.getTeamLeader(teamManager.getPlayerTeam(player.getName())));
        skullMeta.setDisplayName("§6" + teamManager.getTeamLeader(teamManager.getPlayerTeam(player.getName())) + " §8(§cEjer§8)");
        leader.setItemMeta(skullMeta);

        inv.setItem(13, leader);
        inv.setItem(53, newItem(Material.BARRIER, "§cTilbage", Arrays.asList("§7Gå tilbage til hovedmenuen.")));

        player.openInventory(inv);
    }

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Team Manager");

        ItemStack mainItem = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta metaMain = mainItem.getItemMeta();
        metaMain.setDisplayName("§bDit Team");
        if(Team.teamMembers.get(teamManager.getPlayerTeam(player.getName())).size() == 0) {
            metaMain.setLore(Arrays.asList("§3Ejer:"
                    , "§7- §a" + Team.teamLeaders.get(teamManager.getPlayerTeam(player.getName()))
                    , ""
                    , "§3Members §8(§60§7/§c10§8):"
                    , "§7- §cIngen members"));
        }
        for(String s : teamManager.getTeamMembers(teamManager.getPlayerTeam(player.getName()))) {
            metaMain.setLore(Arrays.asList("§3Ejer:"
                    , "§7- §a" + Team.teamLeaders.get(teamManager.getPlayerTeam(player.getName()))
                    , ""
                    , "§3Members: §aDer er lige nu §6" + Team.teamMembers.get(teamManager.getPlayerTeam(player.getName())).size() + "§7/§c10 §amembers.", "§7Klik her for at se alle members."));
        }
        mainItem.setItemMeta(metaMain);

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 0) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §6Intet rank", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a250")));
        }
        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 250) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §61", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a500")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 500) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §62", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a750")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 750) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §63", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a1000")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 1000) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §64", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a1500")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 1500) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §65", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a2000")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 2000) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §66", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§a2500")));
        }

        if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 2500) {
            inv.setItem(29, newItem(Material.GOLDEN_APPLE, "§bTeam Rank"
                    , Arrays.asList("§3Rank: §6MAX", "", "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) + "§7/§aMAX")));
        }

        inv.setItem(21, newItem(Material.CHEST, "§bTeam Shop §8- §e§lNYHED", Arrays.asList("§7Køb fordele til hele dit Team.")));
        inv.setItem(22, mainItem);
        inv.setItem(23, newItem(Material.CHEST, "§bTeam Chest", Arrays.asList("§7Kommer Snart.")));
        inv.setItem(33, newItem(Material.BOOK, "§bHjælp"
                , Arrays.asList("§3Kommandoer:"
                , "§7- §a/team slet §8-> §7Slet dit team. §8(§6Ejer§8)"
                , "§7- §a/team forlad §8-> §7Forlad dit team."
                , "§7- §a/team invite <spiller> §8-> §7Invite en spiller til dit team."
                , "§7- §a/team kick <spiller> §8-> §7Kick en spiller fra dit team.")));
        inv.setItem(15, newItem(Material.DIAMOND_SWORD, "§3Team Stats", Arrays.asList("§3Kills: §6" + teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName()))
                , "§3Deaths: §6" + teamManager.getTeamDeaths(teamManager.getPlayerTeam(player.getName()))
                , "§3KDR: §6" + String.format("%.2f", teamManager.getTeamKDR(teamManager.getPlayerTeam(player.getName()))))));
        inv.setItem(11, newItem(Material.BOW, "§3Team Rewards", Arrays.asList("§7Se hvilke missioner dit team har klaret.", "§8(§aKlik her§8)")));

        player.openInventory(inv);
    }

    public void openRewardsInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Team Rewards");
        int kills = 100;
        int slotCounter = 13;
        int money = 125;
        int missionCounter = 1;
        for(int i=0;i<15;i++) {
            if(slotCounter == 14) { slotCounter = 20; }
            if(slotCounter == 25) { slotCounter = 29; }
            if(slotCounter == 34) { slotCounter = 39; }
            if(slotCounter == 42) { slotCounter = 49; }
            if (kills <= 1500) {
                if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) < kills) {
                    inv.setItem(slotCounter, newItem(Material.STAINED_GLASS_PANE, 14, "§cMission nr. " + missionCounter, Arrays.asList("§7Få §6" + kills + " §7team kills for at klare denne mission.", "§3Belønning: §6" + money + "$")));
                } else {
                    inv.setItem(slotCounter, newItem(Material.STAINED_GLASS_PANE, 5, "§aMission nr. " + missionCounter + " §8(§6" + kills + " kills§8)", Arrays.asList("§aDit Team har klaret denne mission.", "§3Belønning: §6" + money + "$")));
                }
                slotCounter++;
                missionCounter++;
                kills = kills + 100;
                money = money + 15;
            } else {
                kills = 100;
                money = 125;
            }
        }
        inv.setItem(53, newItem(Material.BARRIER, "§cTilbage", Arrays.asList("§7Gå tilbage til hovedmenuen.")));
        player.openInventory(inv);
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getInventory().getTitle().equals("Team Shop")) {
            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BARRIER) && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Tilbage")) {
                openInventory(p);
                return;
            }
        }
        if(e.getInventory().getTitle().equals("Team Manager") || e.getInventory().getTitle().equals("Team Rewards") || e.getInventory().getTitle().equals("Team Members")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BARRIER) && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Tilbage")) {
                openInventory(p);
                return;
            }
            if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BOW)) {
                openRewardsInventory(p);
            } else if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                openMembersInventory(p);
            } else if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.CHEST) && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Team Chest")) {
                // Open team chest
            } else if(e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.CHEST) && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Team Shop")) {
                teamShop.openTeamShop(p);
            }
        }
    }
}
