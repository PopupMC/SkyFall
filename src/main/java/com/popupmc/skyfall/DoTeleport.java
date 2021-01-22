package com.popupmc.skyfall;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DoTeleport {
    public static boolean doTeleport(Entity entity) {
        World world = entity.getWorld();
        Location loc = entity.getLocation();
        double entityHeight = entity.getHeight();

        // If in the nether above the ceiling, teleport to main world of same location
        if(world.getName().equalsIgnoreCase(SkyFall.netherWorldName) && entity.getLocation().getY() > netherCeiling) {

            // Check for sufficient air before teleporting player
            for(int i = 0; i < entityHeight; i++) {
                Block block = Bukkit.getWorld(SkyFall.mainWorldName).getBlockAt(loc.getBlockX(), mainFloor + i + 1, loc.getBlockZ());
                if(block.getType() != Material.AIR &&
                        block.getType() != Material.VOID_AIR &&
                        block.getType() != Material.CAVE_AIR)
                    block.setType(Material.AIR);
            }

            // See if this is a player
            if(entity instanceof Player) {
                Player p = (Player)entity;

                // See if the player is not gliding with an elytra or flying
                if(!p.isGliding() && !p.isFlying() && Bukkit.getWorld(SkyFall.mainWorldName) != null) {

                    // Get block we're going to teleport player to
                    Block block = Bukkit.getWorld(SkyFall.mainWorldName).getBlockAt(loc.getBlockX(), mainFloor, loc.getBlockZ());

                    // If it's an air block add something there
                    if(block.getType() == Material.AIR ||
                            block.getType() == Material.VOID_AIR ||
                            block.getType() == Material.CAVE_AIR)
                        block.setType(Material.BEDROCK);
                }
            }

            doTeleport(entity, changeLoc(loc, Bukkit.getWorld(SkyFall.mainWorldName), mainFloor + entityHeight));
            return true;
        }

        // If in the main world below floor, teleport to nether of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && entity.getLocation().getY() < mainFloor) {

            // Check for sufficient air before teleporting player
            for(int i = 0; i < entityHeight; i++) {
                Block block = Bukkit.getWorld(SkyFall.netherWorldName).getBlockAt(loc.getBlockX(), netherCeiling - i - 1, loc.getBlockZ());
                if(block.getType() != Material.AIR &&
                        block.getType() != Material.VOID_AIR &&
                        block.getType() != Material.CAVE_AIR)
                    block.setType(Material.AIR);
            }

            // Destroy block above players location if there
            Block block = Bukkit.getWorld(SkyFall.netherWorldName).getBlockAt(loc.getBlockX(), netherCeiling, loc.getBlockZ());
            block.setType(Material.AIR);

            doTeleport(entity, changeLoc(loc, Bukkit.getWorld(SkyFall.netherWorldName), netherCeiling - entityHeight));
            return true;
        }

        // If in the main world above ceiling, teleport to end of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && entity.getLocation().getY() > mainCeiling) {

            // Check for sufficient air before teleporting player
            for(int i = 0; i < entityHeight; i++) {
                Block block = Bukkit.getWorld(SkyFall.endWorldName).getBlockAt(loc.getBlockX(), endFloor + i + 1, loc.getBlockZ());
                if(block.getType() != Material.AIR &&
                        block.getType() != Material.VOID_AIR &&
                        block.getType() != Material.CAVE_AIR)
                    block.setType(Material.AIR);
            }

            // See if this is a player
            if(entity instanceof Player) {
                Player p = (Player)entity;

                // See if the player is not gliding with an elytra or flying
                if(!p.isGliding() && !p.isFlying() && Bukkit.getWorld(SkyFall.endWorldName) != null) {

                    // Get block we're going to teleport player to
                    Block block = Bukkit.getWorld(SkyFall.endWorldName).getBlockAt(loc.getBlockX(), endFloor, loc.getBlockZ());

                    // If it's an air block add something there
                    if(block.getType() == Material.AIR ||
                            block.getType() == Material.VOID_AIR ||
                            block.getType() == Material.CAVE_AIR)
                        block.setType(Material.END_STONE);
                }
            }

            doTeleport(entity, changeLoc(loc, Bukkit.getWorld(SkyFall.endWorldName), endFloor + entityHeight));
            return true;
        }

        // If in the end world below floor, teleport to main of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.endWorldName) && entity.getLocation().getY() < endFloor) {

            // Check for sufficient air before teleporting player
            for(int i = 0; i < entityHeight; i++) {
                Block block = Bukkit.getWorld(SkyFall.mainWorldName).getBlockAt(loc.getBlockX(), mainCeiling - i - 1, loc.getBlockZ());
                if(block.getType() != Material.AIR &&
                        block.getType() != Material.VOID_AIR &&
                        block.getType() != Material.CAVE_AIR)
                    block.setType(Material.AIR);
            }

            doTeleport(entity, changeLoc(loc, Bukkit.getWorld(SkyFall.mainWorldName), mainCeiling - entityHeight));
            return true;
        }

        return false;
    }

    public static Location changeLoc(Location loc, World world, double y, boolean nether, boolean netherScaleUp) {
        Location newLoc = loc.clone();

        if(nether && netherScaleUp)
            newLoc.setX(loc.getX() * netherScale);
        else if(nether)
            newLoc.setX(loc.getX() / netherScale);

        newLoc.setY(y);

        if(nether && netherScaleUp)
            newLoc.setX(loc.getZ() * netherScale);
        else if(nether)
            newLoc.setX(loc.getZ() / netherScale);

        newLoc.setWorld(world);

        return newLoc;
    }

    public static Location changeLoc(Location loc, World world, double y) {
        return changeLoc(loc, world, y, false, false);
    }

    public static void doTeleport(Entity e, Location loc) {

        // Clone velocity
        Vector vec = e.getVelocity().clone();

        // Get new world
        World world = loc.getWorld();

        // Load chunk async
        world.getChunkAtAsync(loc).thenRun(() -> {
            // Teleport player to newly loaded chunk
            e.teleport(loc);

            // Re-give correct velocity
            e.setVelocity(vec);
        });
    }

    // Code readability
    public final static int netherCeiling = 127;
    public final static int mainFloor = 0;
    public final static int mainCeiling = 256;
    public final static int endFloor = 0;
    public final static int netherScale = 8;
}
