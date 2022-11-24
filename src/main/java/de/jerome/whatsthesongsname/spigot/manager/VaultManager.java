package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class VaultManager {

    private final boolean economyEnabled;
    private Economy economy;

    public VaultManager() {
        if (!WTSNMain.getInstance().getConfigManager().isRewardVault()) {
            economyEnabled = false;
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            WTSNMain.getInstance().getLogger().log(Level.WARNING, "Vault is not installed, so rewards are disabled!");
            economyEnabled = false;
            return;
        }

        economyEnabled = setupEconomy();
        if (!economyEnabled) {
            WTSNMain.getInstance().getLogger().log(Level.INFO, "No economy system was found connected to Vault, so rewards are disabled!");
        }

        WTSNMain.getInstance().getLogger().log(Level.INFO, "Vault has been linked to " + economy.getName() + "!");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) return false;
        economy = registeredServiceProvider.getProvider();
        return true;
    }

    public @Nullable Economy getEconomy() {
        return economy;
    }

    public boolean isEconomyEnabled() {
        return economyEnabled;
    }
}
