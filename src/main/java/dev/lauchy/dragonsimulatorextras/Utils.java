package dev.lauchy.dragonsimulatorextras;

import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static HashMap<String, String> SYMBOLS = new HashMap<String, String>(){{
        put(":health:", "❤");
        put(":damage:", "❁");
        put(":strength:", "❁");
        put(":defense:", "❈");
        put(":defence:", "❈");
        put(":true_defense:", "❂");
        put(":speed:", "✦");
        put(":intelligence:", "✎");
        put(":crit_chance:", "☣");
        put(":crit_damage:", "☠");
        put(":attack_speed:", "⚔");
        put(":magic_find:", "✯");
        put(":ability_damage:", "๑");
        put(":slayer:", "☠");
        put(":level_req:", "❣");
        put(":maddox:", "✆");
        put(":right:", "🡆");
        put(":vitality", "♨");
    }};
    public static String c(String message) {
        for(Map.Entry<String, String> s : SYMBOLS.entrySet()) {
            message = message.replaceAll(s.getKey(), s.getValue());
        }
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    /** @return minecraft game completion credits **/
    public static void sendCredits(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 1));
    }
    /** @return minecraft demo screen **/
    public static void sendDemo(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 0));
    }
    // Sends a guardian effect to a player
    public static void sendGuardian(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(10, 0));
    }
}
