package com.popupmc.skyfall;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamageEvent implements Listener {
    @EventHandler
    public void onEntityDamage (EntityDamageEvent e) {

        // Get damage reason
        EntityDamageEvent.DamageCause damageCause = e.getCause();

        // Stop here if not void related
        if(damageCause != EntityDamageEvent.DamageCause.VOID)
            return;

        // Do a teleport between worlds and cancel damage if successful (Right conditions)
        if(SkyFall.bridgeManager.attemptTeleport(e.getEntity()))
            e.setCancelled(true);
    }
}
