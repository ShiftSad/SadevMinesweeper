package me.sadev.campominado.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.sadev.campominado.CampoMinado;
import org.bukkit.configuration.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final HikariDataSource dataSource;

    public Database(CampoMinado plugin) {
        // Setup database
        HikariConfig config = new HikariConfig();
        Configuration cnfg = plugin.getConfig();

        config.setJdbcUrl(cnfg.getString("mysql.jbdcUrl"));
        config.setUsername(cnfg.getString("mysql.username"));
        config.setPassword(cnfg.getString("mysql.password"));
        config.setDriverClassName(cnfg.getString("mysql.driverClassName"));
        config.setMaximumPoolSize(cnfg.getInt("mysql.poolSize"));

        dataSource = new HikariDataSource(config);

        // Create tables if not exist
        createTable();
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS sapiens_Vips (Name varchar(50) not null primary key,UUID varchar(36) not null,Started BIGINT not null,Permission varchar(50) not null);";
        try (Connection con = dataSource.getConnection()) {
            con.prepareStatement(query).execute();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public HikariDataSource dataSource() {
        return dataSource;
    }
}
