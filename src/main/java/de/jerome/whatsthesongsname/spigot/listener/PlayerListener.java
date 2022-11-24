package de.jerome.whatsthesongsname.spigot.listener;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, WTSNMain.getInstance());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        WTSNMain.getInstance().getGameManager().leaveGame(event.getPlayer());
        WTSNMain.getInstance().getPlayerManager().getPlayer(event.getPlayer()).save();
    }

}
