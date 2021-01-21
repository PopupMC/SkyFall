package com.popupmc.skyfall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class OnBlockDamageEvent implements Listener {
    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent e) {

        Block block = e.getBlock();
        World world = block.getWorld();
        Location loc = block.getLocation();

        // We're only interested in bedrock
        if(block.getType() != Material.BEDROCK)
            return;

        // And only if it's at the top of the nether or bottom of the main world
        if(!(world.getName().equalsIgnoreCase(SkyFall.netherWorldName) && loc.getBlockY() == DoTeleport.netherCeiling) &&
                !(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && loc.getBlockY() == DoTeleport.mainFloor))
            return;

        // No one seems to know how to do this, so I'm just kind of guessing here

        // Allow to break instantly
        e.setInstaBreak(true);

        // Break naturally
        block.breakNaturally();

        // Set as air
        block.setType(Material.AIR);
    }
}
