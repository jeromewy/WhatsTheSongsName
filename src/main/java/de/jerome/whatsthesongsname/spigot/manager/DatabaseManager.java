package de.jerome.whatsthesongsname.spigot.manager;

import de.jerome.whatsthesongsname.spigot.WITSNMain;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class DatabaseManager {

    private static final ConfigManager configManager = WITSNMain.getInstance().getConfigManager();

    private Connection connection;
    private Statement statement;

    public DatabaseManager() {
        reload();
    }

    public void reload() {
        if (!configManager.isDatabaseEnable()) return;
        disconnect();
        connect();
        checkTables();
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + configManager.getDatabaseHost() + ":" + configManager.getDatabasePort() + "/" + configManager.getDatabaseDatabase(), configManager.getDatabaseUsername(), configManager.getDatabasePassword());
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() {
        try {
            if (connection == null) return true;
            connection.close();
            connection = null;
            statement = null;
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void checkTables() {
        try {
            getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS witsn_players (UUID VARCHAR (37), POINTS INT, GUESSED_CORRECTLY INT, GUESSED_WRONG INT, PRIMARY KEY (UUID))");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public @Nullable Connection getConnection() {
        try {
            if (configManager.isDatabaseEnable())
                if (connection == null || connection.isClosed() || !connection.isValid(2))
                    connect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public @Nullable Statement getStatement() {
        try {
            statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statement;
    }
}
