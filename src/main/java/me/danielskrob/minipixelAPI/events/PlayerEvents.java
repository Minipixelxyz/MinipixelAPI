package me.danielskrob.minipixelAPI.events;

import me.danielskrob.minipixelAPI.utils.PlayerApiUtils;
import me.danielskrob.minipixelAPI.utils.SpectatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    private boolean isSpectator(Player p) {
        return SpectatorManager.getSpectators().contains(p.getUniqueId());
    }

    private boolean isStarting(Player p) {
        return PlayerApiUtils.getStartingPlayers().contains(p.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (isSpectator(p) || isStarting(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p && isSpectator(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if (e.getEntity() instanceof Player p && isSpectator(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getWhoClicked() instanceof Player p && isSpectator(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onBucketUse(PlayerBucketEmptyEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e){
        if (isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if (isStarting(e.getPlayer())) e.setCancelled(true);
    }
}