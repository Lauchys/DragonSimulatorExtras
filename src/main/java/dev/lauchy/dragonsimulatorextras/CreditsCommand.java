package dev.lauchy.dragonsimulatorextras;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class CreditsCommand extends AbstractCommand {
    private final Main main;
    public CreditsCommand(Main main){
        this.main = main;
    }
    @Override
    public String[] getExecutables() {
        return new String[] {
                "credits"
        };
    }
    @Override
    public CommandRestriction getRestriction() {
        return CommandRestriction.BOTH;
    }
    @Override
    public String getPermission() {
        return "seabot.basic.admin";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Utils.c("&cUsage: /credits <player>"));
            return true;
        }
        if (args.length == 1) {
            // Sends to all online players
            if (args[0].equalsIgnoreCase("-a")) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(Utils.c("&eAn &cAdmin &eis messing with the game settings..."));
                    player.sendMessage(Utils.c("&aSuccess! &2All online players &ahave been sent to the end credits"));
                    Utils.sendCredits(online);
                    return true;
                }
            }
            Player target = main.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player doesn't exist!");
                return true;
            }
            target.sendMessage(Utils.c("&eAn &cAdmin &eis messing with the game settings..."));
            player.sendMessage(Utils.c("&aSuccess! &2" + target.getName() + " &ahas been sent to the end credits"));
            Utils.sendCredits(target);
            return true;
        }
        return true;
    }
}