package br.com.tinycraft.zsoulkeep.runnable;

import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Willian
 */
public class SoulCounter extends BukkitRunnable
{

    private UserManager userManager;

    public SoulCounter(UserManager userManager)
    {
        this.userManager = userManager;
    }

    @Override
    public void run()
    {
        Iterator<User> i = userManager.getUsers().iterator();
        long currentTime = System.currentTimeMillis();

        while (i.hasNext())
        {
            User user = i.next();
            if(user.giveSoul())
            {
                Bukkit.getPlayer(user.getUuid()).sendMessage("§aVocê recebeu uma alma! Agora tem " + user.getSouls() + " almas.");
            }
        }
    }
}
