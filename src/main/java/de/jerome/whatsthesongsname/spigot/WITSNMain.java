package de.jerome.whatsthesongsname.spigot;

import de.jerome.whatsthesongsname.spigot.command.WITSNCommand;
import de.jerome.whatsthesongsname.spigot.listener.InventoryListener;
import de.jerome.whatsthesongsname.spigot.listener.PlayerListener;
import de.jerome.whatsthesongsname.spigot.manager.*;
import de.jerome.whatsthesongsname.spigot.util.UUIDFetcher;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WITSNMain extends JavaPlugin {

    private static WITSNMain instance;

    private ConfigManager configManager;
    private FileManager fileManager;
    private GameManager gameManager;
    private InventoryManager inventoryManager;
    private PlayerManager playerManager;
    private SongManager songManager;
    private UUIDFetcher uuidFetcher;

    private VaultManager vaultManager;

    public static @NotNull WITSNMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        makeInstances();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        playerManager.saveAllPlayers();

        if (!configManager.isDatabaseEnable())
            fileManager.save();
    }

    public boolean reload() {
        boolean success = fileManager.reload();
        if (success) {
            configManager.reload();
            inventoryManager.reload();
        }
        success = success && songManager.reload();
        if (success) gameManager.reload();
        return success;
    }

    private void makeInstances() {
        instance = this;
        uuidFetcher = new UUIDFetcher();
        fileManager = new FileManager();
        configManager = new ConfigManager(); // FileManager

        vaultManager = new VaultManager(); // ConfigManager
        playerManager = new PlayerManager(); // ConfigManager
        inventoryManager = new InventoryManager(); // ConfigManager
        songManager = new SongManager(); // ConfigManager
        gameManager = new GameManager(); // ConfigManager, SongManager
    }

    private void registerCommands() {
        new WITSNCommand();
    }

    private void registerListeners() {
        new InventoryListener();
        new PlayerListener();
    }

    public @NotNull ConfigManager getConfigManager() {
        return configManager;
    }

    public @NotNull FileManager getFileManager() {
        return fileManager;
    }

    public @NotNull GameManager getGameManager() {
        return gameManager;
    }

    public @NotNull InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public @NotNull PlayerManager getPlayerManager() {
        return playerManager;
    }

    public @NotNull SongManager getSongManager() {
        return songManager;
    }

    public @NotNull UUIDFetcher getUuidFetcher() {
        return uuidFetcher;
    }

    public @NotNull VaultManager getVaultManager() {
        return vaultManager;
    }
}