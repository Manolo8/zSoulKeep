package br.com.tinycraft.zsoulkeep;

import br.com.tinycraft.zsoulkeep.commands.CommandManager;
import br.com.tinycraft.zsoulkeep.data.UserData;
import br.com.tinycraft.zsoulkeep.data.UserDataSQLite;
import br.com.tinycraft.zsoulkeep.language.Language;
import br.com.tinycraft.zsoulkeep.listener.PlayerListener;
import br.com.tinycraft.zsoulkeep.runnable.SoulCounter;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Willian
 */
public class zSoulKeep extends JavaPlugin
{

    private SoulConfig soulConfig;
    private UserData userData;
    private UserManager userManager;
    private CommandManager commandManager;
    private Language language;

    @Override
    public void onEnable()
    {
        soulConfig = new SoulConfig(this);
        userData = new UserDataSQLite(this);
        userManager = new UserManager(userData);
        language = new Language(this, soulConfig);
        commandManager = new CommandManager(userManager, language);

        getServer().getPluginManager().registerEvents(new PlayerListener(userManager, language, soulConfig), this);
        getServer().getScheduler().runTaskTimer(this,
                new SoulCounter(userManager, language), 20 * 60 * soulConfig.getSoulDelay(), 20);

        getCommand("souls").setExecutor(commandManager);
        getCommand("soulsadm").setExecutor(commandManager);
    }

    @Override
    public void onDisable()
    {
        userManager.saveAllUsers();
        userData.saveData();
    }

    public SoulConfig getSoulConfig()
    {
        return soulConfig;
    }
}
