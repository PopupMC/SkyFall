package com.popupmc.skyfall;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {

        // Attempt a teleport on every move
        // This sounds really inefficient but it's actually not
        // If I optimized it any with extra checks it would be exactly the same efficiency
        DoTeleport.doTeleport(e.getPlayer());
    }
}
