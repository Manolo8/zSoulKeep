package br.com.tinycraft.zsoulkeep;

import br.com.tinycraft.zsoulkeep.data.UserData;
import br.com.tinycraft.zsoulkeep.data.UserDataSQLite;
import br.com.tinycraft.zsoulkeep.listener.PlayerListener;
import br.com.tinycraft.zsoulkeep.runnable.SoulCounter;
import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Willian
 */
public class zSoulKeep extends JavaPlugin
{

    private UserData userData;
    private UserManager userManager;

    @Override
    public void onEnable()
    {
        userData = new UserDataSQLite(this);
        userManager = new UserManager(userData);

        getServer().getPluginManager().registerEvents(new PlayerListener(userManager), this);
        getServer().getScheduler().runTaskTimer(this, new SoulCounter(userManager), 20 * 60 * 60 * 2, 20);
        
        getCommand("souls").setExecutor(this);
    }

    @Override
    public void onDisable()
    {
        userManager.saveAllUsers();
        userData.saveData();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if(sender instanceof Player)
        {
            User user = userManager.getUser(((Player) sender).getUniqueId());
            Bukkit.getPlayer(user.getUuid()).sendMessage("§aVocê tem " + user.getSouls() + " almas.");
            
        }
        return true;
    }
}
