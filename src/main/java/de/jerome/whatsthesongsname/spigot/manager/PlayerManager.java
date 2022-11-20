package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.object.WITSNPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID, WITSNPlayer> players;

    public PlayerManager() {
        players = new HashMap<>();
        loadPlayers();
    }

    private void loadPlayers() {
        for (String playerUUID : WITSNMain.getInstance().getFileManager().getPlayers().getFileConfiguration().getKeys(false))
            getPlayer(UUID.fromString(playerUUID));
    }

    public void saveAllPlayers() {
        for (WITSNPlayer player : players.values()) player.save();
    }

    public @Nullable WITSNPlayer getPlayer(@NotNull UUID uuid) {
        if (players.containsKey(uuid)) return players.get(uuid);

        WITSNPlayer witsnPlayer = new WITSNPlayer(uuid);
        players.put(uuid, witsnPlayer);

        return witsnPlayer;
    }

    public @Nullable WITSNPlayer getPlayer(@NotNull OfflinePlayer offlinePlayer) {
        if (players.containsKey(offlinePlayer.getUniqueId())) return players.get(offlinePlayer.getUniqueId());

        WITSNPlayer witsnPlayer = new WITSNPlayer(offlinePlayer);
        players.put(offlinePlayer.getUniqueId(), witsnPlayer);

        return witsnPlayer;
    }

    public @Nullable WITSNPlayer getPlayer(@NotNull String name) {
        UUID uuid = WITSNMain.getInstance().getUuidFetcher().getUUID(name);
        if (uuid == null) return null;

        if (players.containsKey(uuid)) return players.get(uuid);

        WITSNPlayer witsnPlayer = new WITSNPlayer(uuid);
        players.put(uuid, witsnPlayer);

        return witsnPlayer;
    }
}
