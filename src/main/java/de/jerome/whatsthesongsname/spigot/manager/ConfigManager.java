package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.object.Messages;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private final FileConfiguration configYAML, messagesYAML;

    // config.yml
    private String bungeecordLobby, databaseHost, databaseDatabase, databaseUsername, databasePassword;
    private int databasePort, musicPlayTime, choseTime, rewardCorrect, rewardWrong;
    private boolean rewardVault, bungeecordEnable, databaseEnable;

    // messages.yml
    private HashMap<String, String> messages;
    private ArrayList<String> inventorySongItemsLore;

    public ConfigManager() {
        this.configYAML = WITSNMain.getInstance().getFileManager().getConfig().getFileConfiguration();
        this.messagesYAML = WITSNMain.getInstance().getFileManager().getMessages().getFileConfiguration();
        reload();
    }

    public void reload() {
        reloadConfig();
        reloadMessages();
    }

    public void reloadConfig() {
        // config.yml
        musicPlayTime = configYAML.getInt("musicPlayTime");
        choseTime = configYAML.getInt("choseTime");

        rewardVault = configYAML.getBoolean("reward.vault");
        rewardCorrect = configYAML.getInt("reward.correct");
        rewardWrong = configYAML.getInt("reward.wrong");

        bungeecordEnable = configYAML.getBoolean("bungeecord.enable");
        bungeecordLobby = configYAML.getString("bungeecord.lobby");

        databaseEnable = configYAML.getBoolean("database.enable");
        databaseHost = configYAML.getString("database.host");
        databasePort = configYAML.getInt("database.port");
        databaseDatabase = configYAML.getString("database.database");
        databaseUsername = configYAML.getString("database.username");
        databasePassword = configYAML.getString("database.password");
    }

    public void reloadMessages() {
        // messages.yml
        this.messages = new HashMap<>();

        for (Map.Entry<String, Object> entry : messagesYAML.getValues(true).entrySet()) {
            if (!messagesYAML.isString(entry.getKey())) continue;
            messages.put(entry.getKey(), ChatColor.translateAlternateColorCodes('&', (String) entry.getValue()));
        }

        inventorySongItemsLore = new ArrayList<>();
        if (messagesYAML.isList(Messages.INVENTORY_SONG_ITEMS_LORE.getPath())) {
            List<String> tempLore = messagesYAML.getStringList(Messages.INVENTORY_SONG_ITEMS_LORE.getPath());
            for (String loreEntry : tempLore)
                inventorySongItemsLore.add(ChatColor.translateAlternateColorCodes('&', loreEntry));
        }
    }

    public @NotNull String getBungeecordLobby() {
        return bungeecordLobby;
    }

    public @NotNull String getDatabaseHost() {
        return databaseHost;
    }

    public @NotNull String getDatabaseDatabase() {
        return databaseDatabase;
    }

    public @NotNull String getDatabaseUsername() {
        return databaseUsername;
    }

    public @NotNull String getDatabasePassword() {
        return databasePassword;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public int getMusicPlayTime() {
        return musicPlayTime;
    }

    public int getChoseTime() {
        return choseTime;
    }

    public int getRewardCorrect() {
        return rewardCorrect;
    }

    public int getRewardWrong() {
        return rewardWrong;
    }

    public boolean isRewardVault() {
        return rewardVault;
    }

    public boolean isBungeecordEnable() {
        return bungeecordEnable;
    }

    public boolean isDatabaseEnable() {
        return databaseEnable;
    }

    public @NotNull String getMessage(@NotNull String path) {
        String message = messages.get(path);
        return message == null ? "null" : message;
    }

    public @NotNull String getMessage(@NotNull Messages path) {
        return getMessage(path.getPath());
    }

    public @NotNull HashMap<String, String> getMessages() {
        return messages;
    }

    public ArrayList<String> getInventorySongItemsLore() {
        return new ArrayList<>(inventorySongItemsLore);
    }
}
