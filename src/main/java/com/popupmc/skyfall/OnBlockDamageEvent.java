package com.popupmc.skyfall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
        if(!(world.getName().equalsIgnoreCase(SkyFall.netherWorldName) && loc.getBlockY() == BridgeManager.netherCeiling) &&
                !(world.getName().equalsIgnoreCase(SkyFall.mainWorldName) && loc.getBlockY() == BridgeManager.mainFloor))
            return;

        Player p = e.getPlayer();
        Material mainHand = p.getInventory().getItemInMainHand().getType();

        // Requires an axe of any kind in your hand
        if(mainHand != Material.WOODEN_AXE &&
                mainHand != Material.STONE_AXE &&
                mainHand != Material.IRON_AXE &&
                mainHand != Material.GOLDEN_AXE &&
                mainHand != Material.DIAMOND_AXE)
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
