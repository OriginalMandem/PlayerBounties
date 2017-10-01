package io.github.originalalex;

import io.github.originalalex.commands.BountyCommand;
import io.github.originalalex.commands.ListBounty;
import io.github.originalalex.database.DatabaseCommunicator;
import io.github.originalalex.helpers.ConfigUtils;
import io.github.originalalex.helpers.StringUtils;
import io.github.originalalex.listeners.DeathEvent;
import io.github.originalalex.listeners.InventoryChangeEvent;
import io.github.originalalex.listeners.JoinLeave;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.util.stream.Stream;

public class Plugin extends JavaPlugin {

    private Economy economy;

    @Override
    public void onEnable() {
        if (!initializeVault()) {
            return;
        }
        initialize();
    }

    private void initialize() {
        saveDefaultConfig();
        ConfigUtils config = new ConfigUtils(this);
        StringUtils stringUtils = new StringUtils(config);
        DatabaseCommunicator db = new DatabaseCommunicator(this);
        ListBounty bounty = new ListBounty(db);
        getCommand("bounty").setExecutor(new BountyCommand(bounty, db, config, stringUtils, economy));
        Stream.of(
                new InventoryChangeEvent(),
                new DeathEvent(db, config, bounty, economy),
                new JoinLeave(db, bounty)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private boolean initializeVault() {
        if (!setupEconomy() ) {
            Logger.getLogger("Minecraft").info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

}
