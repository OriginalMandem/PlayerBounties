package io.github.originalalex.helpers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtils {

    private FileConfiguration config;

    public ConfigUtils(JavaPlugin plugin) {
        this.config = plugin.getConfig();
    }

    public String getString(String query) {
        return config.getString(query);
    }

    public double getDouble(String query) {
        return config.getDouble(query);
    }

}
