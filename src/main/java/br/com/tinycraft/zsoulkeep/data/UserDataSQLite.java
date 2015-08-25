package br.com.tinycraft.zsoulkeep.data;

import br.com.tinycraft.zsoulkeep.SoulConfig;
import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.zSoulKeep;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

/**
 *
 * @author Willian
 */
public class UserDataSQLite implements UserData
{

    private Connection connection;
    private SoulConfig soulConfig;

    public UserDataSQLite(zSoulKeep plugin)
    {
        this.soulConfig = plugin.getSoulConfig();
        try
        {
            Class.forName("org.sqlite.JDBC");
            plugin.getDataFolder().mkdir();
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/data.db");

            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS Users "
                    + "(uuid               TEXT     NOT NULL,"
                    + " lastGive           REAL     NOT NULL, "
                    + " souls              INT      NOT NULL,"
                    + " soulsLimit         INT      NOT NULL,"
                    + "PRIMARY KEY(uuid));");
            stat.close();

        } catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public User loadUser(UUID uuid)
    {
        try
        {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM Users WHERE uuid='" + uuid + "';");
            User user;
            if (rs.next())
            {
                Long lastGive = rs.getLong("lastGive");
                int souls = rs.getInt("souls");
                int limit = rs.getInt("soulsLimit");

                user = new User(uuid, lastGive, souls, limit, soulConfig.getSoulDelay());
            } else
            {
                user = new User(uuid, System.currentTimeMillis(),
                        soulConfig.getStartSouls(),
                        soulConfig.getSoulLimit(),
                        soulConfig.getSoulDelay());
            }

            return user;

        } catch (SQLException ex)
        {
            Logger.getLogger(UserDataSQLite.class.getName()).log(Level.SEVERE, null, ex);
            return new User(uuid, System.currentTimeMillis(),
                    soulConfig.getStartSouls(),
                    soulConfig.getSoulLimit(),
                    soulConfig.getSoulDelay());
        }
    }

    @Override
    public void saveUser(User user)
    {
        user.setLastGive(System.currentTimeMillis());

        try
        {
            Statement stat = connection.createStatement();
            stat.executeUpdate("INSERT OR REPLACE INTO Users(uuid, lastGive, souls, soulsLimit) "
                    + "VALUES('" + user.getUuid() + "', '"
                    + user.getLastGive() + "', ' "
                    + user.getSouls() + "', '"
                    + user.getLimit() + "');");

        } catch (Exception ex)
        {
            Logger.getLogger(UserDataSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveData()
    {
        try
        {
            connection.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(UserDataSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
