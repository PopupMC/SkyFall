package com.popupmc.skyfall;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class DoTeleport {
    public static boolean doTeleport(Entity entity) {
        World world = entity.getWorld();
        Location loc = entity.getLocation();

        // If in the nether above the ceiling, teleport to main world of same location
        if(world.getName().equalsIgnoreCase(SkyFall.netherWorldName) && entity.getLocation().getY() > (netherCeiling - playerHeight)) {
            //entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.mainWorldName), mainFloor + playerHeight, true, true));
            entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.mainWorldName), mainFloor + playerHeight));
            return true;
        }

        // If in the main world below floor, teleport to nether of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && entity.getLocation().getY() < (mainFloor + playerHeight)) {
            //entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.netherWorldName), netherCeiling - playerHeight, true, false));
            entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.netherWorldName), netherCeiling - playerHeight));
            return true;
        }

        // If in the main world above ceiling, teleport to end of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && entity.getLocation().getY() > (mainCeiling - playerHeight)) {
            entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.endWorldName), endFloor + playerHeight));
            return true;
        }

        // If in the end world below floor, teleport to main of same location
        else if(world.getName().equalsIgnoreCase(SkyFall.endWorldName) && entity.getLocation().getY() < (endFloor + playerHeight)) {
            entity.teleport(changeLoc(loc, Bukkit.getWorld(SkyFall.mainWorldName), mainCeiling - playerHeight));
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

    // Code readability
    public final static int netherCeiling = 127;
    public final static int mainFloor = 0;
    public final static int mainCeiling = 256;
    public final static int endFloor = 0;
    public final static int netherScale = 8;
    public final static int playerHeight = 2;
}
