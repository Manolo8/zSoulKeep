package br.com.tinycraft.zsoulkeep;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Willian
 */
public final class SoulConfig
{

    private final FileConfiguration config;
    private final String language;
    private final int startSouls;
    private final int soulDelay;
    private final int soulLimit;
    private final boolean giveOffline;

    private final double _VERSION = 1.1;

    public FileConfiguration getConfig()
    {
        return config;
    }

    public SoulConfig(zSoulKeep plugin)
    {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        if (this.config.getDouble("Config.VERSION") != _VERSION)
        {
            File file = new File(plugin.getDataFolder(), "config.yml");
            file.delete();
            plugin.saveDefaultConfig();
            Bukkit.getLogger().info("---------------------------------");
            Bukkit.getLogger().info("A NEW CONFIG FILE HAS GENERATED!");
            Bukkit.getLogger().info("---------------------------------");
        }

        this.language = config.getString("Config.LANGUAGE", "pt_BR");
        this.soulLimit = config.getInt("Config.SOUL_LIMIT", 5);
        this.soulDelay = config.getInt("Config.SOUL_TIME_DELAY", 120);
        this.startSouls = config.getInt("Config.START_SOULS", 5);
        this.giveOffline = config.getBoolean("Config.GIVE_OFFLINE", true);
    }

    public String getLanguage()
    {
        return language;
    }

    public int getStartSouls()
    {
        return startSouls;
    }

    public int getSoulDelay()
    {
        return soulDelay;
    }

    public int getSoulLimit()
    {
        return soulLimit;
    }

    public boolean isGiveOffline()
    {
        return giveOffline;
    }
}
