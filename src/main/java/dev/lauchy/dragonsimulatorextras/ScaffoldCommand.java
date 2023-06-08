package dev.lauchy.dragonsimulatorextras;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
public class ScaffoldCommand implements CommandExecutor, Listener {
    private ArrayList<Player> scaffold = new ArrayList<>();
    private Material block = Material.WOOL;
    private Main main;
    public ScaffoldCommand(Main main) {
        this.main = main;
    }
    private void Scaffold(Player player){
        setBlockUnderPlayer(player, block);
    }
    public void setBlockUnderPlayer(Player player, Material material) {
        player.getWorld().getBlockAt(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ())).setType(material);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Only players can do this!");
            return true;
        }
        // Permission check
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("scaffold")) {
            if (!player.hasPermission("seabot.basic.admin")) {
                player.sendMessage(Utils.c("&CYou must be admin or higher to execute this command."));
                return true;
            }
            if (args.length == 0) {
                // Enabling scaffold
                if (!scaffold.contains(player)) {
                    scaffold.add(player);
                    player.sendMessage(Utils.c("&aScaffolding has been enabled"));
                } else {
                    // Disabling scaffold
                    scaffold.remove(player);
                    player.sendMessage(Utils.c("&cScaffolding has been disabled"));
                }
            } else if (args.length == 1) {
                // Telling player their argument is not an actual material
                if (!isBlock(args[0].toUpperCase())) {
                    player.sendMessage(Utils.c("&cInvalid block type: " + args[0].toUpperCase()));
                    return true;
                } else {
                    // Changing scaffold block
                    block = Material.getMaterial(args[0].toUpperCase());
                    player.sendMessage(Utils.c("&aScaffold material has been set to '" + args[0] + "&a'"));
                    return true;
                }
            } else if (args.length > 1) {
                player.sendMessage(Utils.c("Usage: /scaffold [material]"));
            }
            // The runnable to set the block under the player (aka scaffold)
            if (scaffold.contains(player)) {
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (scaffold.contains(player)) {
                            setBlockUnderPlayer(player, block);
                        }
                    }
                };
                bukkitRunnable.runTaskTimer(main, 0, 1);
                return true;
            }
        }
        return true;
    }
    // Check if the argument provided is actually a material
    private boolean isBlock(String material) {
        try {
            boolean i = Material.getMaterial(material.toUpperCase()).isBlock();
            return i;
        } catch (NullPointerException exc) {
            return false;
        }
    }
}