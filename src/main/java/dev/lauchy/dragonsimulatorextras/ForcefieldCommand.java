package dev.lauchy.dragonsimulatorextras;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.*;
import java.util.List;
import static org.bukkit.Bukkit.getServer;
public class ForcefieldCommand implements CommandExecutor, Runnable, Listener {
    private Main main;
    public ForcefieldCommand(Main main) {
        this.main = main;
        getServer().getScheduler().scheduleSyncRepeatingTask(main, this, 1, 5);
        getServer().getPluginManager().registerEvents(this, main);
    }
    public HashMap<UUID, Integer> forcefield = new HashMap<UUID, Integer>();
    private final HashMap<UUID, BukkitRunnable> runnableHashMap = new HashMap<>();
    // Turn off forcefield on log out
    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        UUID id = event.getPlayer().getUniqueId();
        if (runnableHashMap.containsKey(id)){
            cancelRunnable(id);
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("forcefield")) {
            Player p = (Player) commandSender;
            UUID uuid = p.getUniqueId();
            if (!p.hasPermission("seabot.basic.forcefield") || !p.hasPermission("seabot.basic.admin")) {
                p.sendMessage(Utils.c("&cYou must be admin or higher to execute this command."));
                return true;
            }
            if (args.length == 0) {
                if (!forcefield.containsKey(uuid)) {
                    forcefield.put(uuid, getDistance(uuid));
                    run();
                    p.sendMessage(Utils.c("&aForcefield enabled"));
                    /**
                     * A whole bunch of particle math from
                     * {@link seabot.plugin.core.abilities.misc.SpaceHelmet}
                     **/
                    BukkitRunnable particleRunnable = new BukkitRunnable() {
                        int degrees = 0;
                        @Override
                        public void run() {
                            Location loc = p.getLocation();
                            List<Location> particleLocs = new ArrayList<>();
                            // First Particle
                            double radians = Math.toRadians(degrees);
                            float radius = 2f;
                            double x = radius * Math.cos(radians);
                            double z = radius * Math.sin(radians);
                            particleLocs.add(loc.clone().add(x, 1, z));
                            // Second Particle
                            double newDegree = degrees += 180;
                            if(newDegree > 360) degrees -= 360;
                            double radians2 = Math.toRadians(newDegree);
                            double x2 = radius * Math.cos(radians2);
                            double z2 = radius * Math.sin(radians2);
                            particleLocs.add( loc.clone().add(x2, 1, z2));
                            for(Location l :  particleLocs){
                                new ParticleBuilder(ParticleEffect.REDSTONE, l)
                                        .setColor(new Color(50, 30, 255))
                                        .setAmount(10)
                                        .display();
                            }
                            degrees += 10;
                            if(degrees >= 360) degrees = 0;
                        }
                    };
                    particleRunnable.runTaskTimer(main, 0, 1);
                    runnableHashMap.put(uuid, particleRunnable);
                    return true;
                } else {
                    forcefield.remove(uuid);
                    p.sendMessage(Utils.c("&cForcefield disabled"));
                    cancelRunnable(uuid);
                    return true;
                }
            } else if (args.length == 1) {
                if (!isNumber(args[0])) {
                    p.sendMessage(Utils.c("&cInvalid distance"));
                    return true;
                }else{
                    p.sendMessage(Utils.c("&aForcefield distance set to " + args[0] + " &ablocks."));
                    setDistance(uuid, Integer.parseInt(args[0]));
                    return true;
                }
            } else if (args.length > 1) {
                p.sendMessage(Utils.c("&cUsage: /forcefield <distance>"));
            }
        }
        return true;
    }
    /**
     * Whole bunch of checks and work
     * to make forcefield give oomph
     */
    public void run () {
        for (Player player : getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            if (forcefield.containsKey(uuid)) {
                for (Player other : getServer().getOnlinePlayers()) {
                    if (player.equals(other)) continue;
                    if (offset(other, player) > getDistance(uuid)) continue;
                    if (other.getGameMode() == GameMode.SPECTATOR) return;
                    if (other.hasPermission("seabot.basic.forcefield")) return;
                    Entity bottom = other;
                    while (bottom.getVehicle() != null) bottom = bottom.getVehicle();
                    // Set oomph distance
                    velocity(bottom, getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10);
                    // Set oomph sound
                    other.getWorld().playSound(other.getLocation(), Sound.GHAST_FIREBALL, 1F, 1F);
                }
            }
        }
    }
    public double offset(Entity a, Entity b) {
        return a.getLocation().toVector().subtract(b.getLocation().toVector()).length();
    }
    public Vector getTrajectory2d(Entity from, Entity to){
        return to.getLocation().toVector().subtract(from.getLocation().toVector()).setY(0).normalize();
    }
    public void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax){
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0)
            return;
        if (ySet)
            vec.setY(yBase);
        vec.normalize();
        vec.multiply(str);
        vec.setY(vec.getY() + yAdd);
        if (vec.getY() > yMax)
            vec.setY(yMax);
        ent.setFallDistance(0);
        ent.setVelocity(vec);
    }
    public void cancelRunnable(UUID uuid) {
        if(runnableHashMap.containsKey(uuid)) {
            BukkitRunnable runnable = runnableHashMap.get(uuid);
            if(runnable != null) runnable.cancel();// Cancel the runnable
            runnableHashMap.remove(uuid); // Remove the runnable from the hashmap
        }
    }
    public void setDistance(UUID player, int distance) {
        forcefield.put(player, distance);
    }
    public int getDistance(UUID player) {
        return forcefield.getOrDefault(player, 3);
    }
    private boolean isNumber(String str) {
        try {
            int i = Integer.parseInt(str);
            if (i < 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException exc) {
            return false;
        }
    }
}