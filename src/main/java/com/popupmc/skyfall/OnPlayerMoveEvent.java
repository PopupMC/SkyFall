package com.popupmc.skyfall;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {

        // Lag a bit to help the server out
        skipCounter++;
        if((skipCounter % 5) != 0)
            return;

        skipCounter = 0;

        // Do a hasty rough check
        // Basically nobody needs teleporting anywhere if not within these coordinates
        // Another little optimization to help the server out
        Location loc = e.getPlayer().getLocation();

        if(loc.getBlockY() < DoTeleport.netherCeiling &&
            loc.getBlockY() > 0)
            return;

        // Now try to teleport the person
        DoTeleport.doTeleport(e.getPlayer());
    }

    public static int skipCounter = 0;
}
