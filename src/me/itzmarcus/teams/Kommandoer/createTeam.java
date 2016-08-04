package me.itzmarcus.teams.Kommandoer;

import me.itzmarcus.teams.Core;
import me.itzmarcus.teams.Inventory.InventoryManager;
import me.itzmarcus.teams.Utils.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by marcus on 22-06-2016.
 */
public class createTeam implements CommandExecutor {

    Team teamManager = new Team(JavaPlugin.getPlugin(Core.class));
    InventoryManager invManager = new InventoryManager();

    public static HashMap<String, String> accept = new HashMap<>();

    public String prefix = "§8[§3DarkStarPvP§8] ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Nej!");
            return true;
        }
        if(command.getName().equalsIgnoreCase("team")) {
            Player player = (Player) sender;
            if(args.length == 0) {
                if(!teamManager.checkIfPlayerHasATeam(player.getName())) {
                    player.sendMessage("§8[§3DarkStarPvP§8] §7Du er ikke i noget team. §8(§a/team create <navn>§8)");
                    return true;
                }
                invManager.openInventory(player);
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("forlad")) {
                    if(!teamManager.checkIfPlayerHasATeam(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke medlem af noget team.");
                        return true;
                    }
                    String team = teamManager.getPlayerTeam(player.getName());
                    if(team == null) {
                        player.sendMessage(prefix + "§aDu er ikke medlem af noget team.");
                        return true;
                    }else if(teamManager.getTeamLeader(teamManager.getPlayerTeam(player.getName())).equals(player.getName())) {
                        player.sendMessage(prefix + "§aDa du ejer teamet skal du skrive /team slet");
                        return true;
                    } else {
                        teamManager.leaveTeam(team, player.getName());
                        player.sendMessage(prefix + "§aDu har forladt dit team!");
                        Player teamOwner = Bukkit.getPlayer(teamManager.getTeamLeader(team));
                        if(teamOwner != null) {
                            teamOwner.sendMessage(prefix + "§aEn spiller har forladt dit Team §8(§6" + player.getName() + "§8)");
                        }
                        return true;
                    }
                }

                if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("accept")) {
                    player.sendMessage("§3§m-----§r §bTeam Hjælp §3§m-----");
                    player.sendMessage("§7- §a/team create <navn> §7- Lav dit eget team.");
                    player.sendMessage("§7- §a/team slet §7- Slet dit eget team.");
                    player.sendMessage("§7- §a/team forlad §7- Forlad dit team.");
                    player.sendMessage("§7- §a/team §7- Åben team menu.");
                    player.sendMessage("§7- §a/team invite <spiller> §7- Invite en spiller til dit team.");
                    player.sendMessage("§7- §a/team accept <spiller> §7- Accept en team invitation.");
                    player.sendMessage("§7- §a/team kick <spiller> §7- Kick en spiller fra dit team.");
                    player.sendMessage("§7- §a/team info <spiller> §7- Få information om spillerens team.");
                    player.sendMessage("§3§m-------------------------");
                }

                if(args[0].equalsIgnoreCase("slet")) {
                    if(!teamManager.checkIfPlayerHasATeam(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke medlem af noget team. §8(§6/team create <navn>§8)");
                        return true;
                    }
                    String team = teamManager.getPlayerTeam(player.getName());
                    if(teamManager.getTeamLeader(teamManager.getPlayerTeam(player.getName())).equals(player.getName())) {
                        player.sendMessage(prefix + "§aDu har slettet dit team!");
                        teamManager.removeTeam(team);
                        Bukkit.getServer().broadcastMessage("§8[§3DarkStarPvP§8] §c" + player.getName() + " §ahar slettet sit Team §8(§6" + team + "§8)");
                        return true;
                    } else {
                        player.sendMessage(prefix + "§aDu ejer ikke dette team!");
                        return true;
                    }
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("info")) {
                    if(!teamManager.checkIfPlayerHasATeam(args[1])) {
                        player.sendMessage(prefix + "§aDenne spiller er ikke medlem af et team.");
                        return true;
                    }
                    String team = teamManager.getPlayerTeam(args[1]);
                    player.sendMessage(prefix + "§3§m---§r §b" + team + " §3§m---");
                    player.sendMessage(prefix + "§3Team Kills: §a" + teamManager.getTeamKills(team));
                    player.sendMessage(prefix + "§3Team Deaths: §a" + teamManager.getTeamDeaths(team));
                    player.sendMessage(prefix + "§3Team KDR: §a" + String.format("%.2f", teamManager.getTeamKDR(teamManager.getPlayerTeam(args[1]))));
                    // Det ville være lettere at bruge en switch statement her. Ved ikke hvorfor jeg ikke har gjort det.
                    if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 2500) {
                        player.sendMessage(prefix + "§3Rank: §6MAX");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§aMAX");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 2000) {
                        player.sendMessage(prefix + "§3Rank: §66");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a2500");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 1500) {
                        player.sendMessage(prefix + "§3Rank: §65");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a2000");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 1000) {
                        player.sendMessage(prefix + "§3Rank: §64");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a1500");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 750) {
                        player.sendMessage(prefix + "§3Rank: §63");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a1000");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 500) {
                        player.sendMessage(prefix + "§3Rank: §62");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a750");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 250) {
                        player.sendMessage(prefix + "§3Rank: §61");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a500");
                    } else if(teamManager.getTeamKills(teamManager.getPlayerTeam(player.getName())) >= 0) {
                        player.sendMessage(prefix + "§3Rank: §6Intet rank");
                        player.sendMessage(prefix + "§3Næste rank: §a" + teamManager.getTeamKills(teamManager.getPlayerTeam(args[1])) + "§7/§a250");
                    }
                    player.sendMessage(prefix + "§3Ejer: §a" + teamManager.getTeamLeader(team));
                    if(teamManager.getTeamMembers(team).size() == 0) {
                        player.sendMessage(prefix + "§3Members §8(§60§7/§c10§8): §7Ingen members");
                        player.sendMessage(prefix + "§3§m------");
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                        for(String teamMembers : teamManager.getTeamMembers(team)) {
                            sb.append(teamMembers).append(" ");
                        }
                    String members = sb.toString();
                    player.sendMessage(prefix + "§3Members §8(§6" + teamManager.getTeamMembers(team).size() + "§7/§c10§8): §a" + members);
                    player.sendMessage(prefix + "§3§m------");
                }
                if(args[0].equalsIgnoreCase("create")) {
                    String teamName = args[1];
                    teamManager.createTeam(teamName, player);
                }
                if(args[0].equalsIgnoreCase("kick")) {
                    if(!teamManager.checkIfPlayerHasATeam(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke medlem af noget team.");
                        return true;
                    }
                    String team = teamManager.getPlayerTeam(player.getName());
                    if(!teamManager.getTeamLeader(team).equals(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke lederen af dette team!");
                        return true;
                    }
                    String kickedPlayer = args[1];
                    if(!teamManager.getTeamMembers(teamManager.getPlayerTeam(player.getName())).contains(kickedPlayer)) {
                        player.sendMessage(prefix + "§aDenne spiller er ikke med i dit team!");
                        return true;
                    }
                    teamManager.kickMember(teamManager.getPlayerTeam(player.getName()), kickedPlayer);
                    player.sendMessage(prefix + "§aDu har kicked §c" + kickedPlayer + " §aud af dit team!");
                }
                if(args[0].equalsIgnoreCase("accept")) {
                    String teamName = args[1];
                    if(!Team.teams.contains(teamName)) {
                        player.sendMessage(prefix + "§aDette team findes ikke.");
                        return true;
                    }
                    if(!accept.containsKey(teamName)) {
                        player.sendMessage(prefix + "§aDu har ingen invitationer.");
                        return true;
                    }
                    if(teamManager.checkIfPlayerHasATeam(player.getName())) {
                        player.sendMessage(prefix + "§aDu skal forlade dit nuværende team før du kan joine et nyt team.");
                        return true;
                    }
                    if(accept.containsKey(teamName) && accept.get(teamName).contains(player.getName())) {
                        if(teamManager.getTeamMembers(teamName).size() >= 10) {
                            player.sendMessage(prefix + "§aDer er ikke plads til flere spillere i dette Team. §8(§6" + teamManager.getTeamMembers(teamName).size() + "§7/§c10§8)");
                            return true;
                        }
                        teamManager.addMember(teamName, player.getName());
                        accept.remove(teamName, player.getName());
                        player.sendMessage(prefix + "§aDu er nu medlem af et Team §8(§6" + teamName + "§8)");
                        Player leader = Bukkit.getPlayer(teamManager.getTeamLeader(teamName));
                        leader.sendMessage(prefix + "§aEn spiller har acceptere din invitation til dit Team §8(§6" + player.getName() + "§8)");
                    }
                }
                if(args[0].equalsIgnoreCase("invite")) {
                    if(!teamManager.checkIfPlayerHasATeam(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke medlem af noget team.");
                        return true;
                    }
                    String team = teamManager.getPlayerTeam(player.getName());
                    if(!teamManager.getTeamLeader(team).equals(player.getName())) {
                        player.sendMessage(prefix + "§aDu er ikke lederen af dette team!");
                        return true;
                    }
                    String invitedPlayer = args[1];
                    if(teamManager.getTeamMembers(team).contains(invitedPlayer)) {
                        player.sendMessage(prefix + "§aDenne spiller er allerede i dit team!");
                        return true;
                    }
                    if(accept.containsKey(team) && accept.containsValue(invitedPlayer)) {
                        player.sendMessage(prefix + "§aDu har allerede sendt en invitation til denne spiller!");
                        return true;
                    }
                    if(teamManager.getTeamMembers(team).size() >= 10) {
                        player.sendMessage(prefix + "§aDer er ikke plads til flere spillere i dit Team. §8(§6" + teamManager.getTeamMembers(team).size() + "§7/§c10§8)");
                        return true;
                    }
                    accept.put(teamManager.getPlayerTeam(player.getName()), invitedPlayer);
                    player.sendMessage(prefix + "§aDu har sendt en invitation til: §c" + invitedPlayer);
                    Player invitedP = Bukkit.getPlayer(invitedPlayer);
                    if(invitedP != null) {
                        invitedP.sendMessage(prefix + "§c" + player.getName() + " §ahar sendt dig en invitation til §c" + teamManager.getPlayerTeam(player.getName()) + " §8(§6/team accept " + teamManager.getPlayerTeam(player.getName()) + "§8)");
                    }
                    int taskRemover = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Core.class), new Runnable() {
                        @Override
                        public void run() {
                            if(accept.get(teamManager.getPlayerTeam(player.getName())).contains(invitedPlayer)) {
                                accept.remove(invitedPlayer);
                                if (invitedP != null) {
                                    invitedP.sendMessage(prefix + "§aDin invitation til §c" + teamManager.getPlayerTeam(player.getName()) + " er udløbet!");
                                }
                            }
                        }
                    }, 20 * 30);
                }
            }
        }
        return false;
    }
}
