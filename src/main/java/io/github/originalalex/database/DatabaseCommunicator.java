package io.github.originalalex.database;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class DatabaseCommunicator {

    private String url;
    private String root;
    private String rootPass;

    private Connection connection;

    public DatabaseCommunicator(JavaPlugin plugin) {
        ConfigurationSection dbConfig = plugin.getConfig().getConfigurationSection("Database");
        String url = dbConfig.getString("host");
        String port = dbConfig.getString("port");
        String user = dbConfig.getString("user");
        String pass = dbConfig.getString("pass");
        this.root = user;
        this.rootPass = pass;
        this.url = "jdbc:mysql://" + url + ":" + port;
        connect();
    }

    private void connect() {
        try {
            this.connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS bounties");
            statement.executeUpdate("USE bounties");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bounties(uuid text, bounty double)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBounty(String uuid, double newBounty) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE bounties SET bounty=" + newBounty + " WHERE uuid LIKE '" + uuid + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBounty(String uuid) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM bounties WHERE uuid LIKE '" + uuid + "'");
            if (!rs.next()) {
                return -1;
            }
            return rs.getDouble("bounty");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void placeBounty(String uuid, double value) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO bounties VALUES('" + uuid + "', " + value + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBounty(String uuid) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM bounties WHERE uuid LIKE '" + uuid + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getEverything() {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM bounties");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
