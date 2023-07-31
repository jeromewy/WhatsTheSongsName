package de.jerome.whatsthesongsname.spigot.object;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import de.jerome.whatsthesongsname.spigot.manager.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class WTSNPlayer {

    private static final VaultManager vaultManager = WTSNMain.getInstance().getVaultManager();
    private static final FileConfiguration players = WTSNMain.getInstance().getFileManager().getPlayers().getFileConfiguration();

    private final UUID uuid;
    private final OfflinePlayer offlinePlayer;
    private String name;
    private int points, guessedCorrectly, guessedWrong;

    public WTSNPlayer(UUID uuid) {
        this.uuid = uuid;
        offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        reload();
    }

    public WTSNPlayer(OfflinePlayer offlinePlayer) {
        uuid = offlinePlayer.getUniqueId();
        this.offlinePlayer = offlinePlayer;

        reload();
    }

    public void reload() {
        if (WTSNMain.getInstance().getConfigManager().isDatabaseEnable()) {
            try {
                ResultSet resultSet = WTSNMain.getInstance().getDatabaseManager().getStatement().executeQuery("SELECT * FROM wtsn_players WHERE UUID = '" + uuid + "'");
                if (resultSet.next()) {
                    points = resultSet.getInt("POINTS");
                    guessedCorrectly = resultSet.getInt("GUESSED_CORRECTLY");
                    guessedWrong = resultSet.getInt("GUESSED_WRONG");
                } else
                    WTSNMain.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO wtsn_players (UUID, POINTS, GUESSED_CORRECTLY, GUESSED_WRONG) VALUES ('" + uuid + "', " + points + ", " + guessedCorrectly + ", " + guessedCorrectly + ")");
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
        if (WTSNMain.getInstance().getConfigManager().isDatabaseEnable()) {
            try {
                ResultSet resultSet = WTSNMain.getInstance().getDatabaseManager().getStatement().executeQuery("SELECT * FROM wtsn_players WHERE UUID = '" + uuid + "'");
                if (resultSet.next()) {
                    resultSet.updateInt("POINTS", points);
                    resultSet.updateInt("GUESSED_CORRECTLY", guessedCorrectly);
                    resultSet.updateInt("GUESSED_WRONG", guessedWrong);
                    resultSet.updateRow();
                } else
                    WTSNMain.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO wtsn_players (UUID, POINTS, GUESSED_CORRECTLY, GUESSED_WRONG) VALUES ('" + uuid + "', " + points + ", " + guessedCorrectly + ", " + guessedCorrectly + ")");
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

        if (tempName != null) name = tempName;
        else name = WTSNMain.getInstance().getUuidFetcher().getName(uuid);

        return name;
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

    public void setPoints(int points) {
        if (points < 0) return;
        this.points = points;
    }

    public void addPoints(int points) {
        if (points <= 0) return;
        setPoints(this.points + points);
    }

    public void removePoints(int points) {
        if (points <= 0) return;
        setPoints(this.points - points);
    }

    public int getGuessedCorrectly() {
        return guessedCorrectly;
    }

    public void addGuessedCorrectly() {
        addPoints(20);

        if (!offlinePlayer.isOnline()) return;

        // command reward
        if (WTSNMain.getInstance().getConfigManager().isRewardCommandEnabled())
            for (String command : WTSNMain.getInstance().getConfigManager().getRewardCommandCorrect())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%playerName%", Objects.requireNonNull(getName())));

        // vault reward
        if (vaultManager.isEconomyEnabled() && vaultManager.getEconomy() != null)
            vaultManager.getEconomy().depositPlayer(offlinePlayer, WTSNMain.getInstance().getConfigManager().getRewardVaultCorrect());

        this.guessedCorrectly++;
    }

    public int getGuessedWrong() {
        return guessedWrong;
    }

    public void addGuessedWrong() {
        removePoints(5);

        if (!offlinePlayer.isOnline()) return;

        // command reward
        if (WTSNMain.getInstance().getConfigManager().isRewardCommandEnabled())
            for (String command : WTSNMain.getInstance().getConfigManager().getRewardCommandWrong())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%playerName%", Objects.requireNonNull(getName())));

        // vault reward
        if (vaultManager.isEconomyEnabled() && vaultManager.getEconomy() != null)
            vaultManager.getEconomy().withdrawPlayer(offlinePlayer, WTSNMain.getInstance().getConfigManager().getRewardVaultWrong());

        this.guessedWrong++;
    }
}
