package io.github.originalalex.commands;

import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ListBounty implements Function {

    private Map<UUID, Double> playersAndBounties = new HashMap<>();

    public ListBounty(DatabaseCommunicator db) {
        try (ResultSet rs = db.getEverything()){
            while (rs.next()) { // populate our map
                playersAndBounties.put(UUID.fromString(rs.getString("uuid")), rs.getDouble("bounty"));
            }
            Iterator<UUID> iter = playersAndBounties.keySet().iterator();
            while (iter.hasNext()) {
                UUID id = iter.next();
                if (Bukkit.getPlayer(id) == null) {
                    iter.remove();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings( "deprecation" )
    private Inventory getBounties() {
        Inventory inv = Bukkit.createInventory(null, 54, "Bounty Hunter Contracts");
        Set<UUID> keys = playersAndBounties.keySet();
        Comparator<UUID> comparator = new Comparator<UUID>() {
            @Override
            public int compare(UUID uuid, UUID uuid2) {
                return playersAndBounties.get(uuid).compareTo(playersAndBounties.get(uuid2));
            }

        };
        List<UUID> sortedList = new ArrayList<>(keys);
        Collections.sort(sortedList, comparator);
        for (int i = 0; i < sortedList.size(); i++) {
            UUID uuid = sortedList.get(i);
            Player player = Bukkit.getPlayer(uuid);
            String name = player.getName();
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + name);
            meta.setOwner(player.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Bounty: " + ChatColor.AQUA + "" + ChatColor.BOLD + playersAndBounties.get(uuid));
            lore.add("");
            lore.add(ChatColor.GRAY + "Kill this player to receive the reward.");
            meta.setLore(lore);
            skull.setItemMeta(meta);
            inv.setItem(i, skull);
        }
        return inv;
    }

    public void handle(CommandSender sender, String[] args, ConfigUtils configUtils, StringUtils stringUtils) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(stringUtils.mustBePlayerMessage());
            return;
        }
        Player player = (Player) sender;
        player.openInventory(getBounties());
    }

    public void addPlayer(UUID uuid, double bounty) {
        this.playersAndBounties.put(uuid, bounty);
    }

    public void removePlayer(UUID uuid) {
        if (playersAndBounties.containsKey(uuid)) {
            this.playersAndBounties.remove(uuid);
        }
    }

    public boolean hasBounty(UUID uuid) {
        return playersAndBounties.containsKey(uuid);
    }

    public double getReward(UUID uuid) {
        return playersAndBounties.get(uuid);
    }

}
