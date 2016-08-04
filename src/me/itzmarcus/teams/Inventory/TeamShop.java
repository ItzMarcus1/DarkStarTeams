package me.itzmarcus.teams.Inventory;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marcus on 04-07-2016.
 */
public class TeamShop {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));

    public ItemStack newItem(Material material, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newItem(Material material, int data, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material, 1, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void openTeamShop(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Team Shop");

        inv.setItem(21, newItem(Material.DIAMOND, "§6Team Points", Arrays.asList("§7Dit Team har §a" + teamManager.getTeamPoints(teamManager.getPlayerTeam(player.getName())) + " §7Team Points.")));

        if(Core.econ.getBalance(player) <= 500) {
            inv.setItem(23, newItem(Material.STAINED_GLASS_PANE, 14, "§6Køb Points", Arrays.asList("§7Klik her for at købe §a10 §7Team Points.", "", "§7Pris: §c500$")));
        } else {
            inv.setItem(23, newItem(Material.STAINED_GLASS_PANE, 5, "§6Køb Points", Arrays.asList("§7Klik her for at købe §a10 §7Team Points.", "", "§7Pris: §a500$")));
        }

        inv.setItem(3, newItem(Material.POTION, 8194, "§bTeam Speed", Arrays.asList("§7Giv alle online Team Members", "§cSpeed I §7i 2 minutter.", "", "§7Pris: §c10 §7Team Points")));
        inv.setItem(4, newItem(Material.POTION, 8193, "§bTeam Regen", Arrays.asList("§7Giv alle online Team Members", "§cRegeneration II §7i 1 minut.", "", "§7Pris: §c15 §7Team Points")));
        inv.setItem(5, newItem(Material.POTION, 8195, "§bTeam Fire", Arrays.asList("§7Giv alle online Team Members", "§cFire Resistance I §7i 2 minutter.", "", "§7Pris: §c10 §7Team Points")));

        inv.setItem(22, newItem(Material.BARRIER, "§cTilbage", Arrays.asList("§7Gå tilbage til hovedmenuen.")));
        player.openInventory(inv);
    }
}
