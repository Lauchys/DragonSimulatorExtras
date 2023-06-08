package dev.lauchy.dragonsimulatorextras;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("[DSE] Enabling DragonSimulatorExtras");


        initCommands();
    }

    @Override
    public void onDisable() {
        System.out.println("[DSE] Disabling DragonSimulatorExtras");
    }


    private void initCommands() {
        this.getCommand("forcefield").setExecutor(new ForcefieldCommand(this));
        this.getCommand("scaffold").setExecutor(new ScaffoldCommand(this));
        this.getCommand("togglepickup").setExecutor(new TogglePickupCommand(this));
        this.getCommand("credits").setExecutor(new CreditsCommand(this));
        this.getCommand("demo").setExecutor(new DemoCommand(this));
        this.getCommand("guardian").setExecutor(new GuardianCommand(this));
        this.getServer().getPluginManager().registerEvents(new TogglePickupCommand(this), this);
    }

    public static Main getPlugin() {
        return plugin;
    }
}


