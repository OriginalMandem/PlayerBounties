package io.github.originalalex.commands;

import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import org.bukkit.command.CommandSender;

public interface Function {

    public void handle(CommandSender sender, String[] args, ConfigUtils configUtils, StringUtils stringUtils);

}
