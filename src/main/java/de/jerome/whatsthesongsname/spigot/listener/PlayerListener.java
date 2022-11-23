package de.jerome.whatsthesongsname.spigot.listener;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, WITSNMain.getInstance());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        WITSNMain.getInstance().getGameManager().leaveGame(event.getPlayer());
        WITSNMain.getInstance().getPlayerManager().getPlayer(event.getPlayer()).save();
    }

}
