package dev.lauchy.dragonsimulatorextras;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import static org.bukkit.Bukkit.getServer;
public class TogglePickupCommand extends AbstractCommand implements Listener {
    private boolean pickup;
    private final Main main;

    public TogglePickupCommand(Main main) {
        this.main = main;
    }

    @Override
    public String[] getExecutables() {
        return new String[]{
                "tpu",
                "togglepickup",
                "tpickup",
                "pickup"
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
        if (!sender.hasPermission("seabot.basic.admin")) {
            sender.sendMessage(Utils.c("&CYou must be admin or higher to execute this command."));
            return true;
        }
        Player player = (Player) sender;
        // Pickup enabled
        if (pickup) {
            sender.sendMessage(Utils.c("&aItem pickup enabled"));
            Bukkit.broadcastMessage(Utils.c("&c[ADMIN] " + player.getDisplayName() + " &ehas &aenabled &epicking up items."));
            pickup = false;
            return true;
        }
        // Pickup disabled
        if (!pickup) {
            sender.sendMessage(Utils.c("&cItem pickup disabled"));
            Bukkit.broadcastMessage(Utils.c("&c[ADMIN] " + player.getDisplayName() + " &ehas &cdisabled &epicking up items."));
            pickup = true;
            return true;
        }
        return true;
    }

    // Disabling item pickup
    @EventHandler
    public void pickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("seabot.basic.admin")) {
            return;
        }
        if (pickup) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void drop(PlayerItemBreakEvent e) {
        // Setting a broken item to air
        if (pickup) {
            e.getBrokenItem().setAmount(0);
            e.getBrokenItem().setType(Material.AIR);
        }
    }
}