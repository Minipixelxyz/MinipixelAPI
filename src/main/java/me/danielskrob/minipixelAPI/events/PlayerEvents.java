package me.danielskrob.minipixelAPI.events;

import me.danielskrob.minipixelAPI.utils.SpectatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (SpectatorManager.getSpectators().contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player) {
            if (SpectatorManager.getSpectators().contains(player.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

}
