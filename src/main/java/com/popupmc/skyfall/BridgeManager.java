package com.popupmc.skyfall;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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

        if(hasImmunity(entity.getUniqueId()))
            return false;

        boolean ret = false;

        for(Bridge bridge : bridges) {
            ret = bridge.attemptTeleport(entity);
            if(ret) {
                grantInitialImmunity(entity.getUniqueId());
                break;
            }
        }

        return ret;
    }

    // Revokes teleport immunity
    public static void revokeImmuntiy(UUID uuid) {
        if(!hasImmunity(uuid))
            return;

        teleportImmunity.remove(uuid);
    }

    // Grants indefinite immunity, used when switching worlds
    public static void grantInitialImmunity(UUID uuid) {
        revokeImmuntiy(uuid);
        teleportImmunity.put(uuid, null);
    }

    // Grants temporary immuntiy, used after switching worlds
    public static void grantTimedImmunity(UUID uuid) {
        revokeImmuntiy(uuid);

        teleportImmunity.put(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                teleportImmunity.remove(uuid);
            }
        }.runTaskLater(SkyFall.plugin, 20));
    }

    // Detects immunity
    public static boolean hasImmunity(UUID uuid) {
        return teleportImmunity.containsKey(uuid);
    }

    ArrayList<Bridge> bridges = new ArrayList<>();

    // To try and prevent some kind of weird bug where players are teleported to the right world
    // and then the wrong world rapidly
    // EDIT: It doesnt do crap, the bug is still there, idk at this point
    // EDIT: found the bug, it was teleportAsync.... dont do teleportAsync, it does really weird things
    public static HashMap<UUID, BukkitTask> teleportImmunity = new HashMap<>();

    // Code readability
    public final static int netherCeiling = 127;
    public final static int mainFloor = 0;
    public final static int mainCeiling = 256;
    public final static int endFloor = 0;
}
