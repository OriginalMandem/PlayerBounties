package io.github.originalalex.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryChangeEvent implements Listener {

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent e) {
        if (ChatColor.stripColor(e.getInitiator().getName()).equalsIgnoreCase("Bounty Hunter Contracts")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Bounty Hunter Contracts")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Bounty Hunter Contracts")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(InventoryInteractEvent e) {
        if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Bounty Hunter Contracts")){
            e.setCancelled(true);
        }
    }

}
