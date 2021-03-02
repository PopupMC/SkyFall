package com.popupmc.skyfall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class OnBlockDamageEvent implements Listener {
    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent e) {

        Block block = e.getBlock();

        // We're only interested in bedrock
        if(block.getType() != Material.BEDROCK)
            return;

        Player p = e.getPlayer();
        Material mainHand = p.getInventory().getItemInMainHand().getType();

        // Requires an axe of any kind in your hand
        if(mainHand != Material.WOODEN_AXE &&
                mainHand != Material.STONE_AXE &&
                mainHand != Material.IRON_AXE &&
                mainHand != Material.GOLDEN_AXE &&
                mainHand != Material.DIAMOND_AXE &&
                mainHand != Material.NETHERITE_AXE)
            return;

        // No one seems to know how to do this, so I'm just kind of guessing here

        // Allow to break instantly
        e.setInstaBreak(true);

        // Break naturally
        block.breakNaturally(p.getInventory().getItemInMainHand(), true);

        // Drop item at players feet
        p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.BEDROCK));

        // Set as air
        block.setType(Material.AIR);

//        // Place in player inventory
//        HashMap<Integer, ItemStack> leftover = p.getInventory().addItem(new ItemStack(Material.BEDROCK));
//        if(leftover.isEmpty())
//            return;
    }
}
