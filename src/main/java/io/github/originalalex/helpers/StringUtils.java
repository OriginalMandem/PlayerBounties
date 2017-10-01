package io.github.originalalex.helpers;

import org.bukkit.ChatColor;

public class StringUtils {

    private ConfigUtils config;

    public StringUtils(ConfigUtils config) {
        this.config = config;
    }

    public String addColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String mustBePlayerMessage() {
        return ChatColor.RED + "You must be a player to use this command!";
    }

    public String getInvalidPermsMessage() {
        return ChatColor.RED + "You do not have permission to use this command!";
    }

    public String insufficientBalance() {
        return ChatColor.RED + "You do not have sufficient funds to place this bounty!";
    }

    public String getHelpMessage() {
        return addColor(config.getString("help_message"));
    }

    public String invalidPlayer(String name) {
        return ChatColor.RED + "The player " + name + " is not currently online!";
    }

    public String invalidAmount(String amount) {
        return ChatColor.RED + "The amount " + amount + " is not valid.";
    }

    private String invalidUsageBase() {
        return ChatColor.RED + "Invalid usage! ";
    }

    public String invalidUsageRemove() {
        return invalidUsageBase() + "/bounty remove [player]";
    }

    public String invalidUsageAdd() {
        return invalidUsageBase() + "/bounty place [player] [amount]";
    }

    public String invalidUsageGet() {
        return invalidUsageBase() + "/bounty get [player]";
    }

}
