package dev.lauchy.dragonsimulatorextras;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand implements CommandExecutor {

    public abstract String[] getExecutables();
    public abstract CommandRestriction getRestriction();
    public abstract String getPermission();

    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public enum CommandRestriction {
        IN_GAME,
        CONSOLE,
        BOTH
    }

}