package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import de.jerome.whatsthesongsname.spigot.object.WTSNPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID, WTSNPlayer> players;

    public PlayerManager() {
        players = new HashMap<>();
        loadPlayers();
    }

    private void loadPlayers() {
        for (String playerUUID : WTSNMain.getInstance().getFileManager().getPlayers().getFileConfiguration().getKeys(false))
            getPlayer(UUID.fromString(playerUUID));
    }

    public void saveAllPlayers() {
        for (WTSNPlayer player : players.values()) player.save();
    }

    public @Nullable WTSNPlayer getPlayer(@NotNull UUID uuid) {
        if (players.containsKey(uuid)) return players.get(uuid);

        WTSNPlayer WTSNPlayer = new WTSNPlayer(uuid);
        players.put(uuid, WTSNPlayer);

        return WTSNPlayer;
    }

    public @Nullable WTSNPlayer getPlayer(@NotNull OfflinePlayer offlinePlayer) {
        if (players.containsKey(offlinePlayer.getUniqueId())) return players.get(offlinePlayer.getUniqueId());

        WTSNPlayer WTSNPlayer = new WTSNPlayer(offlinePlayer);
        players.put(offlinePlayer.getUniqueId(), WTSNPlayer);

        return WTSNPlayer;
    }

    public @Nullable WTSNPlayer getPlayer(@NotNull String name) {
        UUID uuid = WTSNMain.getInstance().getUuidFetcher().getUUID(name);
        if (uuid == null) return null;

        if (players.containsKey(uuid)) return players.get(uuid);

        WTSNPlayer WTSNPlayer = new WTSNPlayer(uuid);
        players.put(uuid, WTSNPlayer);

        return WTSNPlayer;
    }
}
