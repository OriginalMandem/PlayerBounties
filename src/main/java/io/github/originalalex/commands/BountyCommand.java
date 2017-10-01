package io.github.originalalex.commands;

import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BountyCommand implements CommandExecutor {

    private Add add;
    private Get get;
    private ListBounty list;
    private Remove remove;

    private StringUtils stringHelp;
    private ConfigUtils configUtils;
    private Economy economy;

    public BountyCommand(ListBounty bounty, DatabaseCommunicator db, ConfigUtils configUtils, StringUtils stringUtils, Economy economy) {
        this.get = new Get(db);
        this.list = bounty;
        this.add = new Add(list, configUtils, db, economy);
        this.remove = new Remove(db, list);
        this.stringHelp = stringUtils;
        this.economy = economy;
        this.configUtils = configUtils;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(stringHelp.getHelpMessage());
            return true;
        }
        switch(args[0].toLowerCase()) {
            case "place":
            case "put":
            case "add":
                add.handle(sender, args, configUtils, stringHelp); break;
            case "get":
                get.handle(sender, args, configUtils, stringHelp); break;
            case "list":
                list.handle(sender, args, configUtils, stringHelp); break;
            case "delete":
            case "remove":
                remove.handle(sender, args, configUtils, stringHelp); break;
            default: sender.sendMessage(stringHelp.getHelpMessage());
        }
        return true;
    }
}
