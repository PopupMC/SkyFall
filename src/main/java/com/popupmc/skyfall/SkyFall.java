package com.popupmc.skyfall;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyFall extends JavaPlugin {

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getPluginManager().registerEvents(new OnEntityDamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockDamageEvent(), this);

        getLogger().info("SkyFall is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyFall is disabled");
    }

    static JavaPlugin plugin;

    public static final String mainWorldName = "main";
    public static final String endWorldName = "main_the_end";
    public static final String netherWorldName = "main_nether";
}
