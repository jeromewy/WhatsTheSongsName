package de.jerome.whatsthesongsname.spigot.object;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileObject {

    /**
     * instance of your main class
     */
    private static final JavaPlugin javaPlugin = WTSNMain.getInstance();

    /**
     * path to stream the file from this plugin ressource to servers plugin directory
     */
    private final String fileStreamPath;

    /**
     * the file
     */
    private final File file;

    /**
     * the FileConfiguration
     */
    private FileConfiguration fileConfiguration;

    /**
     * create a new file in the given subdirectories of the main directory of this plugin
     * if there is a file with that name in the resource directory, that file will be copied
     *
     * @param filename name of file with .yml
     */
    public FileObject(String filename, String... filePath) {
        StringBuilder foldersStringBuilder = new StringBuilder();
        for (String temp : filePath)
            foldersStringBuilder.append(temp).append("/");

        this.fileStreamPath = foldersStringBuilder + filename;
        this.file = new File(javaPlugin.getDataFolder().getPath() + "/" + foldersStringBuilder, filename);

        load();
    }

    /**
     * create a new file in the main directory of this plugin
     * if there is a file with that name in the resource directory, that file will be copied
     *
     * @param filename name of file with .yml
     */
    public FileObject(String filename) {
        this.fileStreamPath = filename;
        this.file = new File(javaPlugin.getDataFolder().getPath(), filename);

        load();
    }

    /**
     * load config
     */
    private void load() {
        if (!file.exists())
            createFile();
        reload();
    }

    /**
     * create the file or copy it from the resource if it exists
     */
    private void createFile() {
        if (!file.getParentFile().exists())
            if (!file.getParentFile().mkdirs()) {
                System.out.println("Could not create directory: " + file.getParentFile().getPath());
                return;
            }

        InputStream inputStream = javaPlugin.getResource(fileStreamPath);
        if (inputStream != null) {
            try {
                Files.copy(inputStream, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            if (!file.createNewFile()) System.out.println("Could not create file: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * reload the config from file
     *
     * @return true = if success; false = everything else
     */
    public boolean reload() {
        try {
            if (fileStreamPath.equals("config.yml")) {
                javaPlugin.reloadConfig();
                fileConfiguration = javaPlugin.getConfig();
                return true;
            }

            if (fileConfiguration != null) {
                fileConfiguration.load(file);
                return true;
            }

            fileConfiguration = YamlConfiguration.loadConfiguration(file);
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * save the config to file
     *
     * @return true = if success; false = everything else
     */
    public boolean save() {
        try {
            if (fileStreamPath.equals("config.yml")) {
                javaPlugin.saveConfig();
                return true;
            }

            fileConfiguration.save(file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * represent file
     *
     * @return file
     */
    public @NotNull File getFile() {
        return file;
    }

    /**
     * represent fileConfiguration
     *
     * @return fileConfiguration
     */
    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

}
