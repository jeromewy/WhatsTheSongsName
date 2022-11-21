package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.object.FileObject;
import de.jerome.whatsthesongsname.spigot.object.Messages;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguagesManager {

    private final List<String> isocodes;

    /**
     * HashMap<Isocode, HashMap<Path, Message>>
     */
    private final HashMap<String, HashMap<String, String>> languageMessages;

    /**
     * HashMap<Isocode, HashMap<Path, List<Message>>>
     */
    private final HashMap<String, HashMap<String, List<String>>> languageStringLists;

    public LanguagesManager() {
        isocodes = new ArrayList<>();
        languageMessages = new HashMap<>();
        languageStringLists = new HashMap<>();
        reload();
    }

    /**
     * reloads the messages
     */
    public void reload() {
        // Loads all isocodes
        isocodes.clear();
        isocodes.addAll(WITSNMain.getInstance().getConfigManager().getLanguagesLanguages());

        languageMessages.clear();
        languageStringLists.clear();
        for (String isocode : isocodes) {
            // Loads the FileObject of the isocode
            FileObject fileObject = new FileObject(isocode + ".yml", "languages");

            // Loads all messages
            HashMap<String, String> messages = new HashMap<>();
            for (Map.Entry<String, Object> entry : fileObject.getFileConfiguration().getValues(true).entrySet()) {
                if (!fileObject.getFileConfiguration().isString(entry.getKey())) continue;
                messages.put(entry.getKey(), ChatColor.translateAlternateColorCodes('&', (String) entry.getValue()));
            }
            languageMessages.put(isocode, messages);

            // Loads all StringLists (e.g. for Lore)
            HashMap<String, List<String>> stringLists = new HashMap<>();
            for (Map.Entry<String, Object> entry : fileObject.getFileConfiguration().getValues(true).entrySet()) {
                if (!fileObject.getFileConfiguration().isList(entry.getKey())) continue;
                List<String> stringList = (List<String>) entry.getValue();
                stringList.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));
                stringLists.put(entry.getKey(), stringList);
            }
            languageStringLists.put(isocode, stringLists);
        }
    }

    /**
     * Get all isocodes
     *
     * @return a copy of all isocodes
     */
    public @NotNull List<String> getIsocodes() {
        return new ArrayList<>(isocodes);
    }

    /**
     * Get a message in the correct language
     *
     * @param isocode of the language
     * @param path    of the message file
     * @return the message in the correct language
     */
    public @NotNull String getMessage(@Nullable String isocode, @NotNull Messages path) {
        HashMap<String, String> languageMessages = this.languageMessages.getOrDefault(isocode, this.languageMessages.get(WITSNMain.getInstance().getConfigManager().getLanguagesDefault()));
        if (languageMessages == null) return "[" + path.getPath() + "]";
        String message = languageMessages.get(path.getPath());
        if (message == null) return "[" + path.getPath() + "]";
        return message;
    }

    /**
     * Get a List<String> in the correct language
     *
     * @param isocode of the language
     * @param path    of the message file
     * @return the List<String> in the correct language
     */
    public @NotNull List<String> getStringLists(@Nullable String isocode, @NotNull Messages path) {
        HashMap<String, List<String>> languageStringLists = this.languageStringLists.getOrDefault(isocode, this.languageStringLists.get(WITSNMain.getInstance().getConfigManager().getLanguagesDefault()));
        if (languageStringLists == null) return new ArrayList<>();
        List<String> stringList = languageStringLists.get(path.getPath());
        if (stringList == null) return new ArrayList<>();
        return stringList;
    }
}
