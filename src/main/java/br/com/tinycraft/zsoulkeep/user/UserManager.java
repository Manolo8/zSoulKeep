package br.com.tinycraft.zsoulkeep.user;

import br.com.tinycraft.zsoulkeep.SoulConfig;
import br.com.tinycraft.zsoulkeep.data.UserData;
import br.com.tinycraft.zsoulkeep.exceptions.NoUserDataException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Willian
 */
public class UserManager
{

    private final UserData userData;
    private List<User> users;
    private SoulConfig soulConfig;

    public UserManager(UserData userData, SoulConfig soulConfig)
    {
        this.userData = userData;
        this.soulConfig = soulConfig;

        users = new ArrayList();

        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            loadUser(player);
        }
    }

    public User getUser(UUID uuid, boolean remove)
    {
        Iterator<User> i = users.iterator();

        while (i.hasNext())
        {
            User user = i.next();

            if (user.getUuid().equals(uuid))
            {
                if (remove)
                {
                    i.remove();
                }
                return user;
            }
        }
        return null;
    }

    public User getUser(UUID uuid)
    {
        return getUser(uuid, false);
    }

    public User loadUser(Player player)
    {
        User user;
        try
        {
            user = userData.loadUser(player.getUniqueId());
        } catch (NoUserDataException e)
        {
            e.printStackTrace();
            user = new User(player.getUniqueId(),
                    System.currentTimeMillis(),
                    soulConfig.getStartSouls(),
                    soulConfig.getSoulLimit(),
                    soulConfig.getSoulDelay());
        }
        users.add(user);
        return user;
    }

    public void saveUser(Player player)
    {
        User user = getUser(player.getUniqueId(), true);

        if (user != null)
        {
            saveUser(user);
        }
    }

    public List<User> getUsers()
    {
        return this.users;
    }

    public void saveUser(User user)
    {
        userData.saveUser(user);
    }

    public void loadAllUsers()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            loadUser(player);
        }
    }

    public void saveAllUsers()
    {
        for (User user : users)
        {
            saveUser(user);
        }
    }
}
