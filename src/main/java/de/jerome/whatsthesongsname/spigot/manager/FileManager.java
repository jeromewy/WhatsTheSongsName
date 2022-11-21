package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.object.FileObject;
import org.jetbrains.annotations.NotNull;

public class FileManager {

    private final FileObject config, players;

    public FileManager() {
        config = new FileObject("config.yml");
        players = new FileObject("players.yml");
    }

    public boolean reload() {
        return config.reload() && players.reload();
    }

    public boolean save() {
        return players.save();
    }

    public @NotNull FileObject getConfig() {
        return config;
    }

    public @NotNull FileObject getPlayers() {
        return players;
    }
}
