package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigManager {

    private static final FileConfiguration configYAML = WITSNMain.getInstance().getFileManager().getConfig().getFileConfiguration();

    // config.yml
    private String languagesDefault, bungeecordLobby, databaseHost, databaseDatabase, databaseUsername, databasePassword;
    private int databasePort, musicPlayTime, choseTime, rewardCorrect, rewardWrong;
    private boolean rewardVault, bungeecordEnable, databaseEnable;
    private List<String> languagesLanguages;


    public ConfigManager() {
        reload();
    }

    public void reload() {
        reloadConfig();
    }

    /**
     * reloads content of config.yml
     */
    public void reloadConfig() {
        musicPlayTime = configYAML.getInt("musicPlayTime");
        choseTime = configYAML.getInt("choseTime");

        rewardVault = configYAML.getBoolean("reward.vault");
        rewardCorrect = configYAML.getInt("reward.correct");
        rewardWrong = configYAML.getInt("reward.wrong");

        languagesDefault = configYAML.getString("languages.default");
        languagesLanguages = configYAML.getStringList("languages.languages");

        bungeecordEnable = configYAML.getBoolean("bungeecord.enable");
        bungeecordLobby = configYAML.getString("bungeecord.lobby");

        databaseEnable = configYAML.getBoolean("database.enable");
        databaseHost = configYAML.getString("database.host");
        databasePort = configYAML.getInt("database.port");
        databaseDatabase = configYAML.getString("database.database");
        databaseUsername = configYAML.getString("database.username");
        databasePassword = configYAML.getString("database.password");
    }

    public String getLanguagesDefault() {
        return languagesDefault;
    }

    public @Nullable String getBungeecordLobby() {
        return bungeecordLobby;
    }

    public @Nullable String getDatabaseHost() {
        return databaseHost;
    }

    public @Nullable String getDatabaseDatabase() {
        return databaseDatabase;
    }

    public @Nullable String getDatabaseUsername() {
        return databaseUsername;
    }

    public @Nullable String getDatabasePassword() {
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

    public @NotNull List<String> getLanguagesLanguages() {
        return languagesLanguages;
    }
}
