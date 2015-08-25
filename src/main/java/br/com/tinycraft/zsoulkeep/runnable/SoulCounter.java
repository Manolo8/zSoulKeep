package br.com.tinycraft.zsoulkeep.runnable;

import br.com.tinycraft.zsoulkeep.language.Language;
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
    private Language language;

    public SoulCounter(UserManager userManager, Language language)
    {
        this.userManager = userManager;
        this.language = language;
    }

    @Override
    public void run()
    {
        Iterator<User> i = userManager.getUsers().iterator();
        long currentTime = System.currentTimeMillis();

        while (i.hasNext())
        {
            User user = i.next();
            int souls = user.updateSouls(currentTime);

            if (souls != 0)
            {
                Bukkit.getPlayer(user.getUuid()).sendMessage(language.getMessage("receive.soul", souls, user.getSouls()));
            }
        }
    }
}
