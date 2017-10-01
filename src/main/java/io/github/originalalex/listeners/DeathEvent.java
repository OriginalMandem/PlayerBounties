package io.github.originalalex.listeners;

import io.github.originalalex.commands.ListBounty;
import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.xml.crypto.Data;

public class DeathEvent implements Listener {

    private ListBounty bounty;
    private Economy economy;
    private ConfigUtils config;
    private DatabaseCommunicator db;

    public DeathEvent(DatabaseCommunicator db, ConfigUtils configUtils, ListBounty bounty, Economy economy) {
        this.bounty = bounty;
        this.economy = economy;
        this.config = configUtils;
        this.db = db;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        if (bounty.hasBounty(dead.getUniqueId())) {
            Player killer = dead.getKiller();
            double reward = bounty.getReward(dead.getUniqueId());
            economy.depositPlayer(killer, reward);
            String privateReward = config.getString("collect_bounty_message");
            privateReward = privateReward.replaceAll("%amount%", String.valueOf(reward));
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&', privateReward));
            String rewardReceivedMessage = config.getString("bounty_collect_message");
            rewardReceivedMessage = ChatColor.translateAlternateColorCodes('&', rewardReceivedMessage);
            rewardReceivedMessage = rewardReceivedMessage.replaceAll("%killer%", killer.getName()).replace("%player%", dead.getName()).replaceAll("%amount%", String.valueOf(reward));
            Bukkit.broadcastMessage(rewardReceivedMessage);
            bounty.removePlayer(dead.getUniqueId());
            db.removeBounty(dead.getUniqueId().toString());
        }
    }
}
