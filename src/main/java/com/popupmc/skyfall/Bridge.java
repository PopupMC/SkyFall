package com.popupmc.skyfall;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

// Represents a 1-way bridge between worlds
public class Bridge {

    public Bridge(@NotNull World worldFrom, @NotNull World worldTo, int yFrom, int yTo, @NotNull Material platform, boolean worldToIsBelow) {
        this.worldFrom = worldFrom;
        this.worldTo = worldTo;
        this.yFrom = yFrom;
        this.yTo = yTo;
        this.platform = platform;
        this.worldToIsBelow = worldToIsBelow;
    }

    // This is meant to be very fast
    public boolean checkYCoords(Entity entity) {

        // Gets y level
        Location location = entity.getLocation();
        int y = location.getBlockY();

        // Determines if entity is ready to be teleported for being in the teleportation range
        // Uses short-circuit programming for extra speed (The || operator and all in one statement)

        if(worldToIsBelow)
            return y <= yFrom;
        else
            return y >= yFrom;
    }

    // This is meant to be very fast
    // Checks to see if same world
    public boolean checkWorld(Entity entity) {
        return entity.getWorld() == worldFrom;
    }

    // Creates air for the player, if needed, to prevent suffocation on teleport
    public void ensureTeleportBreathable(Entity e) {

        // Not if in spectator mode
        if(e instanceof Player) {
            if(((Player)e).getGameMode() == GameMode.SPECTATOR)
                return;
        }

        Location loc = e.getLocation();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int height = (int)Math.ceil(e.getHeight());

        // Check for sufficient air before teleporting player
        for(int i = 0; i < height; i++) {

            // Calculate y level
            int y = (worldToIsBelow)
                    ? yTo - i - 1
                    : yTo + i + 1;

            // Get block
            Block block = worldTo.getBlockAt(x, y, z);

            // Determine if breathable and make breathable if not
            if(!BreathableBlocks.isBreathable(block.getType()))
                block.setType(Material.AIR);
        }
    }

    // Ensure the player isn't going to fall back to the other world
    public void ensurePlatform(Entity entity) {

        // Only on worlds that are above
        if(worldToIsBelow)
            return;

        // Entities dont get platforms
        if(!(entity instanceof Player))
            return;

        Player p = (Player)entity;

        // Ignore if player is flying, their platform is wings or /fly
        // Also ignore if spectating
        if(p.isGliding() || p.isFlying() || p.getGameMode() == GameMode.SPECTATOR)
            return;

        // Get players location
        Location loc = entity.getLocation();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Get block
        Block block = worldTo.getBlockAt(x, yTo, z);

        // Set block beneath feat
        if(block.getType().isAir())
            block.setType(platform);
    }

    // Ensure theres an entrance hole above their head when they land in other world
    public void ensureEntranceHole(Entity entity) {
        // Not if in spectator mode
        if(entity instanceof Player) {
            if(((Player)entity).getGameMode() == GameMode.SPECTATOR)
                return;
        }

        // Only on worlds that are below
        if(!worldToIsBelow)
            return;

        // Get entities location
        Location loc = entity.getLocation();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        // Get block
        Block block = worldTo.getBlockAt(x, yTo, z);

        // Set to air if not already something there
        if(!BreathableBlocks.isBreathable(block.getType()))
            block.setType(Material.AIR);
    }

    // Creates a new location, changing world and Y but preserving all other data
    public Location changeLocation(Entity entity) {

        // Clone current location with all of its data like rotate, yaw, pitch, etc...
        Location newLoc = entity.getLocation().clone();

        // Get new y
        double newY = (worldToIsBelow)
                ? yTo - Math.ceil(entity.getHeight())
                : yTo + Math.ceil(entity.getHeight());

        // Change y to new y
        newLoc.setY(newY);

        // Change world
        newLoc.setWorld(worldTo);

        return newLoc;
    }

    public boolean attemptTeleport(Entity e) {

        boolean validBridge = checkYCoords(e) && checkWorld(e);

        if(!validBridge)
            return false;

        // Clone velocity so we can preserve the speed
        Vector vec = e.getVelocity().clone();

        // Get location
        Location loc = changeLocation(e);

        // Get new world
        World world = loc.getWorld();

        // Load chunk async
        world.getChunkAtAsync(loc).thenRun(() -> {

            // Some pre-setup
            ensureTeleportBreathable(e);
            ensurePlatform(e);
            ensureEntranceHole(e);

            // Teleport entity to newly loaded chunk
            // Don't do asyncTeleport. It sounds nice and helpful but it causes pretty weird bugs
            e.teleport(loc);

            // Re-give correct velocity
            e.setVelocity(vec);

            // Countdown timed immunity
            BridgeManager.grantTimedImmunity(e.getUniqueId());
        });

        return true;
    }

    // World from & to
    public World worldFrom;
    public World worldTo;

    // At what y-level is the bridge at
    public int yFrom;
    public int yTo;

    // Platform block to use to place beneath players feet if needed
    public Material platform;

    // Is the new world below this one?
    public boolean worldToIsBelow;
}
