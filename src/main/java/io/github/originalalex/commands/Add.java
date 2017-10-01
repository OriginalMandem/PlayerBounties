package io.github.originalalex.commands;

import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.NumberUtils;
import io.github.originalalex.helpers.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Add implements Function {

    private double minBounty;
    private double maxBounty;

    private DatabaseCommunicator db;
    private ListBounty listBounty;
    private Economy economy;

    public Add(ListBounty lb, ConfigUtils configUtils, DatabaseCommunicator db, Economy econ) {
        this.db = db;
        this.listBounty = lb;
        this.economy = econ;
        minBounty = configUtils.getDouble("minbounty");
        maxBounty = configUtils.getDouble("maxbounty");
        if (maxBounty < 0  || maxBounty < minBounty || minBounty < 0) {
            Logger.getLogger("Minecraft").warning(ChatColor.RED + "Invalid maxbounty and minbounty, please fix the issue in the config.yml");
        }
    }

    public void handle(CommandSender sender, String[] args, ConfigUtils configUtils, StringUtils stringUtils) {
        if (!sender.hasPermission("bounty.bounty")) {
            sender.sendMessage(stringUtils.getInvalidPermsMessage());
            return;
        }
        if (args.length != 3) {
            sender.sendMessage(stringUtils.invalidUsageAdd());
            return;
        }
        String posPlayer = args[1];
        Player player = Bukkit.getPlayerExact(posPlayer);
        if (player == null) {
            sender.sendMessage(stringUtils.invalidPlayer(posPlayer));
            return;
        }
        String amountStr = args[2];
        double amount = NumberUtils.convert(amountStr);
        if (!(amount >= minBounty && amount <= maxBounty)) {
            sender.sendMessage(stringUtils.invalidAmount(amountStr));
            return;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            double bal = economy.getBalance(p);
            if (bal < amount) {
                p.sendMessage(stringUtils.insufficientBalance());
                return;
            }
            economy.withdrawPlayer(p, amount);
        }
        double current = db.getBounty(player.getUniqueId().toString());
        String msg = "";
        if (current == -1) {
            db.placeBounty(player.getUniqueId().toString(), amount);
            msg = configUtils.getString("bounty_place_message");
        } else {
            amount += current;
            db.updateBounty(player.getUniqueId().toString(), amount);
            msg = configUtils.getString("bounty_increase_message");
        }
        sender.sendMessage(stringUtils.addColor(addPlaceHolders(amount, player.getName(), msg)));
        listBounty.addPlayer(player.getUniqueId(), amount);
    }

    private String addPlaceHolders(double amount, String player, String message) {
        return message.replaceAll("%amount%", String.valueOf(amount)).replaceAll("%player%", player);
    }
}
