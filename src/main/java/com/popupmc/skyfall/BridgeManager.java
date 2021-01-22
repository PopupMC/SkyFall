package com.popupmc.skyfall;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Entity;
import java.util.ArrayList;
import java.util.Objects;

public class BridgeManager {

    public BridgeManager() {
        // Nether Ceiling => Main Floor
        bridges.add(
                new Bridge(
                        Objects.requireNonNull(Bukkit.getWorld("main_nether")),
                        Objects.requireNonNull(Bukkit.getWorld("main")),
                        netherCeiling,
                        mainFloor,
                        Material.STONE,
                        false));

        // Main Floor => Nether Ceiling
        bridges.add(
                new Bridge(
                        Objects.requireNonNull(Bukkit.getWorld("main")),
                        Objects.requireNonNull(Bukkit.getWorld("main_nether")),
                        mainFloor,
                        netherCeiling,
                        Material.NETHERRACK,
                        true));

        // Main Ceiling => End Floor
        bridges.add(
                new Bridge(
                        Objects.requireNonNull(Bukkit.getWorld("main")),
                        Objects.requireNonNull(Bukkit.getWorld("main_the_end")),
                        mainCeiling,
                        endFloor,
                        Material.END_STONE,
                        false));

        // End Floor => Main Ceiling
        bridges.add(
                new Bridge(
                        Objects.requireNonNull(Bukkit.getWorld("main_the_end")),
                        Objects.requireNonNull(Bukkit.getWorld("main")),
                        endFloor,
                        mainCeiling,
                        Material.STONE,
                        true));
    }

    // Attempt to teleport player
    public boolean attemptTeleport(Entity entity) {

        boolean ret = false;

        for(Bridge bridge : bridges) {
            ret = bridge.attemptTeleport(entity);
            if(ret)
                break;
        }

        return ret;
    }

    ArrayList<Bridge> bridges = new ArrayList<>();

    // Code readability
    public final static int netherCeiling = 127;
    public final static int mainFloor = 0;
    public final static int mainCeiling = 256;
    public final static int endFloor = 0;
}
