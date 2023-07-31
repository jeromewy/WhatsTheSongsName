package de.jerome.whatsthesongsname.spigot.manager;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import de.jerome.whatsthesongsname.spigot.WTSNMain;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class SongManager {

    private final File songsDirectory;
    private final List<Song> songs;
    private Playlist playlist;
    private RadioSongPlayer radioSongPlayer;

    private boolean firstTry = true;

    public SongManager() {
        songsDirectory = new File(WTSNMain.getInstance().getDataFolder(), "songs");
        songs = new ArrayList<>();

        load();
    }

    private boolean load() {
        songs.clear();

        // create the sounds folder if it doesn't exist
        if (!songsDirectory.mkdirs())
            // if it existed, it is checked whether it is a folder
            if (!songsDirectory.isDirectory())
                // once an attempt is made to delete it and then create it again
                if (firstTry) {
                    firstTry = false;
                    if (!songsDirectory.delete()) return false;
                    return load();
                } else {
                    // Output on second try
                    WTSNMain.getInstance().getLogger().log(Level.SEVERE, "Song list cannot be loaded, please delete the songs file");
                    return false;
                }

        File[] files = songsDirectory.listFiles();
        if (files == null || files.length == 0) {
            WTSNMain.getInstance().getLogger().log(Level.WARNING, "No songs were found in the folder");
            return false;
        }

        // Goes through the "songs" folder, searches for songs, loads them and adds them to the songs list
        for (File songFile : files) {
            // Check if the file is an .nbs file
            String[] split = songFile.getName().split("\\.");
            if (!split[split.length - 1].equals("nbs")) {
                WTSNMain.getInstance().getLogger().log(Level.WARNING, "File " + songFile.getName() + " could not be loaded as a song");
                continue;
            }

            // Loads the song and adds it to songs list
            songs.add(NBSDecoder.parse(songFile));
        }

        if (songs.isEmpty()) {
            WTSNMain.getInstance().getLogger().log(Level.WARNING, "No songs were found in the folder");
            return true;
        }

        // Shuffles the songs, so they don't always play in the same order
        Collections.shuffle(songs);

        playlist = new Playlist(songs.toArray(new Song[0]));
        radioSongPlayer = new RadioSongPlayer(playlist);
        radioSongPlayer.setRandom(true);
        radioSongPlayer.setRepeatMode(RepeatMode.ALL);
        return true;
    }

    /**
     * reloads the song list
     *
     * @return if success
     */
    public boolean reload() {
        firstTry = true;
        return load();
    }

    public @Nullable Playlist getPlaylist() {
        return playlist;
    }

    public @Nullable RadioSongPlayer getRadioSongPlayer() {
        return radioSongPlayer;
    }
}
