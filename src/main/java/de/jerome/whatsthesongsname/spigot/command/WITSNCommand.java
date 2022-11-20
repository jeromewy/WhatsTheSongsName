package de.jerome.whatsthesongsname.spigot.command;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.manager.ConfigManager;
import de.jerome.whatsthesongsname.spigot.object.Messages;
import de.jerome.whatsthesongsname.spigot.object.WITSNPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WITSNCommand implements CommandExecutor, TabExecutor {

    private static final ConfigManager configManager = WITSNMain.getInstance().getConfigManager();

    /*
     * help - / - Shows all commands
     * join - witsn.play - Join the game
     * leave - witsn.play - Leave the game
     * reload - witsn.reload - Reloads the songs and the configuration
     * stats - witsn.stats - Shows the statistics
     * stats [Spielername] - witsn.stats.other - Shows the statistics of a player
     */

    public WITSNCommand() {
        PluginCommand pluginCommand = WITSNMain.getInstance().getCommand("whatisthesongsname");
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {

        if (args.length == 0) {
            commandSender.sendMessage(configManager.getMessage(Messages.INFO)
                    .replaceAll("\\{pluginAuthors}", WITSNMain.getInstance().getDescription().getAuthors().toString())
                    .replaceAll("\\{commandName}", commandName));
            return true;
        }

        if (args.length == 1) {
            // Subcommand "join"
            if (args[0].equalsIgnoreCase("join")) {
                // Does he have the permissions?
                if (!commandSender.hasPermission("witsn.play")) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                // Is he a player?
                if (!(commandSender instanceof Player player)) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                // Is he already in the game? If not, he enters it
                if (!WITSNMain.getInstance().getGameManager().joinGame(player)) {
                    commandSender.sendMessage(configManager.getMessage(Messages.JOIN_ALREADY_IN_GAME));
                    return true;
                }

                commandSender.sendMessage(configManager.getMessage(Messages.JOIN_JOINED));
                return true;
            }

            // Subcommand "leave"
            if (args[0].equalsIgnoreCase("leave")) {
                // Does he have the permissions?
                if (!commandSender.hasPermission("witsn.play")) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                // Is he a player?
                if (!(commandSender instanceof Player player)) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                // Is he in game? If so, he leaves
                if (!WITSNMain.getInstance().getGameManager().leaveGame(player)) {
                    commandSender.sendMessage(configManager.getMessage(Messages.LEAVE_NOT_IN_GAME));
                    return true;
                }

                commandSender.sendMessage(configManager.getMessage(Messages.JOIN_JOINED));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                // Does he have the permissions?
                if (!commandSender.hasPermission("witsn.reload")) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                if (!WITSNMain.getInstance().reload()) {
                    commandSender.sendMessage(configManager.getMessage(Messages.RELOAD_FAILED));
                }

                commandSender.sendMessage(configManager.getMessage(Messages.RELOAD_SUCCESS));
                return true;
            }

            if (args[0].equalsIgnoreCase("stats")) {
                // Does he have the permissions?
                if (!commandSender.hasPermission("witsn.stats")) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                // Is he a player?
                if (!(commandSender instanceof Player player)) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                WITSNPlayer witsnPlayer = WITSNMain.getInstance().getPlayerManager().getPlayer(player);

                // The Stats of the player
                commandSender.sendMessage(configManager.getMessage(Messages.STATS_OWN)
                        .replaceAll("\\{playerPoints}", String.valueOf(witsnPlayer.getPoints()))
                        .replaceAll("\\{playerGuessedCorrectly}", String.valueOf(witsnPlayer.getGuessedCorrectly()))
                        .replaceAll("\\{playerGuessedWrong}", String.valueOf(witsnPlayer.getGuessedWrong())));
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("stats")) {
                // Does he have the permissions?
                if (!commandSender.hasPermission("witsn.stats.other")) {
                    sendSyntaxMessage(commandSender, command, commandName, args);
                    return true;
                }

                WITSNPlayer witsnTarget = WITSNMain.getInstance().getPlayerManager().getPlayer(args[1]);

                // Is there the player from whom the stats are requested?
                if (witsnTarget == null) {
                    commandSender.sendMessage(configManager.getMessage(Messages.STATS_OTHER_PLAYER_NOT_FOUND)
                            .replaceAll("\\{playerName}", args[1]));
                    return true;
                }

                // The Stats of the player
                commandSender.sendMessage(configManager.getMessage(Messages.STATS_OWN)
                        .replaceAll("\\{playerName}", String.valueOf(witsnTarget.getName()))
                        .replaceAll("\\{playerPoints}", String.valueOf(witsnTarget.getPoints()))
                        .replaceAll("\\{playerGuessedCorrectly}", String.valueOf(witsnTarget.getGuessedCorrectly()))
                        .replaceAll("\\{playerGuessedWrong}", String.valueOf(witsnTarget.getGuessedWrong())));
                return true;
            }
        }

        sendSyntaxMessage(commandSender, command, commandName, args);
        return true;
    }

    /*
     * help - / - Shows all commands
     * join - witsn.play - Join the game
     * leave - witsn.play - Leave the game
     * reload - witsn.reload - Reloads the songs and the configuration
     * stats - witsn.stats - Shows the statistics
     * stats [Spielername] - witsn.stats.other - Shows the statistics of a player
     */

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            tab.add("help");
            if (commandSender.hasPermission("witsn.play")) {
                tab.add("join");
                tab.add("leave");
            }
            if (commandSender.hasPermission("witsn.reload")) tab.add("reload");
            if (commandSender.hasPermission("witsn.stats")) tab.add("stats");
        }

        if (args.length == 2)

            if (commandSender.hasPermission("witsn.stats.other"))
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) tab.add(onlinePlayer.getName());

        return tab;
    }

    private void sendSyntaxMessage(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        boolean minOnePermission = false;
        boolean player = commandSender instanceof Player;

        commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_INFO));
        if (player && commandSender.hasPermission("witsn.play")) {
            minOnePermission = true;
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_JOIN).replaceAll("\\{commandName}", commandName));
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_LEAVE).replaceAll("\\{commandName}", commandName));
        }

        if (commandSender.hasPermission("witsn.reload")) {
            minOnePermission = true;
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_RELOAD).replaceAll("\\{commandName}", commandName));
        }

        if (commandSender.hasPermission("witsn.stats.other")) {
            minOnePermission = true;
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_STATS_OTHER).replaceAll("\\{commandName}", commandName));
        } else if (player && commandSender.hasPermission("witsn.stats")) {
            minOnePermission = true;
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_STATS_OWN).replaceAll("\\{commandName}", commandName));
        }

        if (!minOnePermission)
            commandSender.sendMessage(configManager.getMessage(Messages.SYNTAX_NO_PERMISSION).replaceAll("\\{commandName}", commandName));
    }
}
