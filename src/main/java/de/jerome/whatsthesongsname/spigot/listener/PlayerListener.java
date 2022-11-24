package de.jerome.whatsthesongsname.spigot.listener;

import de.jerome.whatsthesongsname.spigot.WTSNMain;
import de.jerome.whatsthesongsname.spigot.object.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, WTSNMain.getInstance());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!WTSNMain.getInstance().getConfigManager().isBungeecordEnable()) return;
        Player player = event.getPlayer();

        // Does he have the permissions?
        if (!player.hasPermission("wtsn.play")) return;

        WTSNMain.getInstance().getGameManager().joinGame(player);
        player.sendMessage(WTSNMain.getInstance().getLanguagesManager().getMessage(player.getLocale(), Messages.JOIN_JOINED));
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        WTSNMain.getInstance().getGameManager().leaveGame(event.getPlayer());
        WTSNMain.getInstance().getPlayerManager().getPlayer(event.getPlayer()).save();
    }

}
