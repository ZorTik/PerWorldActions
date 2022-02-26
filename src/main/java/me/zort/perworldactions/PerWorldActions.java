package me.zort.perworldactions;

import me.zort.perworldactions.configuration.Configuration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class PerWorldActions extends JavaPlugin {

    public static PerWorldActions INSTANCE = null;

    public Configuration configuration;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.configuration = new Configuration(this);
        getServer().getPluginManager().registerEvents(new PerWorldActionsListener(this), this);
        getCommand("perworldactions").setExecutor(new GeneralCommandExecutor());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

}
