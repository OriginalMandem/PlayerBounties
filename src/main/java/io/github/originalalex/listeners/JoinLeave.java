package io.github.originalalex.listeners;

import io.github.originalalex.commands.ListBounty;
import io.github.originalalex.database.DatabaseCommunicator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinLeave implements Listener {

    private ListBounty bounty;
    private DatabaseCommunicator db;

    public JoinLeave(DatabaseCommunicator db, ListBounty bounty) {
        this.bounty = bounty;
        this.db = db;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        double playerBounty = db.getBounty(uuid.toString());
        if (playerBounty != -1) {
            bounty.addPlayer(uuid, playerBounty);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (bounty.hasBounty(uuid)) {
            bounty.removePlayer(uuid);
        }
    }

}
