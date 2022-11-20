package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.object.FileObject;
import org.jetbrains.annotations.NotNull;

public class FileManager {

    private final FileObject config, messages, players;

    public FileManager() {
        config = new FileObject("config.yml");
        messages = new FileObject("messages.yml");
        players = new FileObject("players.yml");
    }

    public boolean reload() {
        return config.reload() && messages.reload() && players.reload();
    }

    public boolean save() {
        return players.save();
    }

    public @NotNull FileObject getConfig() {
        return config;
    }

    public @NotNull FileObject getMessages() {
        return messages;
    }

    public @NotNull FileObject getPlayers() {
        return players;
    }
}
