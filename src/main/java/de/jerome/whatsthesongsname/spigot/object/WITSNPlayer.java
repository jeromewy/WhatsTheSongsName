package de.jerome.whatsthesongsname.spigot.object;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.manager.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class WITSNPlayer {

    private static final VaultManager vaultManager = WITSNMain.getInstance().getVaultManager();
    private static final FileConfiguration players = WITSNMain.getInstance().getFileManager().getPlayers().getFileConfiguration();

    private final UUID uuid;
    private final OfflinePlayer offlinePlayer;
    private String name;
    private int points, guessedCorrectly, guessedWrong;

    public WITSNPlayer(UUID uuid) {
        this.uuid = uuid;
        offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        reload();
    }

    public WITSNPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.name = name;

        reload();
    }

    public WITSNPlayer(OfflinePlayer offlinePlayer) {
        uuid = offlinePlayer.getUniqueId();
        this.offlinePlayer = offlinePlayer;

        reload();
    }

    public void reload() {
        if (WITSNMain.getInstance().getConfigManager().isDatabaseEnable()) {
            try {
                ResultSet resultSet = WITSNMain.getInstance().getDatabaseManager().getStatement().executeQuery("SELECT * FROM witsn_players WHERE UUID = '" + uuid + "'");
                if (resultSet.next()) {
                    points = resultSet.getInt("POINTS");
                    guessedCorrectly = resultSet.getInt("GUESSED_CORRECTLY");
                    guessedWrong = resultSet.getInt("GUESSED_WRONG");
                } else
                    WITSNMain.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO witsn_players (UUID, POINTS, GUESSED_CORRECTLY, GUESSED_WRONG) VALUES ('" + uuid + "', " + points + ", " + guessedCorrectly + ", " + guessedCorrectly + ")");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            points = players.getInt(uuid + ".points");
            guessedCorrectly = players.getInt(uuid + ".guessedCorrectly");
            guessedWrong = players.getInt(uuid + ".guessedWrong");
        }
    }

    public void save() {
        if (WITSNMain.getInstance().getConfigManager().isDatabaseEnable()) {
            try {
                ResultSet resultSet = WITSNMain.getInstance().getDatabaseManager().getStatement().executeQuery("SELECT * FROM witsn_players WHERE UUID = '" + uuid + "'");
                if (resultSet.next()) {
                    resultSet.updateInt("POINTS", points);
                    resultSet.updateInt("GUESSED_CORRECTLY", guessedCorrectly);
                    resultSet.updateInt("GUESSED_WRONG", guessedWrong);
                    resultSet.updateRow();
                } else
                    WITSNMain.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO witsn_players (UUID, POINTS, GUESSED_CORRECTLY, GUESSED_WRONG) VALUES ('" + uuid + "', " + points + ", " + guessedCorrectly + ", " + guessedCorrectly + ")");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            players.set(uuid + ".points", points);
            players.set(uuid + ".guessedCorrectly", guessedCorrectly);
            players.set(uuid + ".guessedWrong", guessedWrong);
        }
    }

    private String loadName() {
        String tempName = Bukkit.getOfflinePlayer(uuid).getName();
        if (tempName != null) return tempName;
        return WITSNMain.getInstance().getUuidFetcher().getName(uuid);
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @Nullable String getName() {
        return this.name == null ? loadName() : name;
    }

    public int getPoints() {
        return points;
    }

    public boolean setPoints(int points) {
        if (points < 0) return false;
        this.points = points;
        return true;
    }

    public boolean addPoints(int points) {
        if (points <= 0) return false;
        return setPoints(this.points + points);
    }

    public boolean removePoints(int points) {
        if (points <= 0) return false;
        return setPoints(this.points - points);
    }

    public int getGuessedCorrectly() {
        return guessedCorrectly;
    }

    public void addGuessedCorrectly() {
        addPoints(20);

        if (vaultManager.isEconomyEnabled())
            vaultManager.getEconomy().depositPlayer(offlinePlayer, WITSNMain.getInstance().getConfigManager().getRewardCorrect());

        this.guessedCorrectly++;
    }

    public int getGuessedWrong() {
        return guessedWrong;
    }

    public void addGuessedWrong() {
        removePoints(5);

        if (vaultManager.isEconomyEnabled())
            vaultManager.getEconomy().withdrawPlayer(offlinePlayer, WITSNMain.getInstance().getConfigManager().getRewardWrong());

        this.guessedWrong++;
    }
}
