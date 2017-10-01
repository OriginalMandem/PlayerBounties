package io.github.originalalex.commands;

import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Remove implements Function {

    private ListBounty listBounty;
    private DatabaseCommunicator db;

    public Remove(DatabaseCommunicator db, ListBounty bounty) {
        this.listBounty = bounty;
        this.db = db;
    }

    public void handle(CommandSender sender, String[] args, ConfigUtils configUtils, StringUtils stringUtils) {
        if (!sender.hasPermission("bounty.remove")) {
            sender.sendMessage(stringUtils.getInvalidPermsMessage());
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(stringUtils.invalidUsageRemove());
            return;
        }
        String posPlayer = args[0];
        Player player = Bukkit.getPlayerExact(posPlayer);
        if (player == null) {
            sender.sendMessage(stringUtils.invalidPlayer(posPlayer));
            return;
        }
        db.removeBounty(player.getUniqueId().toString());
        listBounty.removePlayer(player.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "If there was a bounty on " + player.getName() + " it has been removed.");
    }
}
