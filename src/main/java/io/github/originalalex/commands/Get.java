package io.github.originalalex.commands;

import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Get implements Function {

    private DatabaseCommunicator db;

    public Get(DatabaseCommunicator db) {
        this.db = db;
    }

    public void handle(CommandSender sender, String[] args, ConfigUtils configUtils, StringUtils stringUtils) {
        if (!sender.hasPermission("bounty.bounty")) {
            sender.sendMessage(stringUtils.getInvalidPermsMessage());
            return;
        }
        if (args.length != 2) {
            sender.sendMessage(stringUtils.invalidUsageGet());
            return;
        }
        String posPlayer = args[1];
        Player player = Bukkit.getPlayerExact(posPlayer);
        if (player == null) {
            sender.sendMessage(stringUtils.invalidPlayer(player.getName()));
            return;
        }
        double amount = db.getBounty(player.getUniqueId().toString());
        if (amount == -1) {
            sender.sendMessage(ChatColor.RED + "Player " + player.getName() + " has no active bounty");
        } else {
            String display = stringUtils.addColor(configUtils.getString("bounty_display_message"));
            display = display.replace("%player%", player.getName()).replaceAll("%amount%", String.valueOf(amount));
            sender.sendMessage(display);
        }
    }
}
