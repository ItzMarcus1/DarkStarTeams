package me.itzmarcus.teams.Utils;

import me.itzmarcus.teams.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcus on 22-06-2016.
 */
public class Team {

    static Core plugin;
    public Team(Core instance) {
        plugin = instance;
    }

    public static ArrayList<String> teams = new ArrayList<>();
    public static HashMap<String, List<String>> teamMembers = new HashMap<>();
    public static HashMap<String, String> teamLeaders = new HashMap<>();
    public static HashMap<String, Integer> teamKills = new HashMap<>();
    public static HashMap<String, Integer> teamDeaths = new HashMap<>();
    public static HashMap<String, Integer> teamPoints = new HashMap<>();

    public void createTeam(String name, Player creator) {
        if(name.toLowerCase().contains("ggpvp")
                || name.toLowerCase().contains("quanity")
                || name.toLowerCase().contains("freakyville")
                || name.toLowerCase().contains("nations")
                || name.toLowerCase().contains("mecraft")
                || name.toLowerCase().contains("ragepvp")
                || name.toLowerCase().contains("strandet")
                || name.toLowerCase().contains("everpvp")) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aDet er ikke tilladt at reklamere med Team Navne!");
            for(Player staff : Bukkit.getOnlinePlayers()) {
                if(staff.hasPermission("staffchat.message")) {
                    staff.sendMessage("§c§l[STAFF] §aConsole §b(Admin) §8» §2§l" + creator.getName() + " §a§lhar prøvet at lave et team med navnet: §2§l" + name);
                }
            }
            return;
        }
        if(name.length() < 3 || name.length() > 13) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aDit Team navn skal være mere end 3 bogstaver og mindre end 13 bogstaver.");
            return;
        }
        if(name.contains("'") || name.contains("`") || name.contains(",") || name.contains("^") || name.contains(".") || name.contains("!") || name.contains("<") || name.contains(">") || name.contains("_") || name.contains("#") || name.contains("¤") || name.contains("%") || name.contains("&") || name.contains("/") || name.contains("(") || name.contains(")") || name.contains("=") || name.contains("?")) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aDit Team navn må ikke indeholde specielle bogstaver.");
            return;
        }
        if(checkIfPlayerHasATeam(creator.getName())) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aDu skal forlade dit nuværende team. §8(§6/team forlad§8)");
            return;
        }
        if(teams.contains(name)) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aNavnet er allerede taget.");
            return;
        }
        if(name.equalsIgnoreCase("null")) {
            creator.sendMessage("§8[§3DarkStarPvP§8] §aDit Team navn kan ikke være 'null'");
            return;
        }
        String player = creator.getName();
        String teamName = name;
        List<String> members = new ArrayList<String>();

        teams.add(teamName);
        teamMembers.put(teamName, members);
        teamLeaders.put(teamName, creator.getName());
        teamKills.put(teamName, 0);
        teamDeaths.put(teamName, 0);
        teamPoints.put(teamName, 0);
        Bukkit.getServer().broadcastMessage("§8[§3DarkStarPvP§8] §c" + creator.getName() + " §ahar lavet et nyt Team §8(§6" + teamName + "§8)");
        saveTeams();
    }

    public void addMember(String team, String member) {
        String t = team;
        String player = member;
        List<String> newMembers = teamMembers.get(t);
        newMembers.add(player);
        teamMembers.put(t, newMembers);
        saveTeams();
    }

    public void kickMember(String team, String member) {
        String t = team;
        String player = member;
        List<String> newMembers = teamMembers.get(t);
        newMembers.remove(player);
        teamMembers.put(t, newMembers);
        Bukkit.getServer().getPlayer(player).sendMessage("§8[§3DarkStarPvP§8] §aDu er blevet smidt ud af fra dit Team §8(§6" + t + "§8)");
        saveTeams();
    }

    public static void saveTeams() {
        for(String t : teams) {
            // Bukkit.getServer().broadcastMessage("Saving teams..."); // For debugging
            // Bukkit.getServer().broadcastMessage(t + ", members: " + teamMembers.get(t) + ", leader: " + teamLeaders.get(t)); // For debugging
            plugin.getConfig().set("teams." + t, t);
            plugin.getConfig().set("teams." + t + ".members", teamMembers.get(t));
            plugin.getConfig().set("teams." + t + ".kills", teamKills.get(t));
            plugin.getConfig().set("teams." + t + ".deaths", teamDeaths.get(t));
            plugin.getConfig().set("teams." + t + ".leader", teamLeaders.get(t));
            plugin.getConfig().set("teams." + t + ".points", teamPoints.get(t));
            plugin.saveConfig();
        }
    }

    public int getTeamKills(String team) {
        return teamKills.get(team);
    }

    public int getTeamPoints(String team) {
        return teamPoints.get(team);
    }

    public void giveTeamPoints(String team, int amount) {
        teamPoints.put(team, getTeamPoints(team) + amount);
    }

    public void removeTeamPoints(String team, int amount) {
        if(amount > getTeamPoints(team)) {
            teamPoints.put(team, 0);
            return;
        }
        teamPoints.put(team, getTeamPoints(team) - amount);
    }

    public int getTeamDeaths(String team) {
        return teamDeaths.get(team);
    }

    public double getTeamDeathsKDR(String team) {
        return teamDeaths.get(team);
    }

    public double getTeamKDR(String team) {
        double kd = getTeamKills(team) / getTeamDeathsKDR(team);
        //kd = Math.round(kd * 100);
        //kd = kd/100;
        return kd;
    }
        // Line 59
    public String getPlayerTeam(String player) {
        String teamName = "";
            for (String s : teams) {
                if (teamMembers.get(s).contains(player)) {
                    teamName = s;
                }
                if (getTeamLeader(s) != null && getTeamLeader(s).contains(player)) { // line 153
                    teamName = s;
                }
            }
            return teamName; // Line 67
    }

    public String getPlayerPrefixTeam(String player) {
        for(String s : teams) {
            if(checkIfPlayerHasATeam(player) && getTeamMembers(s).contains(player)) {
                if(getTeamKills(getPlayerTeam(player)) >= 2500) {
                    return "§6" + s + "§r";
                } else {
                    return "§3" + s + "§r";
                }
            } else if(checkIfPlayerHasATeam(player) && getTeamLeader(s).equals(player)) {
                if(getTeamKills(getPlayerTeam(player)) >= 2500) {
                    return "§6*" + s + "§r";
                } else {
                    return "§3*" + s + "§r";
                }
            }
        }
        return ""; // Line 67
    }

    public String getTeamLeader(String team) {
        return teamLeaders.get(team);
    }

    public List<String> getTeamMembers(String team) {
        return teamMembers.get(team);
    }

    public void loadTeams() {
        for(String s : plugin.getConfig().getConfigurationSection("teams").getKeys(false)) {
            teams.add(s);
            teamMembers.put(s, plugin.getConfig().getStringList("teams." + s + ".members"));
            teamLeaders.put(s, plugin.getConfig().getString("teams." + s + ".leader"));
            teamKills.put(s, plugin.getConfig().getInt("teams." + s + ".kills"));
            teamDeaths.put(s, plugin.getConfig().getInt("teams." + s + ".deaths"));
            teamPoints.put(s, plugin.getConfig().getInt("teams." + s + ".points"));
        }
    }

    public void increaseTeamKills(String team) {
        teamKills.put(team, teamKills.get(team) + 1);
    }

    public void increaseTeamDeaths(String team) {
        teamDeaths.put(team, teamDeaths.get(team) + 1);
    }

    public void removeTeam(String team) {
        if(teams.contains(team)) {
            teams.remove(team);
            teamLeaders.remove(team);
            teamMembers.remove(team);
            teamKills.remove(team);
            teamDeaths.remove(team);
            teamPoints.remove(team);
            plugin.getConfig().set("teams." + team, null);
            plugin.saveConfig();
        }
    }

    public void leaveTeam(String team, String member) {
        teamMembers.get(team).remove(member);
        saveTeams();
    }

    public boolean checkIfPlayerHasATeam(String player) {
        if(teams.size() == 0) {
            return false;
        }
        for(String s : teams) {
            if (teamMembers.get(s).contains(player) || teamLeaders.get(s).contains(player)) {
                return true;
            }
        }
        return false;
    }
}
