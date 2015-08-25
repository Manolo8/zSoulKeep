package br.com.tinycraft.zsoulkeep.listener;

import br.com.tinycraft.zsoulkeep.SoulConfig;
import br.com.tinycraft.zsoulkeep.language.Language;
import br.com.tinycraft.zsoulkeep.user.User;
import br.com.tinycraft.zsoulkeep.user.UserManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Willian
 */
public class PlayerListener implements Listener
{
    
    private final UserManager userManager;
    private final Language language;
    private final boolean keepXP;
    private final boolean keepInventory;
    private final boolean giveOffLine;
    private final HashMap<Player, List<ItemStack[]>> playersItems;
    
    public PlayerListener(UserManager userManager, Language language, SoulConfig soulConfig)
    {
        this.userManager = userManager;
        this.language = language;
        this.keepInventory = true;
        this.keepXP = true;
        this.giveOffLine = soulConfig.isGiveOffline();
        playersItems = new HashMap();
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        User user = userManager.loadUser(e.getPlayer());
        int givedSouls = 0;
        if (giveOffLine)
        {
            givedSouls = user.updateSouls(System.currentTimeMillis());
        } else
        {
            user.setLastGive(System.currentTimeMillis());
        }
        
        if (givedSouls == 0)
        {
            e.getPlayer().sendMessage(language.getMessage("login.soul.amount", user.getSouls()));
        } else
        {
            e.getPlayer().sendMessage(language.getMessage("login.soul.receive", givedSouls, user.getSouls()));
        }
    }
    
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e)
    {
        userManager.saveUser(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }
        
        userManager.saveUser(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e)
    {
        if (this.playersItems.containsKey(e.getPlayer()))
        {
            Player player = e.getPlayer();
            
            int row = 0;
            
            for (ItemStack[] items : playersItems.get(e.getPlayer()))
            {
                if (row == 0)
                {
                    player.getInventory().setArmorContents(items);
                } else
                {
                    player.getInventory().setContents(items);
                }
                row++;
            }
        }
        
        this.playersItems.remove(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeathEvent(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        User user = userManager.getUser(player.getUniqueId());
        if (user.lostSoul() == 0)
        {
            player.sendMessage(language.getMessage("death.no.soul"));
            return;
        }
        
        boolean inventory = player.hasPermission("zsoulkeep.keep.inventory");
        boolean exp = player.hasPermission("zsoulkeep.keep.exp");
        int almas = user.getSouls();
        
        if (almas == 0)
        {
            player.sendMessage(language.getMessage("death.last.soul"));
        } else
        {
            player.sendMessage(language.getMessage("death.moreone.soul", almas));
        }
        
        if (keepInventory && inventory)
        {
            List<ItemStack[]> items = new ArrayList();
            items.add(e.getEntity().getInventory().getArmorContents());
            items.add(e.getEntity().getInventory().getContents());
            
            this.playersItems.put(player, items);
            
            e.getDrops().clear();
        }
        
        if (keepXP && exp)
        {
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        }
    }
}
